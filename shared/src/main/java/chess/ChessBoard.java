package chess;

import java.util.HashMap;
import java.util.Map;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];


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
}
