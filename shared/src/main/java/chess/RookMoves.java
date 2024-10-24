package chess;

import java.util.ArrayList;

public class RookMoves implements PieceMoveCalculator{
  private ChessGame.TeamColor color;

  RookMoves(){}

  ArrayList<ChessMove> getAllMoves(ChessBoard board, ChessPosition originalPosition){
    color = board.getPiece(originalPosition).getTeamColor();
    int[][] increments = {
            {1,0},
            {-1, 0},
            {0, -1},
            {0,1},
    };

    FindMovesForBQR moves = new FindMovesForBQR();
    return moves.getListOfMoves(board, originalPosition, increments, color);

  }
}