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
        }
        else {
            //I have a lot to add to this section
            //ChessBoard dummyBoard = gameBoard;
            ChessGame.TeamColor color = gameBoard.getPiece(startPosition).pieceColor;
            ArrayList<ChessMove> validMoves = new ArrayList<>();
            Collection<ChessMove> potentialMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);

            if(isInCheck(color)){
                //might need to change it to getTEamColor above
                for(ChessMove move : potentialMoves){
                    ChessPiece potentialPiece = gameBoard.getPiece(move.getEndPosition());
                    UpdateBoard(move, gameBoard);
                    if(!isInCheck(color)){
                        validMoves.add(move);
                        ChessMove undoMove = new ChessMove(move.getEndPosition(),move.getStartPosition(), move.getPromotionPiece());
                        UpdateBoard(undoMove, gameBoard);
                        gameBoard.addPiece(move.getEndPosition(), potentialPiece);
                        // I see what is happening. My undo move doesn't take into account
                        // the times when a piece of another team is offed. Therefore it just
                        //leaves that space null. Thus making


                        // need to find a way to reset the board to the initial
                        //before the move
                    }
                    else{
                        ChessMove undoMove = new ChessMove(move.getEndPosition(),move.getStartPosition(), move.getPromotionPiece());
                        UpdateBoard(undoMove, gameBoard);
                        //need to find a way to reset the board here as well
                    }
                }
            }
            else{
                for(ChessMove move : potentialMoves){
                    ChessPiece potentialPiece = gameBoard.getPiece(move.getEndPosition());
                    UpdateBoard(move, gameBoard);
                    if(!isInCheck(color)){
                        validMoves.add(move);
                        ChessMove undoMove = new ChessMove(move.getEndPosition(),move.getStartPosition(), move.getPromotionPiece());
                        UpdateBoard(undoMove, gameBoard);
                        gameBoard.addPiece(move.getEndPosition(), potentialPiece);
                    }
                    else{
                        ChessMove undoMove = new ChessMove(move.getEndPosition(),move.getStartPosition(), move.getPromotionPiece());
                        UpdateBoard(undoMove, gameBoard);
                    }
                }

            }

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
            throw new InvalidMoveException();
        }
        else {
            if(teamTurn.peek() != gameBoard.getPiece(move.getStartPosition()).pieceColor){
                throw new InvalidMoveException();
            }
            boolean isValid=CheckMoveValidity(move);
            if (isValid) {
                //function that changes the board.
                UpdateBoard(move, gameBoard);
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
        ArrayList<ChessMove> opposingTeamMoves;
        opposingTeamMoves = GetOpposingTeamMoves(teamColor, gameBoard);
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
        TeamColor opposingColor;
        if(TeamColor.WHITE == teamColor){
            opposingColor = TeamColor.BLACK;
        }
        else{
            opposingColor = TeamColor.WHITE;
        }
        ArrayList<ChessMove> opposingTeamMoves = new ArrayList<>();
        opposingTeamMoves = GetOpposingTeamMoves(teamColor, gameBoard);
        if(isInCheck(teamColor)){
            ChessPosition kingPosition = gameBoard.getPosition(teamColor, ChessPiece.PieceType.KING).getFirst();
            Collection<ChessMove> kingTeamMoves = GetOpposingTeamMoves(opposingColor, gameBoard);
            ArrayList<ChessMove> kingTeam = new ArrayList<>();
            kingTeam.addAll(kingTeamMoves);
            boolean isInCheckEverywhere = false;
            for (ChessMove teamMove : kingTeam){
                ChessPiece potentialPiece = gameBoard.getPiece(teamMove.getEndPosition());
                UpdateBoard(teamMove, gameBoard);
                if(!isInCheck(teamColor)){
                    return false;
                }
                else{
                    isInCheckEverywhere = true;
                }
                ChessMove undoMove = new ChessMove(teamMove.getEndPosition(),teamMove.getStartPosition(), teamMove.getPromotionPiece());
                UpdateBoard(undoMove, gameBoard);
                gameBoard.addPiece(teamMove.getEndPosition(), potentialPiece);
            }
            return isInCheckEverywhere;
           // return !CheckMovesAgainstMoves( opposingTeamMoves,  kings);
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
        ArrayList<ChessMove> opposingTeamMoves = GetOpposingTeamMoves(teamColor, gameBoard);
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
        ArrayList<ChessPosition> kingPosition =gameBoard.getPosition
                (gameBoard.getPiece(move.getStartPosition()).pieceColor, ChessPiece.PieceType.KING);
        if(!kingPosition.isEmpty() &&
                isInCheck(gameBoard.getPiece(move.getStartPosition()).pieceColor)){
            ChessBoard dummyBoard = gameBoard;
            UpdateBoard(move, dummyBoard);
            if(isInCheck(dummyBoard.getPiece(move.getEndPosition()).pieceColor)){
                isValid = false;
                return isValid;
            }
        }
        ChessPiece thePiece=gameBoard.getPiece(move.getStartPosition());
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

    public void UpdateBoard(ChessMove move, ChessBoard board){
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

    ArrayList<ChessMove> GetOpposingTeamMoves(ChessGame.TeamColor teamColor, ChessBoard board) {
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
            if(!opposingTeamMoves.contains(king)){
                return true;
            }
            else{
                canMove = false;
            }
        }

        // essentially got to add the cases where the king captures a piece, but
        // other teams pieces are there to capture the king. need my good old undo
        // gameboard bit.


        return canMove;
    }


    public boolean isInCheckTest(TeamColor teamColor, ChessBoard board) {
        ArrayList<ChessMove> opposingTeamMoves;
        //function to get opposing team moves
        opposingTeamMoves = GetOpposingTeamMoves(teamColor, board);
        ChessPosition kingPosition = board.getPosition(teamColor, ChessPiece.PieceType.KING).getFirst();
        return IsKingInCheck(opposingTeamMoves, kingPosition);
        //throw new RuntimeException("Not implemented");
    }

}
