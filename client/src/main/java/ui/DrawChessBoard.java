package ui;


import chess.*;
import model.JoinGame;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
  String[][] chessBoard = new String[10][10];
  Boolean whiteOnTop;
  ChessBoard board;
  ChessPiece[][] squares;

  ChessPosition checkMoves = null;
  ChessGame game;
  //pass board.

  public DrawChessBoard(ChessGame game) {
    this.game = game;
    this.board = game.getBoard();
    squares = game.getBoard().getSquares();//board.getSquares();

  }

    //i will be the rows
    //j will be the columns
  //might need to go back and add the blank spaces
    public void initializeStuff() {
      if(whiteOnTop){
        squares = switchPerspective(squares);
      }
      for (int i=0; i < 10; i++) {
        for (int j=0; j < 10; j++) {
          if(null == squares[9-i][j]){
            chessBoard[i][j] = " ";
            }
          else{
            whiteRowSetUp(i, j);
            blackRowSetUp(i, j);
          }
          if (i == 0 || i == 9) {
            headerAndFooter(i, j);
          }
          setSideNumber(i, j);
        }
      }
    }


    public void setSideNumber(Integer i, Integer j) {
      if(whiteOnTop) {
        if (i == 1) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 1 ";
          }
        } else if (i == 2) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 2 ";
          }
        } else if (i == 3) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 3 ";
          }
        } else if (i == 4) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 4 ";
          }
        } else if (i == 5) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 5 ";
          }
        } else if (i == 6) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 6 ";
          }
        } else if (i == 7) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 7 ";
          } //place like here may be a good place to add blank spaces.
        } else if (i == 8) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 8 ";
          }
        }
      }
      else {
        if (i == 1) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 8 ";
          }
        } else if (i == 2) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 7 ";
          }
        } else if (i == 3) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 6 ";
          }
        } else if (i == 4) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 5 ";
          }
        } else if (i == 5) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 4 ";
          }
        } else if (i == 6) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 3 ";
          }
        } else if (i == 7) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 2 ";
          } //place like here may be a good place to add blank spaces.
        } else if (i == 8) {
          if (j == 0 || j == 9) {
            chessBoard[i][j]=" 1 ";
          }
        }
      }
    }




    public ChessPiece[][] switchPerspective(ChessPiece[][] squares){
      ChessPiece[][] startBoard =board.makeStartBoard();
      for(int i = 0; i < 10; i++){
        for(int j = 0; j < 10; j++){
          startBoard[i][j] = squares[9-i][9-j];
        }
      }
      return startBoard;
    }

  public void headerAndFooter(int i, int j){
    if(!whiteOnTop){
      if (j == 1) {
        chessBoard[i][j]=" a ";
      } else if (j == 2) {
        chessBoard[i][j]=" b ";
      } else if (j == 3) {
        chessBoard[i][j]=" c ";
      } else if (j == 4) {
        chessBoard[i][j]=" d ";
      } else if (j == 5) {
        chessBoard[i][j]=" e ";
      } else if (j == 6) {
        chessBoard[i][j]=" f ";
      } else if (j == 7) {
        chessBoard[i][j]=" g ";
      } else if (j == 8) {
        chessBoard[i][j]=" h ";
      }else{
        chessBoard[i][j] = " ";
      }
    }
    else{
      if (j == 1) {
        chessBoard[i][j]=" h ";
      } else if (j == 2) {
        chessBoard[i][j]=" g ";
      } else if (j == 3) {
        chessBoard[i][j]=" f ";
      } else if (j == 4) {
        chessBoard[i][j]=" e ";
      } else if (j == 5) {
        chessBoard[i][j]=" d ";
      } else if (j == 6) {
        chessBoard[i][j]=" c ";
      } else if (j == 7) {
        chessBoard[i][j]=" b ";
      } else if (j == 8) {
        chessBoard[i][j]=" a ";
      }else{
        chessBoard[i][j] = " ";
      }
    }

  }

  public void whiteRowSetUp(int i , int j){
    if(squares[9-i][j].getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
      if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.PAWN) {
        chessBoard[i][j]=WHITE_PAWN;
      } else if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.ROOK) {
        chessBoard[i][j]=WHITE_ROOK;
      } else if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.KNIGHT) {
        chessBoard[i][j]=WHITE_KNIGHT;
      } else if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.BISHOP) {
        chessBoard[i][j]=WHITE_BISHOP;
      } else if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.QUEEN) {
        chessBoard[i][j]=WHITE_QUEEN;
      } else if(squares[9-i][j].getPieceType() == ChessPiece.PieceType.KING){
        chessBoard[i][j]=WHITE_KING;
      }
    }
  }

  public void blackRowSetUp(int i , int j){
    if(squares[9-i][j].getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
      if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.PAWN) {
        chessBoard[i][j]=BLACK_PAWN;
      } else if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.ROOK) {
        chessBoard[i][j]=BLACK_ROOK;
      } else if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.KNIGHT) {
        chessBoard[i][j]=BLACK_KNIGHT;
      } else if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.BISHOP) {
        chessBoard[i][j]=BLACK_BISHOP;
      } else if (squares[9-i][j].getPieceType() == ChessPiece.PieceType.QUEEN) {
        chessBoard[i][j]=BLACK_QUEEN;
      } else if(squares[9-i][j].getPieceType() == ChessPiece.PieceType.KING){
        chessBoard[i][j]=BLACK_KING;
      }
    }
  }



  public void printChessBoard(String colorOnTop) {
    if(colorOnTop.equals("white")){
      whiteOnTop = true;
    }
    else{
      whiteOnTop = false;
    }
    initializeStuff();

    for( int i = 0; i < 10; i++){
      for (int j=0; j < 10; j++) {
        if(i == 0 || i == 9){
          System.out.print(SET_BG_COLOR_WHITE);
        }
        else if(j == 0 || j == 9){
          System.out.print(SET_BG_COLOR_WHITE);
        }

        else if ((i + j) % 2 == 0) {
          System.out.print("\u001B[48;5;230m"); // Tan
        }
        else {
          System.out.print("\u001B[48;5;94m"); // Brown
        }
        if(checkMoves != null){
          Collection<ChessMove> highlightMoves = game.validMoves(checkMoves);
          getProperHighLight(highlightMoves, i, j);
        }


        String piece= chessBoard[i][j];
        if(chessBoard[i][j] == null){
          System.out.print("   ");
        }
        else if (piece.equals(" ")) {
          System.out.print("   ");
        }
        else {
          System.out.print(piece);
        }
        System.out.print(RESET_BG_COLOR);

      }
      System.out.print("\n");

    }
  }

  public void setPositionToLookAtMoves(ChessPosition position){
    checkMoves = position;
  }

  public void getProperHighLight(Collection<ChessMove> potentialMoves, int i, int j){
    for(ChessMove move : potentialMoves){
      if(whiteOnTop){
        if(i == move.getEndPosition().getRow() && j == (9 - move.getEndPosition().getColumn())) {
          System.out.print(SET_BG_COLOR_DARK_GREY);
        }
      }
      else{
        if(i == (9-move.getEndPosition().getRow()) && j == move.getEndPosition().getColumn()){
          System.out.print(SET_BG_COLOR_DARK_GREY);
        }
      }
    }
  }


}
