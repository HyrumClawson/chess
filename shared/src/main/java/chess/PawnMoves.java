package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMoves implements PieceMoveCalculator{
  private ArrayList<ChessMove> ListOfMoves = new ArrayList<>();
  private ChessGame.TeamColor color;
  private ChessPosition originalPosition;
  private ChessPiece.PieceType pawn;
  private int row;
  private int col;
  public PawnMoves() {

  }

  ArrayList<ChessMove> GetAllMoves(ChessBoard board, ChessPosition position){
    pawn = board.getPiece(position).getPieceType();
    color = board.getPiece(position).getTeamColor();
    originalPosition = position;
    Move(board, position);

    return ListOfMoves;
  }

  void Move(ChessBoard board, ChessPosition startingPosition){
    int[][] whiteMoveIncrement = {
            {1, -1},
            {1, 0},
            {1,1},
    };

    int[][] blackMoveIncrement = {
            {-1, -1},
            {-1, 0},
            {-1, 1},
    };
    if(board.getPiece(startingPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
      WhitePawnMoves(board, startingPosition, whiteMoveIncrement );
    }
    else{
      BlackPawnMoves(board, startingPosition, blackMoveIncrement);
    }


  }

  void WhitePawnMoves(ChessBoard board, ChessPosition position, int[][] increments){
    if(position.getRow() == 2){
      row = position.getRow() + 2;
      col =position.getColumn();
      ChessPosition newPosition = new ChessPosition(position.getRow()+1, position.getColumn());
      if(board.getPiece(newPosition) == null){
        TestCases(row, col, board);
      }

    }
    for(int[] increment : increments){
      row = position.getRow() + increment[0];
      col = position.getColumn() + increment[1];
      TestCases(row, col, board);

    }
  }

  void BlackPawnMoves(ChessBoard board, ChessPosition position, int[][] increments){
    if(position.getRow() == 7){
      row = position.getRow() - 2;
      col = position.getColumn();
      ChessPosition newPosition = new ChessPosition(position.getRow()-1, position.getColumn());
      if(board.getPiece(newPosition) == null){
        TestCases(row, col, board);
      }
    }
    for(int[] increment : increments){
      row = position.getRow() + increment[0];
      col = position.getColumn() + increment[1];
      TestCases(row, col, board);
    }

  }

  void TestCases(int row, int col, ChessBoard board){
    ChessPosition newMovePosition = new ChessPosition(row,col);
    ChessMove newChessMove = new ChessMove(originalPosition, newMovePosition, null);
    if(newMovePosition.getRow() > 0 && newMovePosition.getRow() < 9 && newMovePosition.getColumn() > 0
    && newMovePosition.getColumn() < 9) {

      if (newMovePosition.getColumn() == originalPosition.getColumn()) {
        if (board.getPiece(newChessMove.getEndPosition()) == null) {
          //ListOfMoves.add(newChessMove);
          if(newMovePosition.getRow() == 8 || newMovePosition.getRow() == 1){
            MakePromotionMoves(newMovePosition);
          }
          else{
            ListOfMoves.add(newChessMove);
          }
        }
      } else {
        if (board.getPiece(newChessMove.getEndPosition()) != null) {
          if (board.getPiece(newChessMove.getEndPosition()).getTeamColor() != color) {
            //ListOfMoves.add(newChessMove);
            if(newMovePosition.getRow() == 8 || newMovePosition.getRow() == 1){
              MakePromotionMoves(newMovePosition);
            }
            else{
              ListOfMoves.add(newChessMove);
            }
          }
        }
      }
    }
  }

  void MakePromotionMoves(ChessPosition newMovePosition){
    ChessPiece.PieceType [] promotionPieces = {
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,

    };

    for(ChessPiece.PieceType piece : promotionPieces){
      ChessMove newChessMove = new ChessMove(originalPosition, newMovePosition, piece);
      ListOfMoves.add(newChessMove);
    }

  }


}


