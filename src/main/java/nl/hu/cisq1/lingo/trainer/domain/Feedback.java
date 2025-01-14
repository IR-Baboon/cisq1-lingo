package nl.hu.cisq1.lingo.trainer.domain;


import nl.hu.cisq1.lingo.trainer.domain.converters.MarkConverter;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidFeedBackException;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="feedback")
public class Feedback {
    @Id
    @GeneratedValue
    private int id;

    private String attempt;

    @Convert(converter = MarkConverter.class)
    private List<Mark> marks;

    public Feedback(){}
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

    @Override
    public int hashCode() {
        return Objects.hash(attempt, marks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) &&
                Objects.equals(marks, feedback.marks);
    }
//    @Override
//    public String toString() {
//        return "Feedback{" +
//                ", attempt='" + attempt + '\'' +
//                ", marks=" + marks +
//                '}';
//    }
}
