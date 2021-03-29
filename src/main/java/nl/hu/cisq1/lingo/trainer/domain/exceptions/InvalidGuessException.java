package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class InvalidGuessException extends RuntimeException {
    public InvalidGuessException(String message){
        super(message);
    }

    public static InvalidGuessException noMoreGuesses(){
        return new InvalidGuessException(
                "Round is over, guesses are Empty"
        );
    }
}
