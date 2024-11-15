package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
  String[][] chessBoard = new String[10][10];
  Boolean whiteOnTop;

  public DrawChessBoard() {
  }

    // Define a chessboard with pieces using Unicode characters
    //i will be the rows
    //j will be the columns
    public void initializeStuff(String colorOnTop) {
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
            }
          } else if (i == 1 && whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 8 ";
            } else if (j == 1 || j == 8) {
              chessBoard[i][j]=WHITE_ROOK;
            } else if (j == 2 || j == 7) {
              chessBoard[i][j]=WHITE_KNIGHT;
            } else if (j == 3 || j == 6) {
              chessBoard[i][j]=WHITE_BISHOP;
            } else if (j == 4) {
              chessBoard[i][j]=WHITE_KING;
            } else {
              chessBoard[i][j]=WHITE_QUEEN;
            }
          } else if (i == 1 && !whiteOnTop) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 8 ";
            } else if (j == 1 || j == 8) {
              chessBoard[i][j]=BLACK_ROOK;
            } else if (j == 2 || j == 7) {
              chessBoard[i][j]=BLACK_KNIGHT;
            } else if (j == 3 || j == 6) {
              chessBoard[i][j]=BLACK_BISHOP;
            } else if (j == 4) {
              chessBoard[i][j]=BLACK_QUEEN;
            } else {
              chessBoard[i][j]=BLACK_KING;
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
          } else if (i == 8) {
            if (j == 0 || j == 9) {
              chessBoard[i][j]=" 1 ";
            }
          }

        }
      }
    }







//    // Initialize the board with the starting positions of the pieces
//    for (int i = 0; i < 8; i++) {
//      for (int j = 0; j < 8; j++) {
//        if (i == 0) {
//          // Black pieces on the first row
//          if (j == 0 || j == 7) chessBoard[i][j] = "\u265C"; // Black rook
//          else if (j == 1 || j == 6) chessBoard[i][j] = "\u265E"; // Black knight
//          else if (j == 2 || j == 5) chessBoard[i][j] = "\u265D"; // Black bishop
//          else if (j == 3) chessBoard[i][j] = "\u265B"; // Black queen
//          else chessBoard[i][j] = "\u265A"; // Black king
//        } else if (i == 1) {
//          // Black pawns on the second row
//          chessBoard[i][j] = "\u265F"; // Black pawn
//        } else if (i == 6) {
//          // White pawns on the seventh row
//          chessBoard[i][j] = "\u2659"; // White pawn
//        } else if (i == 7) {
//          // White pieces on the eighth row
//          if (j == 0 || j == 7) chessBoard[i][j] = "\u2656"; // White rook
//          else if (j == 1 || j == 6) chessBoard[i][j] = "\u2658"; // White knight
//          else if (j == 2 || j == 5) chessBoard[i][j] = "\u2657"; // White bishop
//          else if (j == 3) chessBoard[i][j] = "\u2655"; // White queen
//          else chessBoard[i][j] = "\u2654"; // White king
//        } else {
//          // Empty squares
//          chessBoard[i][j] = " "; // Empty space
//        }
//      }
//    }

    // Print the chessboard
    //printChessBoard(chessBoard);
//  }

  // Method to print the chessboard to the console
  public void printChessBoard(String colorOnTop) {
    if(colorOnTop.equals("white")){
      whiteOnTop = true;
    }
    else{
      whiteOnTop = false;
    }
    // Print top border with column letters (a-h)
    System.out.print("\u001B[48;5;15m"); // White background for outline
    System.out.print("   a  b  c  d  e  f  g  h   ");
    System.out.println("\u001B[0m");


    // Print each row with the left-side row number
    for (int i=7; i >= 0; i--) {
      // Print row number on the left side, inside the white outline
      System.out.print("\u001B[48;5;15m"); // White background
      System.out.print(" " + (i + 1) + " "); // Row number
      System.out.print("\u001B[0m"); // Reset the background color

      // Print the chessboard row
      for (int j=0; j < 10; j++) {
        // Alternate the background color for chessboard visualization (tan and brown)
        if(i == 0 || i == 9){
          System.out.print(SET_BG_COLOR_WHITE);
        }
        else if ((i + j) % 2 == 0) {
          System.out.print("\u001B[48;5;230m"); // Tan background (light brown)
        }
        else if(j == 0 || j == 9){
          System.out.print(SET_BG_COLOR_WHITE);
        }
        else {
          System.out.print("\u001B[48;5;94m"); // Brown background (dark brown)
        }

        // Center the piece in the square by adding spaces
        String piece=chessBoard[i][j];
        if (piece.equals(" ")) {
          // Empty space, just add a single space
          System.out.print("   ");
        } else {
          // For pieces, add padding to center them
          System.out.print(" " + piece + " ");
        }

        // Reset the background color to default
        System.out.print("\u001B[0m");
      }

      // Print the row number on the right side, inside the white outline
      System.out.print("\u001B[48;5;15m"); // White background
      System.out.println(" " + (i + 1) + " ");
      System.out.print("\u001B[0m"); // Reset background

    }

    // Print the bottom border with column letters (a-h) inside white background
    System.out.print("\u001B[48;5;15m");
    System.out.print("   a  b  c  d  e  f  g  h   ");
    System.out.println("\u001B[0m");
  }

}

  // Board dimensions.
