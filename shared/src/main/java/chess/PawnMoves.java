package chess;

import java.util.ArrayList;

public class PawnMoves implements PieceMoveCalculator{
  private ArrayList<ChessMove> listOfMoves = new ArrayList<>();
  private ChessGame.TeamColor color;

  PawnMoves(){}

  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition originalPosition){
    color = board.getPiece(originalPosition).getTeamColor();
    int[][] whiteIncrements = {
            {1,0},
            {1,1},
            {1,-1},
    };
    int[][] blackIncrements = {
            {-1,0},
            {-1,1},
            {-1,-1},
    };
    if(color == ChessGame.TeamColor.WHITE){
      if(originalPosition.getRow() == 2){
        ChessPosition newPosition = new ChessPosition(originalPosition.getRow()+2, originalPosition.getColumn());
        ChessPosition check = new ChessPosition(originalPosition.getRow()+1, originalPosition.getColumn());
        if(board.getPiece(check) == null){
          if(board.getPiece(newPosition) == null){
            ChessMove newMove = new ChessMove(originalPosition, newPosition, null);
            listOfMoves.add(newMove);
          }
        }
      }
      //add special case code
      for(int[] increment : whiteIncrements){
        FindMoves(board, originalPosition, increment);
      }
    }
    else{
      if(originalPosition.getRow() == 7){
        ChessPosition newPosition = new ChessPosition(originalPosition.getRow()-2, originalPosition.getColumn());
        ChessPosition check = new ChessPosition(originalPosition.getRow()-1, originalPosition.getColumn());
        if(board.getPiece(check) == null){
          if(board.getPiece(newPosition) == null){
            ChessMove newMove = new ChessMove(originalPosition, newPosition, null);
            listOfMoves.add(newMove);
          }
        }
      }
      for(int[] increment : blackIncrements){
        FindMoves(board, originalPosition, increment);
      }

    }
    return listOfMoves;
  }

  void FindMoves(ChessBoard board, ChessPosition originalPosition, int[] increment){
    boolean goodToMove = true;
    int row;
    int col;
    row = originalPosition.getRow() + increment[0];
    col = originalPosition.getColumn() + increment[1];
    ChessPosition newPosition = new ChessPosition(row, col);
    ChessMove newMove = new ChessMove(originalPosition, newPosition, null);
//      row = newPosition.getRow() + increment[0];
//      col =newPosition.getColumn() + increment[1];
    if(newPosition.getRow() > 0 && newPosition.getRow() < 9 && newPosition.getColumn() > 0 &&
            newPosition.getColumn() < 9
      && (newPosition.getRow() != 1 || newPosition.getRow() != 8) ){
      if(board.getPiece(newPosition) != null ){
        if(color != board.getPiece(newPosition).getTeamColor()){
          if(newPosition.getColumn() != originalPosition.getColumn()){
            if(newPosition.getRow() == 1 || newPosition.getRow() == 8){
              GetPromotionMoves(originalPosition, newPosition);
            }
            else{
              listOfMoves.add(newMove);
            }

          }
          goodToMove = false;
        }
        else{
          goodToMove = false;
        }
      }
      else{
        if(originalPosition.getColumn() == newPosition.getColumn()){
          if(newPosition.getRow() == 1 || newPosition.getRow() == 8){
            GetPromotionMoves(originalPosition, newPosition);
          }
          else{
            listOfMoves.add(newMove);
          }

        }

      }
    }
    else{
      goodToMove = false;
    }

  }

  void GetPromotionMoves(ChessPosition originalPosition, ChessPosition newPosition){
    ChessMove Rook = new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.ROOK);
    ChessMove Knight = new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.KNIGHT);
    ChessMove Queen = new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.QUEEN);
    ChessMove Bishop = new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.BISHOP);
    ArrayList<ChessMove> promotionMoves = new ArrayList<>();

    promotionMoves.add(Rook);
    promotionMoves.add(Knight);
    promotionMoves.add(Queen);
    promotionMoves.add(Bishop);

    listOfMoves.addAll(promotionMoves);
  }

}
