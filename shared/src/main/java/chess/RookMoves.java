package chess;

import java.util.ArrayList;

public class RookMoves implements PieceMoveCalculator{
  private ArrayList<ChessMove> listOfMoves = new ArrayList<>();
  private ChessGame.TeamColor color;

  RookMoves(){}

  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition originalPosition){
    color = board.getPiece(originalPosition).getTeamColor();
    int[][] increments = {
            {1,0},
            {-1, 0},
            {0, -1},
            {0,1},
    };

    for(int[] increment : increments){
      FindMoves(board, originalPosition, increment);
    }

    return listOfMoves;
  }
  void FindMoves(ChessBoard board, ChessPosition originalPosition, int[] increment){
    boolean goodToMove = true;
    int row;
    int col;
    row = originalPosition.getRow() + increment[0];
    col = originalPosition.getColumn() + increment[1];
    while(goodToMove){
      ChessPosition newPosition = new ChessPosition(row, col);
      ChessMove newMove = new ChessMove(originalPosition, newPosition, null);
      row = newPosition.getRow() + increment[0];
      col =newPosition.getColumn() + increment[1];
      if(newPosition.getRow() > 0 && newPosition.getRow() < 9 && newPosition.getColumn() > 0 &&
              newPosition.getColumn() < 9){
        if(board.getPiece(newPosition) != null){
          if(color != board.getPiece(newPosition).getTeamColor()){
            listOfMoves.add(newMove);
            goodToMove = false;
          }
          else{
            goodToMove = false;
          }
        }
        else{
          listOfMoves.add(newMove);
        }
      }
      else{
        goodToMove = false;
      }
    }
  }
}