package chess;

import java.util.ArrayList;

public class KingMoves implements PieceMoveCalculator {

  private ChessGame.TeamColor color;

  KingMoves() {
  }

  ArrayList<ChessMove> getAllMoves(ChessBoard board, ChessPosition originalPosition) {
    color=board.getPiece(originalPosition).getTeamColor();
    int[][] increments={
            {1, 0},
            {-1, 0},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 1},
            {-1, -1},
            {-1, 1},
    };

    FindMovesForKingAndKnight moves=new FindMovesForKingAndKnight();
    return moves.getListOfMoves(board, originalPosition, increments, color);
  }
}