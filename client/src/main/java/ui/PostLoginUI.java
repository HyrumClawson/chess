package ui;

import java.util.Arrays;
import java.util.Scanner;

public class PostLoginUI {

  String serverUrl;
  ServerFacade serverFacade;
  Client client;


  public PostLoginUI(String serverUrl, Client client){
    this.serverUrl = serverUrl;
    this.client = client;




  }

  public void run(){
    //System.out.println("Logged in as " + visitorName);
    System.out.println("Is it working?");
    //System.out.println("\uD83D\uDC36 Welcome to 240 Chess. Type Help to get started.");
    //System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }


  private void printPrompt() {
    //+ RESET + GREEN
    System.out.print("\n" + "[LOGGED_IN]" + ">>> " );
  }










}
