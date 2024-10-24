package chess;

import java.util.ArrayList;

public class FindMovesForBQR {
  ArrayList<ChessMove> listOfMoves = new ArrayList<>();

  ArrayList<ChessMove> getListOfMoves(ChessBoard board, ChessPosition originalPosition,
                                      int[][] increments, ChessGame.TeamColor color){
    for(int[] increment : increments){
      findMoves(board, originalPosition, increment, color);
    }
    return listOfMoves;

  }
  public void findMoves(ChessBoard board, ChessPosition originalPosition, int[] increment,
                        ChessGame.TeamColor color){
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
