package ui;

public class GamePlayUI {
  DrawChessBoard drawIt = new DrawChessBoard();
  public void drawBoard(){
    System.out.println("\n White On Top");
    drawIt.printChessBoard("white");
    //drawIt.printChessBoard("black");
    System.out.println("\n Black On Top");
    drawIt.printChessBoard("black");
  }
}
