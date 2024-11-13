package ui;


import java.util.Scanner;

import ui.EscapeSequences;

public class PreLoginUI {
  /** this will be the repl class */
  private final Client client;

  public PreLoginUI(String serverUrl){
    client = new Client(serverUrl);
  }

  public void run(){
    System.out.println("Is it working?");
    System.out.println("\uD83D\uDC36 Welcome to 240 Chess. Type Help to get started.");
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
    System.out.print("\n" + "[LOGGED_OUT]" + ">>> " );
  }

}
