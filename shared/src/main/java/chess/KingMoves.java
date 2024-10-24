package chess;

import java.util.ArrayList;

public class KingMoves implements PieceMoveCalculator{
  private ArrayList<ChessMove> listOfMoves = new ArrayList<>();
  private ChessGame.TeamColor color;

  KingMoves(){}

  ArrayList<ChessMove> getAllMoves(ChessBoard board, ChessPosition originalPosition){
    color = board.getPiece(originalPosition).getTeamColor();
    int[][] increments = {
            {1,0},
            {-1, 0},
            {0, -1},
            {0,1},
            {1, -1},
            {1, 1},
            {-1, -1},
            {-1, 1},
    };

    for(int[] increment : increments){
      findMoves(board, originalPosition, increment);
    }

    return listOfMoves;
  }
  void findMoves(ChessBoard board, ChessPosition originalPosition, int[] increment){
    boolean goodToMove = true;
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
