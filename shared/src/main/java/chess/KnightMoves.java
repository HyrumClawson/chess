package chess;

import java.util.ArrayList;

public class KnightMoves implements PieceMoveCalculator{

  private ArrayList<ChessMove> ListOfMoves = new ArrayList<>();

  private ChessGame.TeamColor color;

  private ChessPosition originalPosition;

  private ChessPiece.PieceType Knight;

  private String Direction;


  int newRow;
  int newCol;

  public KnightMoves() {}




  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition position){
    Knight = board.getPiece(position).getPieceType();
    color = board.getPiece(position).getTeamColor();
    originalPosition = position;
    Up(board, position);
    Left(board, position);
    Right(board, position);
    Down(board, position);


    return ListOfMoves;
  }

  ChessPosition Up (ChessBoard board, ChessPosition startPosition) {
    ArrayList<ChessMove> listOfChessMoves=new ArrayList<>();
    newRow=startPosition.getRow() + 2;
    newCol=startPosition.getColumn() - 1;
    listOfChessMoves = createMoveAndAdd(newRow, newCol, listOfChessMoves);
    newRow=startPosition.getRow() + 2;
    newCol=startPosition.getColumn() + 1;
    listOfChessMoves = createMoveAndAdd(newRow, newCol, listOfChessMoves);
    for (ChessMove move : listOfChessMoves) {
      if (move.getEndPosition().getRow() < 9 && move.getEndPosition().getColumn() > 0 &&
              move.getEndPosition().getColumn() < 9) {
        RepeatedCode(board, startPosition, move);
      }
    }
    return startPosition;
  }

  ChessPosition Down (ChessBoard board, ChessPosition startPosition){
    ArrayList<ChessMove> listOfChessMoves=new ArrayList<>();
    newRow=startPosition.getRow() - 2;
    newCol=startPosition.getColumn() + 1;
    listOfChessMoves = createMoveAndAdd(newRow, newCol, listOfChessMoves);
    newRow=startPosition.getRow() - 2;
    newCol=startPosition.getColumn() - 1;
    listOfChessMoves = createMoveAndAdd(newRow, newCol, listOfChessMoves);
    for (ChessMove move : listOfChessMoves) {
      if (move.getEndPosition().getRow() > 0 && move.getEndPosition().getColumn() > 0 &&
              move.getEndPosition().getColumn() < 9) {
        RepeatedCode(board, startPosition, move);
      }
    }
    return startPosition;

  }

  ChessPosition Left (ChessBoard board, ChessPosition startPosition){
    ArrayList<ChessMove> listOfChessMoves=new ArrayList<>();
    newRow=startPosition.getRow() + 1;
    newCol=startPosition.getColumn() - 2;
    listOfChessMoves = createMoveAndAdd(newRow, newCol, listOfChessMoves);
    newRow=startPosition.getRow() - 1;
    newCol=startPosition.getColumn() - 2;
    listOfChessMoves = createMoveAndAdd(newRow, newCol, listOfChessMoves);
    for (ChessMove move : listOfChessMoves) {
      if (move.getEndPosition().getRow() < 9 && move.getEndPosition().getRow() > 0 &&
              move.getEndPosition().getColumn() > 0) {
        RepeatedCode(board, startPosition, move);
      }
    }
    return startPosition;
  }



  ChessPosition Right (ChessBoard board, ChessPosition startPosition){
    ArrayList<ChessMove> listOfChessMoves=new ArrayList<>();
    newRow=startPosition.getRow() + 1;
    newCol=startPosition.getColumn() + 2;
    listOfChessMoves = createMoveAndAdd(newRow, newCol, listOfChessMoves);
    newRow=startPosition.getRow() - 1;
    newCol=startPosition.getColumn() + 2;
    listOfChessMoves = createMoveAndAdd(newRow, newCol, listOfChessMoves);
    for (ChessMove move : listOfChessMoves) {
      if (move.getEndPosition().getRow() < 9 && move.getEndPosition().getRow() > 0 &&
              move.getEndPosition().getColumn() < 9) {
        RepeatedCode(board, startPosition, move);
      }
    }
    return startPosition;
  }

  ChessPosition RepeatedCode(ChessBoard board, ChessPosition startPosition, ChessMove move ){
    // have to see if end position's row and col are in range
    if(board.getPiece(move.getEndPosition()) != null){
      if(board.getPiece(move.getEndPosition()).getTeamColor() == color){
        return startPosition;
      }
      else{
        ListOfMoves.add(move);
        return startPosition;
      }
    }
    else{
      ListOfMoves.add(move);
      return startPosition;
      }
    }



    ArrayList<ChessMove> createMoveAndAdd(int row, int col, ArrayList<ChessMove> Moves){
      ChessPosition newMovePosition=new ChessPosition(row, col);
      ChessMove newChessMove=new ChessMove(originalPosition, newMovePosition, null);
      Moves.add(newChessMove);
      return Moves;
    }


  }





