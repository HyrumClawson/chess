package ui;

import java.util.Arrays;
import Exception.ResponseException;
import model.AuthData;
import model.UserData;

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
      if(state == State.SIGNEDOUT){
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
          case "register" -> register(params);
//        case "rescue" -> rescuePet(params);
//        case "list" -> listPets();
//        case "signout" -> signOut();
//        case "adopt" -> adoptPet(params);
//        case "adoptall" -> adoptAllPets();
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


  public String login(String... params) throws ResponseException {
    //assertSignedIn();
    if (params.length == 2) {
      UserData user = new UserData(params[0], params[1], null);
      try{
        AuthData authData = serverFacade.login(user);
        state = State.SIGNEDIN;
        visitorName = authData.username();
        //postLoginUi.run(visitorName);

      }
      catch(ResponseException e){
        ResponseException r = new ResponseException(e.typeOfException);
        r.setMessage(e.getMessage());
        throw r;
      }

    }
    ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    r.setMessage("Bad Request, missing either username, password, or email. Or added \n" +
            "something extra. \n try again");
    throw r;
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
    if (state == State.SIGNEDOUT) {
      return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
    }

    return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
  }

//  private void assertSignedIn() throws ResponseException {
//    if (state == State.SIGNEDOUT) {
//      throw new ResponseException(400, "You must sign in");
//    }
//  }
//}







}
