package chess;

import java.util.ArrayList;

public class FindMovesForKingAndKnight {
  ArrayList<ChessMove> listOfMoves = new ArrayList<>();

  ArrayList<ChessMove> getListOfMoves(ChessBoard board, ChessPosition originalPosition,
                                      int[][] increments, ChessGame.TeamColor color){
    for(int[] increment : increments){
      findMoves(board, originalPosition, increment, color);
    }
    return listOfMoves;
  }
  void findMoves(ChessBoard board, ChessPosition originalPosition,
                 int[] increment, ChessGame.TeamColor color){
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
