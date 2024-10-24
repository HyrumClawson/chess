package chess;

import java.util.ArrayList;

public class KnightMoves implements PieceMoveCalculator{
  private ArrayList<ChessMove> listOfMoves = new ArrayList<>();
  private ChessGame.TeamColor color;

  KnightMoves(){}

  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition originalPosition){
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

    for(int[] increment : increments){
      FindMoves(board, originalPosition, increment);
    }

    return listOfMoves;
  }
  void FindMoves(ChessBoard board, ChessPosition originalPosition, int[] increment){
    int row;
    int col;
    row = originalPosition.getRow() + increment[0];
    col = originalPosition.getColumn() + increment[1];
    ChessPosition newPosition = new ChessPosition(row, col);
    ChessMove newMove = new ChessMove(originalPosition, newPosition, null);
    if(newPosition.getRow() > 0 && newPosition.getRow() < 9 && newPosition.getColumn() > 0 &&
            newPosition.getColumn() < 9){
      if(board.getPiece(newPosition) != null){
        if(color != board.getPiece(newPosition).getTeamColor()){
          listOfMoves.add(newMove);
        }
      }
      else{
        listOfMoves.add(newMove);
      }
    }
  }
}
