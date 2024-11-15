package ui;


import static ui.EscapeSequences.*;

public class DrawChessBoard {
  String[][] chessBoard = new String[10][10];
  Boolean whiteOnTop;

  public DrawChessBoard() {}
    //i will be the rows
    //j will be the columns
    public void initializeStuff() {
      for (int i=0; i < 10; i++) {
        for (int j=0; j < 10; j++) {
          if (i == 0 || i == 9) {
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
          } else if (i == 1 && whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 8 ";
            } else{
              whiteRowSetUp(i, j);
            }
          } else if (i == 1 && !whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 8 ";
            }else{
              blackRowSetUp(i , j);
            }
          } else if (i == 2 && whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 7 ";
            } else {
              chessBoard[i][j]=WHITE_PAWN;
            }
          } else if (i == 2 && !whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 7 ";
            } else {
              chessBoard[i][j]=BLACK_PAWN;
            }
          } else if (i == 3) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 6 ";
            } else{
              chessBoard[i][j] = " ";
            }
          } else if (i == 4) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 5 ";
            } else{
              chessBoard[i][j] = " ";
            }
          } else if (i == 5) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 4 ";
            } else{
              chessBoard[i][j] = " ";
            }
          } else if (i == 6) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 3 ";
            } else{
              chessBoard[i][j] = " ";
            }
          } else if (i == 7 && whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 2 ";
            } else {
              chessBoard[i][j]=BLACK_PAWN;
            }
          } else if (i == 7 && !whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 2 ";
            } else {
              chessBoard[i][j]=WHITE_PAWN;
            }
          } else if (i == 8 && whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 1 ";
            } else{
              blackRowSetUp(i,j);
            }
          } else if (i==8 && !whiteOnTop){
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 1 ";
            } else{
              whiteRowSetUp(i, j);
            }
          } else{
            chessBoard[i][j] = " ";
          }
        }
      }
    }
    

  public void whiteRowSetUp(int i , int j){
    if (j == 1 || j == 8) {
      chessBoard[i][j]=WHITE_ROOK;
    } else if (j == 2 || j == 7) {
      chessBoard[i][j]=WHITE_KNIGHT;
    } else if (j == 3 || j == 6) {
      chessBoard[i][j]=WHITE_BISHOP;
    } else if (j == 4 && whiteOnTop) {
      chessBoard[i][j]=WHITE_KING;
    } else if(j == 4 && !whiteOnTop){
      chessBoard[i][j] = WHITE_QUEEN;
    } else {
      if(whiteOnTop){
        chessBoard[i][j]=WHITE_QUEEN;
      }
      else{
        chessBoard[i][j]=WHITE_KING;
      }
    }
  }

  public void blackRowSetUp(int i , int j){
    if (j == 1 || j == 8) {
      chessBoard[i][j]=BLACK_ROOK;
    } else if (j == 2 || j == 7) {
      chessBoard[i][j]=BLACK_KNIGHT;
    } else if (j == 3 || j == 6) {
      chessBoard[i][j]=BLACK_BISHOP;
    } else if (j == 4 && whiteOnTop) {
      chessBoard[i][j]=BLACK_KING;
    } else if(j == 4 && !whiteOnTop){
      chessBoard[i][j] = BLACK_QUEEN;
    } else {
      if(whiteOnTop){
        chessBoard[i][j]=BLACK_QUEEN;
      }
      else{
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
        String piece= chessBoard[i][j];
        if (piece.equals(" ")) {
          System.out.print("   ");
        } else {
          System.out.print(piece);
        }
        System.out.print(RESET_BG_COLOR);

      }
      System.out.print("\n");

    }
  }
}