//  private static final int BOARD_SIZE_IN_SQUARES = 3; // set this to 8
//  private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
//  private static final int LINE_WIDTH_IN_PADDED_CHARS = 3; //use this code just turn this to 0
//
//  // Padded characters.
//  private static final String EMPTY = "   ";
//  private static final String X = " X ";
//  private static final String O = " O ";
//
//  private static Random rand = new Random();
//
//
//  public void drawBoard(){
//    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//
//    out.print(ERASE_SCREEN);
//
//    // drawHeaders(out);
//
//    drawTicTacToeBoard(out);
//
//    //drawTicTacToeBoard(out);
//
//    out.print(SET_BG_COLOR_BLACK);
//    out.print(SET_TEXT_COLOR_WHITE);
//  }
//
//  private static void drawHeaders(PrintStream out) {
//
//    setBlack(out);
//
//    String[] headers = { "TIC", "TAC", "TOE" };
//    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
//      drawHeader(out, headers[boardCol]);
//
//      if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
//        out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
//      }
//    }
//
//    out.println();
//  }
//
//  private static void drawHeader(PrintStream out, String headerText) {
//    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
//    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
//
//    out.print(EMPTY.repeat(prefixLength));
//    printHeaderText(out, headerText);
//    out.print(EMPTY.repeat(suffixLength));
//  }
//
//  private static void printHeaderText(PrintStream out, String player) {
//    out.print(SET_BG_COLOR_BLACK);
//    out.print(SET_TEXT_COLOR_GREEN);
//
//    out.print(player);
//
//    setBlack(out);
//  }
//
//  private static void drawTicTacToeBoard(PrintStream out) {
//
//    for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
//
//      drawRowOfSquares(out);
//
//      if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
//        // Draw horizontal row separator.
//        drawHorizontalLine(out);
//        setBlack(out);
//      }
//    }
//  }
//
//  private static void drawRowOfSquares(PrintStream out) {
//
//    for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
//      for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
//        setWhite(out);
//
//        if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
//          int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
//          int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
//
//          out.print(EMPTY.repeat(prefixLength));
//          printPlayer(out, rand.nextBoolean() ? X : O);
//          out.print(EMPTY.repeat(suffixLength));
//        }
//        else {
//          out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
//        }
//
//        if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
//          // Draw vertical column separator.
//          setRed(out);
//          out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
//        }
//
//        setBlack(out);
//      }
//
//      out.println();
//    }
//  }
//
//  private static void drawHorizontalLine(PrintStream out) {
//
//    int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
//            (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_PADDED_CHARS;
//
//    for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
//      setRed(out);
//      out.print(EMPTY.repeat(boardSizeInSpaces));
//
//      setBlack(out);
//      out.println();
//    }
//  }
//
//  private static void setWhite(PrintStream out) {
//    out.print(SET_BG_COLOR_WHITE);
//    out.print(SET_TEXT_COLOR_WHITE);
//  }
//
//  private static void setRed(PrintStream out) {
//    out.print(SET_BG_COLOR_RED);
//    out.print(SET_TEXT_COLOR_RED);
//  }
//
//  private static void setBlack(PrintStream out) {
//    out.print(SET_BG_COLOR_BLACK);
//    out.print(SET_TEXT_COLOR_BLACK);
//  }
//
//  private static void printPlayer(PrintStream out, String player) {
//    out.print(SET_BG_COLOR_WHITE);
//    out.print(SET_TEXT_COLOR_BLACK);
//
//    out.print(player);
//
//    setWhite(out);
//  }
//}
//
