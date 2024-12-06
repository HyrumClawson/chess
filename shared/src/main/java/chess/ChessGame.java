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
    private ChessBoard gameBoard = new ChessBoard();
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
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
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
        }
        else {
            //I have a lot to add to this section
            ChessGame.TeamColor color = gameBoard.getPiece(startPosition).pieceColor;
            ArrayList<ChessMove> validMoves = new ArrayList<>();
            Collection<ChessMove> potentialMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
            validMoves = kingIsInCheckMoves(potentialMoves, validMoves, color);
            return validMoves;
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
            throw new InvalidMoveException(InvalidMoveException.ExceptionType.STARTNULL);
        }
        else {
            if(teamTurn.peek() != gameBoard.getPiece(move.getStartPosition()).pieceColor){
                throw new InvalidMoveException(InvalidMoveException.ExceptionType.WRONGTURN);
            }
            //Collection<ChessMove> goodMoves = validMoves(move.getStartPosition());
            //goodMoves.contains(move);
            boolean isValid=checkMoveValidity(move);
            if (isValid) {
                updateBoard(move, gameBoard);
                //UpdateTeamTurn(move);
                if(gameBoard.getPiece(move.getEndPosition()).pieceColor == TeamColor.WHITE){
                    teamTurn.push(TeamColor.BLACK);
                }
                else{
                    teamTurn.push(TeamColor.WHITE);
                }
            }
            else {
                throw new InvalidMoveException(InvalidMoveException.ExceptionType.INVALIDMOVE);
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
        ArrayList<ChessMove> opposingTeamMoves;
        opposingTeamMoves = getOpposingTeamMoves(teamColor, gameBoard);
        ChessPosition kingPosition = gameBoard.getPosition(teamColor, ChessPiece.PieceType.KING).getFirst();
        return isKingInCheck(opposingTeamMoves, kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        TeamColor opposingColor = getOpposingColor(teamColor);
        if(isInCheck(teamColor)){
            return isInCheckMateOrStalemate(teamColor, opposingColor);
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
        TeamColor opposingColor = getOpposingColor(teamColor);
        if(isInCheckmate(teamColor)){
            return false;
        }
        else{
            return isInCheckMateOrStalemate(teamColor, opposingColor);
        }
        // essentially if every move of the team's piece results in isInCheck being true that
        // team is in checkmate.
    }



    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard=board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }


    boolean checkMoveValidity(ChessMove move) {
        boolean isValid=false;
        ArrayList<ChessPosition> kingPosition =gameBoard.getPosition
                (gameBoard.getPiece(move.getStartPosition()).pieceColor, ChessPiece.PieceType.KING);
        if(!kingPosition.isEmpty() &&
                isInCheck(gameBoard.getPiece(move.getStartPosition()).pieceColor)){
            ChessBoard dummyBoard = gameBoard;
            updateBoard(move, dummyBoard);
            if(isInCheck(dummyBoard.getPiece(move.getEndPosition()).pieceColor)){
                isValid = false;
                return isValid;
            }
        }
        ChessPiece thePiece=gameBoard.getPiece(move.getStartPosition());
        Collection<ChessMove> validMoves=thePiece.pieceMoves(gameBoard, move.getStartPosition());
        for (ChessMove thing : validMoves) {
            if (thing.getEndPosition().getRow() == move.getEndPosition().getRow()
            && thing.getEndPosition().getColumn() == move.getEndPosition().getColumn() ) {
                isValid=true;
                break;
            }
        }

        return isValid;
    }

    public void updateBoard(ChessMove move, ChessBoard board){
        if(board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN
        && ( move.getEndPosition().getRow() == 8 || move.getEndPosition().getRow() == 1  )){
            ChessPiece piece = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),
                       move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), piece);
        }
        else{
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        }
        board.addPiece(move.getStartPosition(), null);
    }

    ArrayList<ChessMove> getOpposingTeamMoves(ChessGame.TeamColor teamColor, ChessBoard board) {
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
        return moves;
    }

    public boolean isKingInCheck( ArrayList<ChessMove> opposingTeamMoves, ChessPosition kingPosition){
        for(ChessMove opposeMove : opposingTeamMoves){
            int row = opposeMove.getEndPosition().getRow();
            int col = opposeMove.getEndPosition().getColumn();
            if( row == kingPosition.getRow() && col == kingPosition.getColumn()){
                return true;
            }
        }
        return false;
    }

    public boolean isInCheckMateOrStalemate(TeamColor teamColor, TeamColor opposingColor){
        Collection<ChessMove> kingTeamMoves = getOpposingTeamMoves(opposingColor, gameBoard);
        ArrayList<ChessMove> kingTeam = new ArrayList<>();
        kingTeam.addAll(kingTeamMoves);
        boolean isInCheckEverywhere = false;
        for (ChessMove teamMove : kingTeam){
            ChessPiece potentialPiece = gameBoard.getPiece(teamMove.getEndPosition());
            updateBoard(teamMove, gameBoard);
            if(!isInCheck(teamColor)){
                return false;
            }
            else{
                isInCheckEverywhere = true;
            }
            ChessMove undoMove = new ChessMove(teamMove.getEndPosition(),teamMove.getStartPosition(), teamMove.getPromotionPiece());
            updateBoard(undoMove, gameBoard);
            gameBoard.addPiece(teamMove.getEndPosition(), potentialPiece);
        }
        return isInCheckEverywhere;
    }

    TeamColor getOpposingColor(TeamColor teamColor){
        TeamColor opposingColor;
        if(TeamColor.WHITE == teamColor){
            opposingColor = TeamColor.BLACK;
        }
        else{
            opposingColor = TeamColor.WHITE;
        }
        return opposingColor;
    }

    ArrayList<ChessMove> kingIsInCheckMoves(Collection<ChessMove> potentialMoves, ArrayList<ChessMove> validMoves, ChessGame.TeamColor color){
        for(ChessMove move : potentialMoves){
            ChessPiece potentialPiece = gameBoard.getPiece(move.getEndPosition());
            updateBoard(move, gameBoard);
            if(!isInCheck(color)){
                validMoves.add(move);
                ChessMove undoMove = new ChessMove(move.getEndPosition(),move.getStartPosition(), move.getPromotionPiece());
                updateBoard(undoMove, gameBoard);
                gameBoard.addPiece(move.getEndPosition(), potentialPiece);
                // I see what is happening. My undo move doesn't take into account
                // the times when a piece of another team is offed. it just
                //leaves that space null. Thus making
                // need to find a way to reset the board to the initial
                //before the move
            }
            else{
                ChessMove undoMove = new ChessMove(move.getEndPosition(),move.getStartPosition(), move.getPromotionPiece());
                updateBoard(undoMove, gameBoard);
            }
        }
        return validMoves;
    }

}
