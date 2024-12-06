package ui;

public class GamePlayUI {
  // intialize the session here... looks like the websocket client code
  /**
   * that means I need to pass in the WebScoketFacade object from the client
   * when gameplay is called.
   */
  DrawChessBoard drawIt = new DrawChessBoard();
  public void drawBoard(){
    System.out.println("\n White On Top");
    drawIt.printChessBoard("white");

    System.out.println("\n Black On Top");
    drawIt.printChessBoard("black");
  }
}
