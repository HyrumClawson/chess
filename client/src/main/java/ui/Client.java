package ui;

import java.util.ArrayList;
import java.util.Arrays;
import exception.ResponseException;
import model.*;

public class Client {
  private String serverUrl;
  private String visitorName = null;
  private final ServerFacade serverFacade;
  private State state = State.SIGNEDOUT;

  final static String RESET = EscapeSequences.RESET_TEXT_COLOR;//"\033[0m";      // Reset color to default
  final static String BLUE = EscapeSequences.SET_TEXT_COLOR_BLUE;//"\033[34m";      // Blue color
  final static String MAGENTA = EscapeSequences.SET_TEXT_COLOR_MAGENTA;//"\033[35m";


  /**
   * So essentially put all the calls to the server in here and based on whether it's
   * the state is signed in or signed out. Like all the calls from the prelogin and postlogin
   * will come here to this client.
   * */
  public Client(String serverUrl){
    serverFacade = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
  }

  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);

      if(state == State.SIGNEDOUT) {
        return switch (cmd) {
          case "register" -> register(params);
          case "login" -> login(params);
          case "quit" -> "quit";
          default -> help();
        };
      }
      else{
        return switch (cmd) {
          case "create" -> createGame(params);
          case "list" -> listGames();
          case "join" -> joinGame(params);
          case "logout" -> logout();
          case "observe" -> observe(params);
          case "quit" -> "quit";
          default -> help();
        };
      }

    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String register(String... params) throws ResponseException {
    //I have no idea if this will work.
    if (params.length == 3) {
      UserData newUser = new UserData(params[0], params[1], params[2]);
      try {
        AuthData authData = serverFacade.register(newUser);
        state = State.SIGNEDIN;
        visitorName = authData.username();

      }
      catch(ResponseException e) {
        ResponseException r = new ResponseException(e.typeOfException);
        r.setMessage(e.getMessage());
        throw r;
      }
      return String.format("logged in as %s.", visitorName);
    }
    ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    r.setMessage("Bad Request, missing either username, password, or email. Or added \n" +
            "something extra. \n try again");
    throw r;
  }


  public String createGame(String... params) throws ResponseException {
    //assertSignedIn();
    if (params.length == 1) {
      GameData newGame = new GameData(0, null,
              null, params[0], null );
      try{
        GameID gameIdObject = serverFacade.addGame(newGame);
        int gameId = gameIdObject.gameID();
        return String.format("Successfully created %s.", params[0]);
      }

      catch(ResponseException e){
        ResponseException r = new ResponseException(e.typeOfException);
        r.setMessage(e.getMessage());
        throw r;
      }

    }
    ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    r.setMessage("Bad Request, need a name for the game \n try again");
    throw r;
  }

  public String login(String... params) throws ResponseException {
    //assertSignedIn();
    if (params.length == 2) {
      UserData user = new UserData(params[0], params[1], null);
      try{
        AuthData authData = serverFacade.login(user);
        state = State.SIGNEDIN;
        visitorName = authData.username();
        //postLoginUi.run(visitorName);
        return String.format("logged in as %s.", visitorName);
      }

      catch(ResponseException e){
        ResponseException r = new ResponseException(e.typeOfException);
        r.setMessage(e.getMessage());
        throw r;
      }

    }
    ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    r.setMessage("Bad Request, missing either username, password. Or added \n" +
            "something extra. \n try again");
    throw r;
  }


  public String listGames() throws ResponseException {
      try{
        ArrayList<ListingGameData> gameList = serverFacade.listGames();
        String output = "";
        StringBuilder sb = new StringBuilder();
        for(ListingGameData game : gameList){
          output = "\n" + BLUE + game.gameID() + ". " + "GameName: " +
                  RESET + MAGENTA + game.gameName() + " " + RESET + BLUE +
          "White: " + RESET + MAGENTA + game.whiteUsername() + " " +RESET + BLUE +
                  "Black: " + RESET + MAGENTA + game.blackUsername() + RESET;
          sb.append(formatTheListLine(output)).append("\n");
        }
        return sb.toString();
      }
      catch(ResponseException e){
        ResponseException r = new ResponseException(e.typeOfException);
        r.setMessage(e.getMessage());
        throw r;
      }
  }

  public String joinGame(String... params) throws ResponseException {
    //assertSignedIn();
    if (params.length == 2) {
      if(!isAnInt(params[0])){
        ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
        r.setMessage("Bad Request, ID needs to be a number \n try again");
        throw r;
      }
      int id = Integer.parseInt(params[0]);
      String desiredColor = params[1].toUpperCase();
      if(!desiredColor.equals("WHITE") && !desiredColor.equals("BLACK")){
        ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
        r.setMessage("Bad Request, please specify white or black \n try again");
        throw r;
      }

      JoinGame joinRequest = new JoinGame(desiredColor, id);
      try{
        serverFacade.joinGame(joinRequest);
        return String.format("Joined Game %d", id);
      }

      catch(ResponseException e){
        ResponseException r = new ResponseException(e.typeOfException);
        r.setMessage(e.getMessage());
        throw r;
      }

    }
    ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    r.setMessage("Bad Request, missing either gameId, desired color, or added \n" +
            "something extra. \n try again");
    throw r;
  }


  public String logout() throws ResponseException{
    try{
      serverFacade.logout();
      String name = visitorName;
      visitorName = null;
      state = State.SIGNEDOUT;
      return "logout";
    }
    catch(ResponseException e){
      ResponseException r = new ResponseException(e.typeOfException);
      r.setMessage(e.getMessage());
      throw r;
    }
  }

  public String observe(String ... params) throws ResponseException{
    if(params.length != 1){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
      r.setMessage("Is it really that hard? Just put in a gameID \n try again");
      throw r;
    }
    if(!isAnInt(params[0])){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
      r.setMessage("Bruh please just put in a number \n try again");
      throw r;
    }
    try{
      int id = Integer.parseInt(params[0]);
      ArrayList<ListingGameData> list = serverFacade.listGames();
      String gameName = "";
      for(ListingGameData game : list){
        if(id == game.gameID()){
          gameName = game.gameName();
        }
      }
      if(gameName.isEmpty()){
        ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
        r.setMessage("That gameID doesn't exist \n try again");
        throw r;
      }
      return String.format("Observing game %s", gameName);

    }
    catch(ResponseException e){
      ResponseException r = new ResponseException(e.typeOfException);
      r.setMessage(e.getMessage());
      throw r;
    }
  }


  public String help() {
    if (state == State.SIGNEDOUT) {
      String returnString = BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET +
              " - " + MAGENTA + "to create an account" + RESET + "\n" +
              BLUE + "login <USERNAME> <PASSWORD>" + RESET
              + " - " + MAGENTA + "to play chess" + RESET + "\n" +
              BLUE + "quit" + RESET + " - " + MAGENTA + "playing chess" + RESET + "\n" +
              BLUE + "help" + RESET + " - " + MAGENTA + "with possible commands" + RESET;
      return returnString;
    }

    String returnString = BLUE + "create <NAME>" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
            BLUE + "list" + RESET + " - " + MAGENTA + "games" + RESET + "\n" +
            BLUE + "join <ID> [WHITE|BLACK]" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
            BLUE + "observe <ID>" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
            BLUE + "logout" + RESET + " - " + MAGENTA + "when you are done" + RESET + "\n" +
            BLUE + "quit" + RESET + " - " + MAGENTA + "playing chess" + RESET + "\n" +
            BLUE + "help" + RESET + " - " + MAGENTA + "with possible commands" + RESET;

    return returnString;
  }


  private Boolean isAnInt(String string){
      try {
        Integer.parseInt(string);
        return true;
      } catch (NumberFormatException e) {
        return false;
      }
  }

  private String formatTheListLine(String line) {
    String[] parts = line.split(" ");
    return String.format("%-10s%-10s%-30s%-25s%-25s%-25s%-25s", parts[0], parts[1], parts[2], parts[3], parts[4],
            parts[5], parts[6]);
  }

}
