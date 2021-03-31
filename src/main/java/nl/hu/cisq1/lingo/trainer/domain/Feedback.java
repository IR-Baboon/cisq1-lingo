package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidFeedBackException;

import java.util.List;

public class Feedback {
    private String attempt;
    private List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) throws InvalidFeedBackException {
        if(attempt.length() != marks.size()){
            throw InvalidFeedBackException.wrongLength();
        }
        this.attempt = attempt;
        this.marks = marks;
    }

    public Boolean isWordGuessed(){
        return this.marks.stream().allMatch(mark -> mark == Mark.CORRECT);
    }

    public Boolean guessIsInvalid(){
        return this.marks.stream().anyMatch(mark -> mark == Mark.INVALID);
    }


    public String giveHint(String previousHint) throws InvalidFeedBackException {
        // size of previous hint and attempt or marks do not match
        if(previousHint.length() != attempt.length() || previousHint.length() != marks.size()){
            // throw exception
            throw InvalidFeedBackException.invalidMarks();
        }
        // transform de vorige hint naar een charArray.
        // Hierin gaan we letters veranderen als een mark op CORRECT staat.
        char[] hint = previousHint.toCharArray();
        // for loop om door de markeringen heen te loopen. De loop stopt
        for(int index = 0; index < attempt.length(); index++){
            // als de markering
            if(marks.get(index) == Mark.CORRECT){
                hint[index] = attempt.charAt(index);
            }
        }
        // geef de nieuwe hint terug
        return String.valueOf(hint);
    }

}
