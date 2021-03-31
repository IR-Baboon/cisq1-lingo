package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class InvalidRoundException extends RuntimeException {
    public InvalidRoundException(String message){
        super(message);
    }

    public static InvalidRoundException roundActive(){
        return new InvalidRoundException(
                "There is already an active round. complete that first"
        );
    }
}
