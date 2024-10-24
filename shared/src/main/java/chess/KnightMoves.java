package chess;

import java.util.ArrayList;

public class KnightMoves implements PieceMoveCalculator{
  private ChessGame.TeamColor color;

  KnightMoves(){}

  ArrayList<ChessMove> getAllMoves(ChessBoard board, ChessPosition originalPosition){
    color = board.getPiece(originalPosition).getTeamColor();
    int[][] increments = {
            {2,1},
            {2, -1},
            {-2, 1},
            {-2,-1},
            {1, -2},
            {-1, -2},
            {-1, 2},
            {1, 2},
    };
    FindMovesForKingAndKnight moves=new FindMovesForKingAndKnight();
    return moves.getListOfMoves(board, originalPosition, increments, color);

  }
}
