package nl.hu.cisq1.lingo.trainer.domain.exceptions;

import nl.hu.cisq1.lingo.trainer.domain.Mark;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class InvalidFeedBackException extends RuntimeException{
    public InvalidFeedBackException(String message){
        super(message);
    }

    public static InvalidFeedBackException wrongLength(String woord, List<Mark> marks){
        return new InvalidFeedBackException(
                String.format(
                        "Given wordlength (%s) and marks length (%s) don't match",
                        woord.length(),
                        marks.size()
                )
        );
    }
    public static InvalidFeedBackException invalidMarks(){
        return new InvalidFeedBackException(
                        "Either given marks or attempt length does not match length word to guess."
        );
    }
}
