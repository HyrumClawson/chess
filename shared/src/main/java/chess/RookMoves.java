package chess;

import java.util.ArrayList;

public class RookMoves implements PieceMoveCalculator{
  private ArrayList<ChessMove> ListOfMoves = new ArrayList<>();

  private ChessGame.TeamColor color;

  private ChessPosition originalPosition;

  private ChessPiece.PieceType rook;

  private String Direction;

  public RookMoves() {}


  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition position){
    rook = board.getPiece(position).getPieceType();
    color = board.getPiece(position).getTeamColor();
    originalPosition = position;
    Up(board, position);
    Left(board, position);
    Right(board, position);
    Down(board, position);


    return ListOfMoves;
  }

  ChessPosition Up (ChessBoard board, ChessPosition startPosition){
    Direction = "Up";
    int newRow;
    int newCol;
    if(startPosition.getRow() < 8){
      newRow = startPosition.getRow() + 1;
      newCol = startPosition.getColumn();
      return RepeatedCode(board, startPosition, newRow, newCol, Direction);
    }
    else{
      return startPosition;
    }
  }

  ChessPosition Down (ChessBoard board, ChessPosition startPosition){
    Direction = "Down";
    int newRow;
    int newCol;
    if(startPosition.getRow() > 1){
      newRow = startPosition.getRow() - 1;
      newCol = startPosition.getColumn();
      return RepeatedCode(board, startPosition, newRow, newCol, Direction);
    }
    else{
      return startPosition;
    }
  }

  ChessPosition Left (ChessBoard board, ChessPosition startPosition){
    String Direction = "Left";
    int newRow;
    int newCol;
    if(startPosition.getColumn() > 1){
      newRow = startPosition.getRow();
      newCol = startPosition.getColumn() - 1;
      return RepeatedCode(board, startPosition, newRow, newCol, Direction);
    }
    else{
      return startPosition;
    }
    //return startPosition;
  }



  ChessPosition Right (ChessBoard board, ChessPosition startPosition){
    Direction = "Right";
    int newRow;
    int newCol;
    if(startPosition.getColumn() < 8){
      newRow = startPosition.getRow();
      newCol = startPosition.getColumn() + 1;
      return RepeatedCode(board, startPosition, newRow, newCol, Direction);
    }
    else{
      return startPosition;
    }
  }

  ChessPosition RepeatedCode(ChessBoard board, ChessPosition startPosition, int row, int col, String Direction){
    ChessPosition newMovePosition = new ChessPosition(row,col);
    ChessMove newChessMove = new ChessMove(originalPosition, newMovePosition, null);
    if(board.getPiece(newMovePosition) != null){
      if(board.getPiece(newMovePosition).getTeamColor() == color){
        return startPosition;
      }
      else{
        ListOfMoves.add(newChessMove);
        return startPosition;
      }
    }
    else{
      ListOfMoves.add(newChessMove);
      switch (Direction){
        case "Up":
          return Up(board, newMovePosition);
        case "Down":
          return  Down(board, newMovePosition);
        case "Left":
          return Left(board, newMovePosition);
        case "Right":
          return Right(board, newMovePosition);
      }
    }
    return startPosition;
  }


}
