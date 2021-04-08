package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class InvalidWordException extends RuntimeException{
    public InvalidWordException(String message){
        super(message);
    }

    public static InvalidWordException invalidLength(){
        return new InvalidWordException(
                "Length wordToGuess is not right."
        );
    }
    public static InvalidWordException wordDoesNotExist(){
        return new InvalidWordException(
                "guessed word does not exist."
        );
    }
}
