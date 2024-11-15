package ui;

public class GamePlayUI {
  DrawChessBoard drawIt = new DrawChessBoard();
  public void drawBoard(){
    System.out.println("\n here be the board");
    drawIt.printChessBoard("white");
    drawIt.printChessBoard("black");
  }
}
