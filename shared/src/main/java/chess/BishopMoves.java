package chess;

import java.util.ArrayList;

public class BishopMoves implements PieceMoveCalculator{
  private ArrayList<ChessMove> ListOfMoves = new ArrayList<>();
  private ChessPiece.PieceType bishop;

  private ChessGame.TeamColor color;

  private ChessPosition originalPosition;

  /**
   * Need to figure out how exactly to add the positions into the correct form of array list
   */

  public BishopMoves(){

  }

  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition position){
    bishop = board.getPiece(position).getPieceType();
    color = board.getPiece(position).getTeamColor();
    originalPosition = position;

    UL(board, position);
    UR(board, position);
    DL(board, position);
    DR(board, position);

    return ListOfMoves;
  }
  ChessPosition UL (ChessBoard board, ChessPosition startPosition){
    int newRow;
    int newCol;
    if(startPosition.getRow() < 8 && startPosition.getColumn() > 1){
      newRow = startPosition.getRow() + 1;
      newCol = startPosition.getColumn() - 1;
      ChessPosition newMovePosition = new ChessPosition(newRow,newCol);
      /**
       * might need to make the start position just the original position
       * rn start position is 5,4
       * but the 2d array it's pulling from has index 0-7
       */
      ChessMove newChessMove = new ChessMove(originalPosition, newMovePosition, null);
      if(board.getPiece(newMovePosition) != null){
        if(board.getPiece(newMovePosition).getTeamColor() == color){
          return startPosition;
        }
        else{
          //ChessMove newChessMove = new ChessMove(startPosition, newMovePosition, board.getPiece(startPosition).getPieceType());
          ListOfMoves.add(newChessMove);
          return startPosition;
        }
      }
      else{
        ListOfMoves.add(newChessMove);
        UL(board, newMovePosition);
      }
    }
    else{
      return startPosition;
    }
    return startPosition;
  }

  ChessPosition UR (ChessBoard board, ChessPosition startPosition){
    int newRow;
    int newCol;
    if(startPosition.getRow() < 8 && startPosition.getColumn() < 8){
      newRow = startPosition.getRow() + 1;
      newCol = startPosition.getColumn() + 1;
      ChessPosition newMovePosition = new ChessPosition(newRow,newCol);
      /**
       * might need to make the start position just the original position
       * rn start position is 5,4
       * but the 2d array it's pulling from has index 0-7
       */
      ChessMove newChessMove = new ChessMove(originalPosition, newMovePosition, null);
      if(board.getPiece(newMovePosition) != null){
        if(board.getPiece(newMovePosition).getTeamColor() == color){
          return startPosition;
        }
        else{
          //ChessMove newChessMove = new ChessMove(startPosition, newMovePosition, board.getPiece(startPosition).getPieceType());
          ListOfMoves.add(newChessMove);
          return startPosition;
        }
      }
      else{
        ListOfMoves.add(newChessMove);
        UR(board, newMovePosition);
      }
    }
    else{
      return startPosition;
    }
    return startPosition;

  }

  ChessPosition DL (ChessBoard board, ChessPosition startPosition){
    int newRow;
    int newCol;
    if(startPosition.getRow() > 1 && startPosition.getColumn() > 1){
      newRow = startPosition.getRow() - 1;
      newCol = startPosition.getColumn() - 1;
      ChessPosition newMovePosition = new ChessPosition(newRow,newCol);
      /**
       * might need to make the start position just the original position
       * rn start position is 5,4
       * but the 2d array it's pulling from has index 0-7
       */
      ChessMove newChessMove = new ChessMove(originalPosition, newMovePosition, null);
      if(board.getPiece(newMovePosition) != null){
        if(board.getPiece(newMovePosition).getTeamColor() == color){
          return startPosition;
        }
        else{
          //ChessMove newChessMove = new ChessMove(startPosition, newMovePosition, board.getPiece(startPosition).getPieceType());
          ListOfMoves.add(newChessMove);
          return startPosition;
        }
      }
      else{
        ListOfMoves.add(newChessMove);
        DL(board, newMovePosition);
      }
    }
    else{
      return startPosition;
    }
    return startPosition;


  }
  ChessPosition DR (ChessBoard board, ChessPosition startPosition){
    int newRow;
    int newCol;
    if(startPosition.getRow() > 1 && startPosition.getColumn() < 8){
      newRow = startPosition.getRow() - 1;
      newCol = startPosition.getColumn() + 1;
      ChessPosition newMovePosition = new ChessPosition(newRow,newCol);
      /**
       * might need to make the start position just the original position
       * rn start position is 5,4
       * but the 2d array it's pulling from has index 0-7
       */
      ChessMove newChessMove = new ChessMove(originalPosition, newMovePosition, null);
      if(board.getPiece(newMovePosition) != null){
        if(board.getPiece(newMovePosition).getTeamColor() == color){
          return startPosition;
        }
        else{
          //ChessMove newChessMove = new ChessMove(startPosition, newMovePosition, board.getPiece(startPosition).getPieceType());
          ListOfMoves.add(newChessMove);
          return startPosition;
        }
      }
      else{
        ListOfMoves.add(newChessMove);
        DR(board, newMovePosition);
      }
    }
    else{
      return startPosition;
    }
    return startPosition;


  }
}


