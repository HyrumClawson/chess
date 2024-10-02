package chess;

import java.util.Collection;
import java.util.Objects;
import java.io.*;
import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameBoard=new ChessBoard();
    Stack<ChessGame.TeamColor> teamTurn=new Stack<>();

    public ChessGame() {
        gameBoard.resetBoard();
        teamTurn.push(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        boolean pushBlack=false;
        if (TeamColor.WHITE == teamTurn.peek()) {
            pushBlack=true;
        }
        ChessGame.TeamColor turn=teamTurn.pop();
        if (pushBlack) {
            teamTurn.push(TeamColor.BLACK);
        } else {
            teamTurn.push(TeamColor.WHITE);
        }

        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn.push(team);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame=(ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        } else {
            return gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        /**
         * Can't make move if isInCheck is true unless its to get the king out of danger
         * very interesting...
         */
        if(gameBoard.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException();
        }
        else {
            if(teamTurn.peek() != gameBoard.getPiece(move.getStartPosition()).pieceColor){
                throw new InvalidMoveException();
            }
            boolean isValid=CheckMoveValidity(move);
            if (isValid) {
                //function that changes the board.
                UpdateBoard(move);
                if(gameBoard.getPiece(move.getEndPosition()).pieceColor == TeamColor.WHITE){
                    teamTurn.push(TeamColor.BLACK);
                }
                else{
                    teamTurn.push(TeamColor.WHITE);
                }
            }
            else {
                throw new InvalidMoveException();
            }
        }


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<ChessMove> opposingTeamMoves = new ArrayList<>();
        //function to get opposing team moves
        opposingTeamMoves = GetOpposingTeamMoves(teamColor);
        ChessPosition kingPosition = gameBoard.getPosition(teamColor, ChessPiece.PieceType.KING).getFirst();
        return IsKingInCheck(opposingTeamMoves, kingPosition);
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ArrayList<ChessMove> opposingTeamMoves = new ArrayList<>();
        opposingTeamMoves = GetOpposingTeamMoves(teamColor);

        if(isInCheck(teamColor)){
            ChessPosition kingPosition = gameBoard.getPosition(teamColor, ChessPiece.PieceType.KING).getFirst();
            Collection<ChessMove> kingMoves = gameBoard.getPiece(kingPosition).pieceMoves(gameBoard,kingPosition);
            ArrayList<ChessMove> kings = new ArrayList<>();
            kings.addAll(kingMoves);
            return CheckMovesAgainstMoves( opposingTeamMoves,  kings);
//            return true;
        }
        else{
            return false;
        }

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard=board;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }


    boolean CheckMoveValidity(ChessMove move) {
        boolean isValid=false;
        ChessPiece thePiece=gameBoard.getPiece(move.getStartPosition());
        // technically it's not all the valid moves... there will be extras unfortunately
        Collection<ChessMove> ValidMoves=thePiece.pieceMoves(gameBoard, move.getStartPosition());
        for (ChessMove thing : ValidMoves) {
            if (thing.getEndPosition().getRow() == move.getEndPosition().getRow()
            && thing.getEndPosition().getColumn() == move.getEndPosition().getColumn() ) {
                isValid=true;
                break;
            }
        }

        return isValid;
    }

    public void UpdateBoard(ChessMove move){
        if(gameBoard.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN
        && ( move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1  )){
            ChessPiece piece = new ChessPiece(gameBoard.getPiece(move.getStartPosition()).getTeamColor(),
                       move.getPromotionPiece());
            gameBoard.addPiece(move.getEndPosition(), piece);
        }
        else{
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
        }

        gameBoard.addPiece(move.getStartPosition(), null);

    }

    ArrayList<ChessMove> GetOpposingTeamMoves(ChessGame.TeamColor teamColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (int i=0; i < gameBoard.getSquares().length; i++) {
            for (int j=0; j < gameBoard.getSquares().length; j++) {
                if(gameBoard.getSquares()[i][j] != null){
                    if(gameBoard.getSquares()[i][j].getTeamColor() != teamColor){
                        ChessPosition start = new ChessPosition(i, j);
                        Collection<ChessMove> pieceMoves = gameBoard.getSquares()[i][j].pieceMoves(gameBoard, start);
                        moves.addAll(pieceMoves);
                    }

                }
                else{
                    //don't do anything
                }
            }
        }
//        for(ChessPiece[] row : gameBoard.getSquares()){
//            for(ChessPiece piece : row){
//                if(piece.getTeamColor() != teamColor && piece != null){
//                    moves.add(piece.pieceMoves(gameBoard, ))
//                }
//            }
//        }
        return moves;
    }

    public boolean IsKingInCheck( ArrayList<ChessMove> opposingTeamMoves, ChessPosition kingPosition){
        for(ChessMove opposeMove : opposingTeamMoves){
            int row = opposeMove.getEndPosition().getRow();
            int col = opposeMove.getEndPosition().getColumn();
            if( row == kingPosition.getRow() && col == kingPosition.getColumn()){
                return true;
            }
        }
        return false;
    }

    public boolean CheckMovesAgainstMoves(ArrayList<ChessMove> opposingTeamMoves, ArrayList<ChessMove> kings){
        boolean canMove = true;
        for(ChessMove king : kings){
            for(ChessMove oppose: opposingTeamMoves){
                int kRow = king.getEndPosition().getRow();
                int kCol = king.getEndPosition().getColumn();
                int oRow = oppose.getEndPosition().getRow();
                int oCol = oppose.getEndPosition().getColumn();
                if(kRow == oRow && kCol == oCol){
                    canMove = false;
                }
                else{
                    canMove = true;
                    break;
                }
            }
        }

        return canMove;
    }

}
