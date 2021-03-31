package nl.hu.cisq1.lingo.trainer.domain.exceptions;


public class InvalidFeedBackException extends RuntimeException{
    public InvalidFeedBackException(String message){
        super(message);
    }

    public static InvalidFeedBackException wrongLength(){
        return new InvalidFeedBackException(

                        "Given wordlength and marks length don't match"


        );
    }
    public static InvalidFeedBackException invalidMarks(){
        return new InvalidFeedBackException(
                        "Either given marks or attempt length does not match length word to guess."
        );
    }
}
