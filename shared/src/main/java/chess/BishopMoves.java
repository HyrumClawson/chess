package chess;

import java.util.ArrayList;

public class BishopMoves implements PieceMoveCalculator{
  private ChessGame.TeamColor color;

  BishopMoves(){}

  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition originalPosition){
    color = board.getPiece(originalPosition).getTeamColor();
    int[][] increments = {
            {1, -1},
            {1, 1},
            {-1, -1},
            {-1, 1},
    };

    FindMovesForBQR moves = new FindMovesForBQR();
    return moves.getListOfMoves(board, originalPosition, increments, color);
  }

}