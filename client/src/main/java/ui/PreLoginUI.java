package ui;


import java.util.Objects;
import java.util.Scanner;

import ui.EscapeSequences;

public class PreLoginUI {
  /** this will be the repl class */
  private final Client client;
  private final PostLoginUI postLoginUI;

  public PreLoginUI(String serverUrl){
    client = new Client(serverUrl);
    postLoginUI = new PostLoginUI(serverUrl, client);

  }

  public void run(){
    System.out.println("Is it working?");
    System.out.println(EscapeSequences.WHITE_KING+ " Welcome to 240 Chess. " +
            "Type Help to get started." + EscapeSequences.WHITE_KING);
    //System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        String[] words = result.split("\\s+");
        if(Objects.equals(words[0], "logged")){
          postLoginUI.run();
        }

        //I might need to figure this out more
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
