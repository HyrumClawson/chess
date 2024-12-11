package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class TicTacToe {

  // Board dimensions.
  private static final int BOARD_SIZE_IN_SQUARES = 3; // set this to 8
  private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
  private static final int LINE_WIDTH_IN_PADDED_CHARS = 3; //use this code just turn this to 0

  // Padded characters.
  private static final String EMPTY = "   ";
  private static final String X = " X ";
  private static final String O = " O ";

  private static Random rand = new Random();

  static DrawChessBoard board;
  static ChessGame game = new ChessGame();



  public static void main(String[] args) {


    board = new DrawChessBoard(game);
    board.printChessBoard("black");
    System.out.println("\n");
    board.printChessBoard("white");
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

    
  }


}
