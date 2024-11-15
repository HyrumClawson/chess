package ui;

import java.util.ArrayList;
import java.util.Arrays;
import Exception.ResponseException;
import model.*;

public class Client {
  private String serverUrl;
  private String visitorName = null;
  private final ServerFacade serverFacade;
  private State state = State.SIGNEDOUT;

  private PostLoginUI postLoginUi;


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
//        case "list" -> listPets();
//        case "signout" -> signOut();
//        case "adopt" -> adoptPet(params);
//        case "adoptall" -> adoptAllPets();
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
       // postLoginUi.run(visitorName);

      }
      catch(ResponseException e) {
        ResponseException r = new ResponseException(e.typeOfException);
        r.setMessage(e.getMessage());
        throw r;
      }
      //ws = new WebSocketFacade(serverUrl, notificationHandler);
      //ws.enterPetShop(visitorName);
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
    //assertSignedIn();
      try{
        ArrayList<ListingGameData> gameList = serverFacade.listGames();
        String output = "";
        StringBuilder sb = new StringBuilder();
        for(ListingGameData game : gameList){
          output = "\n" + String.valueOf(game.gameID()) + ". " + "GameName: " + game.gameName() +
          " White: " + game.whiteUsername() + " Black: " + game.blackUsername();
          sb.append(formatTheListLine(output)).append("\n");
        }
        return sb.toString();
        //return String.format(output);
        //return String.format(gameList.toString());
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
//        AuthData authData = serverFacade.login(user);
//        state = State.SIGNEDIN;
//        visitorName = authData.username();
        //postLoginUi.run(visitorName);
        return String.format("Joined Game %d", id);
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



//  public String listPets() throws ResponseException {
//    assertSignedIn();
//    var pets = server.listPets();
//    var result = new StringBuilder();
//    var gson = new Gson();
//    for (var pet : pets) {
//      result.append(gson.toJson(pet)).append('\n');
//    }
//    return result.toString();
//  }
//
//  public String adoptPet(String... params) throws ResponseException {
//    assertSignedIn();
//    if (params.length == 1) {
//      try {
//        var id = Integer.parseInt(params[0]);
//        var pet = getPet(id);
//        if (pet != null) {
//          server.deletePet(id);
//          return String.format("%s says %s", pet.name(), pet.sound());
//        }
//      } catch (NumberFormatException ignored) {
//      }
//    }
//    throw new ResponseException(400, "Expected: <pet id>");
//  }
//
//  public String adoptAllPets() throws ResponseException {
//    assertSignedIn();
//    var buffer = new StringBuilder();
//    for (var pet : server.listPets()) {
//      buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
//    }
//
//    server.deleteAllPets();
//    return buffer.toString();
//  }
//
//  public String signOut() throws ResponseException {
//    assertSignedIn();
//    ws.leavePetShop(visitorName);
//    ws = null;
//    state = State.SIGNEDOUT;
//    return String.format("%s left the shop", visitorName);
//  }
//
//  private Pet getPet(int id) throws ResponseException {
//    for (var pet : server.listPets()) {
//      if (pet.id() == id) {
//        return pet;
//      }
//    }
//    return null;
//  }

  public String help() {
    String RESET = "\033[0m";      // Reset color to default
    String BLUE = "\033[34m";      // Blue color
    String MAGENTA = "\033[35m";
    if (state == State.SIGNEDOUT) {
      return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
    }
//""" + BLUE + "<NAME>" + RESET + " - a game
//    """
//                create <NAME> - a game
//                list - games
//                join <ID> [WHITE|BLACK] - a game
//                observe <ID> - a game
//                logout - when you are done
//                quit - playing chess
//                help - with possible commands
//                """;
    String returnString = BLUE + "create <NAME>" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
            BLUE + "list" + RESET + " - " + MAGENTA + "games" + RESET + "\n" +
            BLUE + "join <ID> [WHITE|BLACK]" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
            BLUE + "observe <ID>" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
            BLUE + "logout" + RESET + " - " + MAGENTA + "when you are done" + RESET + "\n" +
            BLUE + "quit" + RESET + " - " + MAGENTA + "playing chess" + RESET + "\n" +
            BLUE + "help" + RESET + " - " + MAGENTA + "with possible commands" + RESET;

    return returnString;
  }

//  private void assertSignedIn() throws ResponseException {
//    if (state == State.SIGNEDOUT) {
//      throw new ResponseException(400, "You must sign in");
//    }
//  }
//}

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
    return String.format("%-1s%-10s%-15s%-10s%-10s%-10s%-10s", parts[0], parts[1], parts[2], parts[3], parts[4],
            parts[5], parts[6]);
  }

//"%-5s%-15s%-10s%-10s"





}
