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
    public String reason;
    public InvalidMoveException(ExceptionType e) {
        this.typeOfException = e;
        this.reason = switch(e){
          case INVALIDMOVE -> "Invalid move";
          case WRONGTURN -> "Not your turn";
          case STARTNULL -> "Trying to move from empty square";
          default -> "Other issue making a move";
        };

    }

    public InvalidMoveException(String message) {
        super(message);
    }
}
