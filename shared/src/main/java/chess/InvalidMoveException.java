package chess;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidMoveException extends Exception {
    public Enum typeOfException;
    public enum ExceptionType {
       STARTNULL,
        WRONGTURN,

        INVALIDMOVE,


    }
    public InvalidMoveException(ExceptionType e) {
        this.typeOfException = e;
    }

    public InvalidMoveException(String message) {
        super(message);
    }
}
