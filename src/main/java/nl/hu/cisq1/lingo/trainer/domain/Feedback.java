package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidFeedBackException;

import java.util.List;
import java.util.Objects;


public class Feedback {
    private String attempt;
    private List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) throws InvalidFeedBackException {
        if(attempt.length() != marks.size()){
            throw InvalidFeedBackException.wrongLength(attempt, marks);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        Feedback feedback = (Feedback) o;
        return attempt.equals(feedback.attempt) &&
                marks.equals(feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, marks);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", marks=" + marks +
                '}';
    }

    public String giveHint(String previousHint, String wordToGuess){
        char[] previous = previousHint.toCharArray();
        char[] gameword = wordToGuess.toCharArray();

        for(int letter = 0; letter < gameword.length; letter += 1){
            if(gameword[letter] != previous[letter]){
                if(marks.get(letter) == Mark.CORRECT){
                    previous[letter] = gameword[letter];
                }
            }
        }
        return String.valueOf(previous);
    }

    public static Boolean correct(String woord){
        return true;
    }
    public static Boolean invalid(String woord){
        return true;
    }
}
