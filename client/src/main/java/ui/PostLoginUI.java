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

  public String run(){
    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(result);
        if(result.equals("quit")){
          return result;
        }
      } catch (Throwable e) {
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
