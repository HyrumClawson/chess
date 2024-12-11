package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.util.Scanner;

public class GamePlayUI {
  // intialize the session here... looks like the websocket client code
  /**
   * that means I need to pass in the WebScoketFacade object from the client
   * when gameplay is called.
   */
  ChessGame game = new ChessGame();
  DrawChessBoard drawIt = new DrawChessBoard(game);
  String serverUrl;
  Client client;

  public GamePlayUI(String serverUrl, Client client){
    this.serverUrl = serverUrl;
    this.client = client;
  }

  public String run(){
    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("leave")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(result);
        if(result.equals("leave")){
          return result;
        }

      }
      catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
    return result;
  }


  private void printPrompt() {
    //+ RESET + GREEN
    System.out.print("\n" + "[GAME_PLAY]" + ">>> " );
  }

}
