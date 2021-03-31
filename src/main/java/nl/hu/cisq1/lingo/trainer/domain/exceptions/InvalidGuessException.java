package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class InvalidGuessException extends RuntimeException {
    public InvalidGuessException(String message){
        super(message);
    }

    public static InvalidGuessException noMoreGuesses(){
        return new InvalidGuessException(
                "Round is over, attempts are empty"
        );
    }
    public static InvalidGuessException wordIsGuessed(){
        return new InvalidGuessException(
                "Round is over. The word has been guessed"
        );
    }
    public static InvalidGuessException playerDefeated(){
        return new InvalidGuessException(
                "Round is over. You have been eliminated."
        );
    }
}
