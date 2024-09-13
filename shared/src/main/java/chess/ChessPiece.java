package chess;

import java.util.Collection;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    ChessGame.TeamColor pieceColor;
    PieceType type;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch(board.getPiece(myPosition).getPieceType()){
            case KING :
                return new ArrayList<>();
            case QUEEN:
                QueenMoves queenmoves = new QueenMoves();
                return queenmoves.GetAllMoves(board,myPosition);
            case BISHOP:
                BishopMoves newMoves = new BishopMoves();
                return newMoves.GetAllMoves(board, myPosition);
            case KNIGHT:
                return new ArrayList<>();
            case ROOK:
                return new ArrayList<>();
            case PAWN:
                return new ArrayList<>();

        }
        ArrayList<ChessMove> testing = new ArrayList<ChessMove>();
        ChessMove testMove = new ChessMove(myPosition, myPosition, board.getPiece(myPosition).getPieceType() );
        testing.add(testMove);
        return testing;
        /**
         * have this call the PieceMovesCalculator interface/class
         **/
        //throw new RuntimeException("Not implemented");
    }
}