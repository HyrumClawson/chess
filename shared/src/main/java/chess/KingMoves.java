package chess;

import java.util.ArrayList;

public class KingMoves implements PieceMoveCalculator{
  private ArrayList<ChessMove> ListOfMoves = new ArrayList<>();

  private ChessGame.TeamColor color;

  private ChessPosition originalPosition;

  private ChessPiece.PieceType king;

  private String Direction;

  public KingMoves(){}


  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition position){
    king = board.getPiece(position).getPieceType();
    color = board.getPiece(position).getTeamColor();
    originalPosition = position;
    Up(board, position);
    Left(board, position);
    Right(board, position);
    Down(board, position);
    UL(board, position);
    UR(board, position);
    DL(board, position);
    DR(board, position);

    return ListOfMoves;
  }
  ChessPosition UL (ChessBoard board, ChessPosition startPosition){
    Direction = "UL";
    int newRow;
    int newCol;
    if(startPosition.getRow() < 8 && startPosition.getColumn() > 1){
      newRow = startPosition.getRow() + 1;
      newCol = startPosition.getColumn() - 1;
      return RepeatedCode(board, startPosition, newRow, newCol, Direction);
    }
    else{
      return startPosition;
    }
  }

  ChessPosition UR (ChessBoard board, ChessPosition startPosition){
    Direction = "UR";
    int newRow;
    int newCol;
    if(startPosition.getRow() < 8 && startPosition.getColumn() < 8){
      newRow = startPosition.getRow() + 1;
      newCol = startPosition.getColumn() + 1;
      return RepeatedCode(board, startPosition, newRow, newCol, Direction);
    }
    else{
      return startPosition;
    }
  }

  ChessPosition DL (ChessBoard board, ChessPosition startPosition){
    Direction = "DL";
    int newRow;
    int newCol;
    if(startPosition.getRow() > 1 && startPosition.getColumn() > 1){
      newRow = startPosition.getRow() - 1;
      newCol = startPosition.getColumn() - 1;
      return RepeatedCode(board, startPosition, newRow, newCol, Direction);
    }
    else{
      return startPosition;
    }
  }

  ChessPosition DR (ChessBoard board, ChessPosition startPosition){
    Direction = "DR";
    int newRow;
    int newCol;
    if(startPosition.getRow() > 1 && startPosition.getColumn() < 8){
      newRow = startPosition.getRow() - 1;
      newCol = startPosition.getColumn() + 1;
      return RepeatedCode(board, startPosition, newRow, newCol, Direction);
    }
    else{
      return startPosition;
    }
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
      /**return RepeatedCode(board,
       *
       */
//      ChessPosition newMovePosition = new ChessPosition(newRow,newCol);
//      ChessMove newChessMove = new ChessMove(originalPosition, newMovePosition, null);
//      if(board.getPiece(newMovePosition) != null){
//        if(board.getPiece(newMovePosition).getTeamColor() == color){
//          return startPosition;
//        }
//        else{
//          ListOfMoves.add(newChessMove);
//          return startPosition;
//        }
//      }
//      else{
//        ListOfMoves.add(newChessMove);
//        DR(board, newMovePosition);
//      }

      /**until the statment above is the repeated code we can take out and put in the function below
       *
       */
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
      return startPosition;
    }
  }


}
