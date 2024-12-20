package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class Client implements NotificationHandler {
  private String serverUrl;
  private String visitorName = null;
  private String authToken;
  private Integer gameId;
  private ServerFacade serverFacade;
  ChessGame gameToUpdate = new ChessGame();
  String color = null;

  //here's the websocket facade object.
  public WebSocketFacade ws;

  private State state = State.SIGNEDOUT;

  final static String RESET = EscapeSequences.RESET_TEXT_COLOR;//"\033[0m";      // Reset color to default
  final static String BLUE = SET_TEXT_COLOR_BLUE;//"\033[34m";      // Blue color
  final static String MAGENTA = EscapeSequences.SET_TEXT_COLOR_MAGENTA;//"\033[35m";


  /**
   * So essentially put all the calls to the server in here and based on whether it's
   * the state is signed in or signed out. Like all the calls from the prelogin and postlogin
   * will come here to this client.
   *
   * also have this implement that notification handler that we spoke of.
   * */
  public Client(String serverUrl) {
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
      else if(state == State.SIGNEDIN){
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
      else{
        return switch (cmd) {
          case "redraw" -> redrawBoard(params);
          case "leave" -> leaveGame(params);
          case "move" -> makeMove(params);
          case "resign" -> resignGame(params);
          case "strategy" -> highlightMoves(params);
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
        authToken = authData.authToken();
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
        authToken = authData.authToken();
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
        state = State.GAME;
        //, authToken, joinRequest.gameID()
        ws = new WebSocketFacade(serverUrl, this, authToken, joinRequest.gameID());
        ws.connect();
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
      state = State.GAME;
      //, authToken, joinRequest.gameID()
      ws = new WebSocketFacade(serverUrl, this, authToken, id);
      ws.connect();

      return String.format("Observing game %s", gameName);

    }
    catch(ResponseException e){
      ResponseException r = new ResponseException(e.typeOfException);
      r.setMessage(e.getMessage());
      throw r;
    }
  }


  public String redrawBoard(String ... params) throws ResponseException{
    //just use the
    //ws.redrawChessBoard();
    DrawChessBoard draw = new DrawChessBoard(gameToUpdate);
    draw.printChessBoard(this.color);
    return "";
  }

  public String leaveGame(String ... params) throws ResponseException{
    state = State.SIGNEDIN;
    ws.leave();
    return "leave";
  }

  public String makeMove(String ... params) throws ResponseException{
    if(params.length != 1){
        ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
        r.setMessage("put in the start position then \nthe position you want to move to.  try again");
        throw r;
    }
    String regex = "^[a-h]\\d[a-h]\\d$";  // Matches: letter-digit-letter-digit

    if (!params[0].matches(regex)) {
      ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
      r.setMessage("put in the form col-row-col-row. Try again");
      throw r;
//      System.out.println("Input matches the regex!");
    }
    else {
      ChessMove newMove = generateMove(params[0]);
      ChessPiece.PieceType type = gameToUpdate.getBoard().getPiece(newMove.getStartPosition()).getPieceType();
      if(type == ChessPiece.PieceType.PAWN){
        if(newMove.getEndPosition().getRow() == 1 || newMove.getEndPosition().getRow() == 9){
          Scanner scanner = new Scanner(System.in);

          // Prompt the user for input
          System.out.println("Please enter your desired promotion piece: ");

          String userInput = scanner.nextLine();
          ChessPiece.PieceType promtionPiece = changeToPieceEnum(userInput);
          newMove.setPromotionPiece(promtionPiece);
        }
      }
      ws.makeMove(newMove);
      //System.out.println("Input does NOT match the regex.");
    }

    return "";
  }

  public String resignGame(String ... params) throws ResponseException{
    return "";
  }

  public String highlightMoves(String ... params) throws ResponseException{
    if(params.length != 1){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
      r.setMessage("put in the position that you want to see \n the moves for.  try again");
      throw r;
    }
    String regex = "^[a-h][0-9]$";  // Matches: letter-digit-letter-digit

    if (!params[0].matches(regex)) {
      ResponseException r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
      r.setMessage("put in the form col-row. Try again");
      throw r;
//      System.out.println("Input matches the regex!");
    }
    else{
      ChessMove newMove = generateMove(params[0]);

      DrawChessBoard drawer = new DrawChessBoard(gameToUpdate);

      drawer.setPositionToLookAtMoves(newMove.getStartPosition());
      //might need to come back and do the opposite of teh color.
      drawer.printChessBoard(color);
    }
    return "";
  }

















  public String help() {
    if (state == State.SIGNEDOUT) {
      String returnString=BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET +
              " - " + MAGENTA + "to create an account" + RESET + "\n" +
              BLUE + "login <USERNAME> <PASSWORD>" + RESET
              + " - " + MAGENTA + "to play chess" + RESET + "\n" +
              BLUE + "quit" + RESET + " - " + MAGENTA + "playing chess" + RESET + "\n" +
              BLUE + "help" + RESET + " - " + MAGENTA + "with possible commands" + RESET;
      return returnString;
    } else if (state == State.SIGNEDIN) {
      String returnString=BLUE + "create <NAME>" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
              BLUE + "list" + RESET + " - " + MAGENTA + "games" + RESET + "\n" +
              BLUE + "join <ID> [WHITE|BLACK]" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
              BLUE + "observe <ID>" + RESET + " - " + MAGENTA + "a game" + RESET + "\n" +
              BLUE + "logout" + RESET + " - " + MAGENTA + "when you are done" + RESET + "\n" +
              BLUE + "quit" + RESET + " - " + MAGENTA + "playing chess" + RESET + "\n" +
              BLUE + "help" + RESET + " - " + MAGENTA + "with possible commands" + RESET;

      return returnString;
    }
    //
    else{
      String returnString=BLUE + "redraw" + RESET + " - " + MAGENTA + "your board" + RESET + "\n" +
              BLUE + "leave" + RESET + " - " + MAGENTA + "your game" + RESET + "\n" +
              BLUE + "move <start>[a-g][1-8]<end>[a-g][1-8] " + RESET + " - " + MAGENTA + "a chesspiece" + RESET + "\n" +
              BLUE + "resign" + RESET + " - " + MAGENTA + "acknowledge defeat" + RESET + "\n" +
              BLUE + "strategy <position>[a-g][1-8]" + RESET + " - " + MAGENTA + "see different moves for a piece" + RESET + "\n" +
              BLUE + "help" + RESET + " - " + MAGENTA + "with possible commands" + RESET;

      return returnString;
    }
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

  private ChessMove generateMove(String input){
    char[] charArray = input.toCharArray();

    char symbol = charArray[0];
    Integer row1 = Character.getNumericValue(charArray[1]);
    char symbol2;
    Integer row2;
    Integer col2;
    Integer col1 = letterToNumber(symbol);
    if(charArray.length > 2) {
      symbol2=charArray[2];
      row2=Character.getNumericValue(charArray[3]);
      col2 = letterToNumber(symbol2);
      return  new ChessMove(new ChessPosition(row1, col1), new ChessPosition(row2,col2), null);
    }
    else{
      return new ChessMove(new ChessPosition(row1, col1), new ChessPosition(1,1), null);
    }

  }

  private Integer letterToNumber(char symbol ){
    String letter = Character.toString(symbol);
    if("a".equals(letter)){
      return 1;
    } else if("b".equals(letter)){
      return 2;
    }else if("c".equals(letter) ){
      return 3;
    }else if("d".equals(letter) ){
      return 4;
    }else if("e".equals(letter) ){
      return 5;
    }else if("f".equals(letter) ){
      return 6;
    }else if("g".equals(letter) ){
      return 7;
    }else {
      return 8;
    }
  }

  public ChessPiece.PieceType changeToPieceEnum(String userInput){
    switch(userInput){
      case "queen":
        return ChessPiece.PieceType.QUEEN;
      case "bishop":
        return ChessPiece.PieceType.BISHOP;
      case "knight":
        return ChessPiece.PieceType.KNIGHT;
      case "rook":
        return ChessPiece.PieceType.ROOK;
      default:
        return null;

    }
  }



  public void notify(String message){
    ServerMessage checkWithThis = new Gson().fromJson(message, ServerMessage.class);
    switch (checkWithThis.getServerMessageType()) {
      case NOTIFICATION:
        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
        System.out.print("\n");
        System.out.print(SET_TEXT_COLOR_BLUE + notification.getMessage() + RESET);
        System.out.print("\n");
        break;
      case LOAD_GAME:
        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        gameToUpdate = loadGameMessage.getGame();
        this.color = loadGameMessage.getColor();
        DrawChessBoard board = new DrawChessBoard(loadGameMessage.getGame());
        System.out.print("\n");
        board.printChessBoard(loadGameMessage.getColor());
        System.out.print("\n");
        break;
      case ERROR:
        ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
        System.out.print("\n");
        System.out.print(SET_TEXT_COLOR_RED + error.getErrorMessage() + RESET);
        System.out.print("\n");
        //System.out.print("this is where the errors go");
        break;
      default:
        System.out.print("ooops");
    }

    System.out.print("\n" + "[GAME_PLAY]" + ">>> " );

    //System.out.println(message);
  }

}
