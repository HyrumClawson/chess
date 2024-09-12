package chess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[9][9];


//    Map<ChessPosition, ChessPiece> ChessPiecePositions = new HashMap<>();
    ChessPosition position;
    ChessPiece piece;

    /** note
     * This could be the wrong place to put these two fields ^^^^
     */

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;

//        this.position = position;
//        this.piece = piece;
//        ChessPiecePositions.put(position,piece);

       // throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
//        for(Map.Entry<ChessPosition,ChessPiece> entry : ChessPiecePositions.entrySet()){
//            if(entry.getKey() == position){
//                return entry.getValue();
//            }
//        }

        //return null;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that=(ChessBoard) o;
        return Arrays.equals(squares, that.squares) && Objects.equals(position, that.position) && Objects.equals(piece, that.piece);
    }

    @Override
    public int hashCode() {
        int result=Objects.hash(position, piece);
        result=31 * result + Arrays.hashCode(squares);
        return result;
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                ", position=" + position +
                ", piece=" + piece +
                '}';
    }
}
