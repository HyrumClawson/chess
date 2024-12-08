package ui;

import java.util.Arrays;
import java.util.Scanner;

public class PostLoginUI {

  String serverUrl;
  ServerFacade serverFacade;
  Client client;

  GamePlayUI gamePlay;


  public PostLoginUI(String serverUrl, Client client){
    this.serverUrl = serverUrl;
    this.client = client;
    gamePlay = new GamePlayUI(serverUrl, client);

  }

  public String run(){
    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("logout")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(result);
        if(result.equals("quit")){
          return result;
        }
        
        String[] resultFirst = result.split(" ");
        if(resultFirst[0].equals("Observing") || resultFirst[0].equals("Joined")) {
          gamePlay.drawBoard();
          gamePlay.run();
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
    System.out.print("\n" + "[LOGGED_IN]" + ">>> " );
  }










}
