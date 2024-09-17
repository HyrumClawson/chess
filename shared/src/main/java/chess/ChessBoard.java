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
   // ChessPosition position;
    //ChessPiece piece;

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
        for(int i = 0; i < squares.length; i++){
            for(int j = 0; j < squares.length; j++){
                squares[i][j] = null;
            }
        }
        ChessPiece[][] startBoard = MakeStartBoard();

        for(int i = 0; i < squares.length; i++){
            for(int j = 0; j < squares.length; j++){
                squares[i][j] = startBoard[i][j];

            }
        }

        //throw new RuntimeException("Not implemented");
       // ChessBoard resetBoard = newChess
    }

     ChessPiece[][] MakeStartBoard() {
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

        ChessPiece[][] startBoard = {
                {null, null, null, null, null, null, null, null, null, null},
                {null, whiteRook, whiteKnight, whiteBishop, whiteQueen, whiteKing, whiteBishop, whiteKnight, whiteRook, null},
                {null, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, null},
                {null, blackRook, blackKnight, blackBishop, blackQueen, blackKing, blackBishop, blackKnight, blackRook, null},
                {null, null, null, null, null, null, null, null, null, null},

        };
        return startBoard;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that=(ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ChessBoard that=(ChessBoard) o;
//        return Arrays.deepEquals(squares, that.squares) && Objects.equals(position, that.position) && Objects.equals(piece, that.piece);
//    }
//
//    @Override
//    public int hashCode() {
//        int result=Objects.hash(position, piece);
//        result=31 * result + Arrays.deepHashCode(squares);
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        return "ChessBoard{" +
//                "squares=" + Arrays.toString(squares) +
//                ", position=" + position +
//                ", piece=" + piece +
//                '}';
//    }
}
