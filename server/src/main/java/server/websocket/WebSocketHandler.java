package server.websocket;
import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.GameData;
import model.JoinGame;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;



@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();
  AuthDAO authDAO;
  GameDAO gameDAO;
  Set<Integer> endedGameIds;


  public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO){
    this.authDAO = authDAO;
    this.gameDAO = gameDAO;
  }

//  @OnWebSocketMessage
//  public void onMessage(Session session, String message) throws Exception {
//    session.getRemote().sendString("WebSocket response: " + message);
//  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    try {
      System.out.println("Received message from client: " + message);
      UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
      MakeMoveCommand move = new Gson().fromJson(message, MakeMoveCommand.class);
//      if(action.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
//
//      }
      //connections.addSessionToGame(action.getAuthToken(), action.getGameID(), session);
//    MakeMoveCommand move = new Gson().fromJson(message, MakeMoveCommand.class);
      /**
       * add the cases for when a player does the following, and the respective actions
       * makes a moves
       * joins a game (under the connect subclass of the userGameCommand)
       * observes a game(also under connect)
       * Leave a game
       * resign a game.
       */
      switch (action.getCommandType()) {
        case CONNECT -> connect(action.getAuthToken(), session, action.getGameID());
        //just checking to see if this is the problem
        case MAKE_MOVE -> makeMove(move.getAuthToken(), session, move.getMove(), move.getGameID());
        case LEAVE -> leaveGame(action.getAuthToken(), session, action.getGameID());
        case RESIGN -> resignGame(action.getAuthToken(), session, action.getGameID());
      }
    } catch (JsonSyntaxException e) {
      // Handle JSON deserialization issues
      System.err.println("Invalid JSON format: " + e.getMessage());
    }
    catch (IOException | ResponseException e) {
      // Handle other errors (like invalid game state or WebSocket issues)
      System.err.println("Error processing WebSocket message: " + e.getMessage());
    }
    catch (Exception e) {
      // Catch any unexpected exceptions
      e.printStackTrace();
    }
  }

  //@Override
  /*(@OnWebSocketConnect
  public void onConnect(Session session) {

    System.out.println("WebSocket connected: " + session.getRemoteAddress());
  }*/



  private void connect(String authToken, Session session, Integer gameID) throws IOException {
    // need to test whether it's a bad authToken (ie just not in the game)
    // or later on if it's an authtoken of an observer... Oh I might just need to
    //make that functionality
    if(authDAO.getAuth(authToken) == null){
      connections.addSessionToGame(authToken,  gameID, session, null);
      ErrorMessage error = new ErrorMessage("Invalid authToken");
      connections.send(authToken,session, gameID, error);
      session.close();
    }
    else{
      if(gameDAO.getGame(new JoinGame(null, gameID))== null){
        connections.addSessionToGame(authToken,  gameID, session, null);
        ErrorMessage error = new ErrorMessage("Invalid gameID");
        connections.send(authToken,session, gameID, error);
      }
      else{
        String username = authDAO.getAuth(authToken).username();
        String color = getTeamColor(authToken, gameID);
        ChessGame game = gameDAO.getGame(new JoinGame(null, gameID)).game();
        NotificationMessage notification = new NotificationMessage();
        LoadGameMessage loadGame = new LoadGameMessage(game);
        loadGame.setColorOnTop(color);
        notification.setNotification(username + " has joined as " + color);
        connections.addSessionToGame(authToken, gameID, session, color);


        //connections.broadcastInGame(authToken, session, gameID, loadGame, true);
        connections.send(authToken,session, gameID, loadGame);
        connections.broadcastInGame(authToken,session, gameID, notification, false);
      }


      //add some if statement to check for stuff
    }


    //System.out.print(color);


  }

  private void makeMove(String authToken, Session session, ChessMove move, Integer gameID) throws IOException{
    if(authDAO.getAuth(authToken) == null){
      connections.addSessionToGame(authToken,  gameID, session, null);
      //might have to make a function
      ErrorMessage error = new ErrorMessage("Invalid authToken");
      connections.send(authToken,session, gameID, error);
      session.close();
    }
    else if(gameDAO.getGame(new JoinGame(null,
            gameID)).game().getBoard().getPiece(move.getStartPosition()) == null){
      var error = new ErrorMessage("That space is null");
      connections.send(authToken, session, gameID, error);
    }
    else if(getTeamEnum(authToken, gameID) !=
            gameDAO.getGame(new JoinGame(null, gameID)).game().getBoard().
                    getPiece(move.getStartPosition()).getTeamColor()){
      ErrorMessage error = new ErrorMessage("Not allowed to move");
      connections.send(authToken, session, gameID, error);

    }
    else if(isObserver(authToken, gameID)){
      //generate an error message

    }
    else {
      JoinGame getGame=new JoinGame(null, gameID);
      ChessGame game=gameDAO.getGame(getGame).game();
      ChessPiece.PieceType type = game.getBoard().getPiece(move.getStartPosition()).getPieceType();
      String stringType = type.toString();
      String username=authDAO.getAuth(authToken).username();
      String color = getTeamColor(authToken, gameID);
      String otherTeamColor;
      String otherTeamUsername = "";
      String otherTeamAuthToken = "";
      Set<Connection> setOfConnections = connections.getConnectionsForGame(gameID);
      if(color.equals("white")){
        otherTeamColor = "black";
      }
      else{
        otherTeamColor = "white";
      }
      for(Connection connection : setOfConnections){
        if(otherTeamColor.equals(connection.color)){
          otherTeamAuthToken = connection.authToken;
          otherTeamUsername = authDAO.getAuth(connection.authToken).username();
        }
      }




      var notification=new NotificationMessage();
      var notification2 = new NotificationMessage();

      //here check if they're name is either in black or white and change the move
      //accordingly. If they're name isn't in either white or black then they are an
      //observer and send them an error message accordingly.
      //ie if they're white and send in a move just directly change it to numbers
      //and send it in as "move". But if they're black when you receive it call a
      //method on it that switches it around so it's takes it from their perspective
      // to how the game is actually stored. Meaning if
      //the move is for black from (1,1) -> (2,1) change it so that it's actually from
      // (8,8) -> (7,8)
      try {
//        if(game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)){
//          String winner;
//          String loser;
//          if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
//            winner = "White ";
//            loser = " Black";
//          }
//          else{
//            winner = "Black ";
//            loser = " White";
//          }
//          notification.setNotification(winner + "has put" + loser + " in checkmate");
//          //might need this to be a send rather than a broadcast
//          connections.broadcastInGame(authToken, session, gameID, notification, true);
//        }
        if (!game.active) {
          //maybe change this
          var error = new ErrorMessage("Game has been resigned, cannot play anymore");
          //either make an error or a notification idk;
          //might need this to be a send rather than a broadcast
          connections.send(authToken, session, gameID, error);
          //connections.broadcastInGame(authToken, session, gameID, error, false, true);
        }
        else {
          game.makeMove(move);

          if(game.isInCheck(getTeamEnum(otherTeamAuthToken, gameID))){
            notification2.setNotification(otherTeamUsername + " is in check");
            connections.broadcastInGame(authToken, session, gameID, notification2, true);
          }
          if(game.isInCheck(getTeamEnum(authToken, gameID))){
            notification2.setNotification(username + " is in check");
            connections.broadcastInGame(authToken, session, gameID, notification2, true);
          }
//          if(game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)){
//            game.active = false;
//          }
          if(game.isInCheckmate(getTeamEnum(authToken, gameID))){
            game.active = false;
            notification2.setNotification(username + " has been checkmated!!!");
            connections.broadcastInGame(authToken, session, gameID, notification2, true);
          }
          if(game.isInCheckmate(getTeamEnum(otherTeamAuthToken, gameID))){
            game.active = false;
            notification2.setNotification(otherTeamUsername + " has been checkmated!!!");
          }

          gameDAO.updateGameItself(gameID, game);
          var reloadGame=new LoadGameMessage(game);
          reloadGame.setColorOnTop(getTeamColor(authToken, gameID));
          String newPosition = goBackToLetter(move.getEndPosition());
          notification.setNotification(username + " has moved a " + stringType + " to " + newPosition );
          connections.broadcastInGame(authToken, session, gameID, reloadGame, true);
          connections.broadcastInGame(authToken, session, gameID, notification, false);
//          if(notification2.getMessage() != null){
//            connections.broadcastInGame(authToken, session, gameID, notification2, true);
//          }

        }

      } catch (InvalidMoveException e) {
        ErrorMessage error = new ErrorMessage(e.reason);
        connections.send(authToken, session, gameID, error);
      } catch (ResponseException e) {
        // put some stuff here and what not
      }
    }
    //do I need to notify them of the move?
    /**
     * a lot of other cases I need to look out for above but that should
     * be a good starting point.
     *
     * now we just call a method that updates the gameitself in game data
     * then send the game to LoadGameMessage which will redraw the board
     * for everybody.
     */
  }

  private void leaveGame(String authToken, Session session, Integer gameID) throws IOException, ResponseException{
    String username = authDAO.getAuth(authToken).username();
    String color = getTeamColor(authToken, gameID);
    var notification = new NotificationMessage();
    if(isObserver(authToken, gameID)){
      notification.setNotification(username + "has left the game");
      connections.broadcastInGame(authToken, session,gameID, notification, false);
      connections.removeSessionFromGame(authToken, gameID, session);
    }
    //we'll see if this works just setting the player username back to null.
    else {
      gameDAO.updateGame(new JoinGame(color, gameID), null, color + "Username");
      //just to check that everything is going smoothly
      GameData gameData=gameDAO.getGame(new JoinGame(null, gameID));
      notification.setNotification(username + "has left the game");
      connections.broadcastInGame(authToken, session, gameID, notification, false);
      connections.removeSessionFromGame(authToken, gameID, session);
    }
  }

  private void resignGame(String authToken, Session session, Integer gameID) throws IOException{
    if(isObserver(authToken, gameID)){
      ErrorMessage error = new ErrorMessage("You are an Observer and cannot resign the game");
      connections.send(authToken, session, gameID, error);
    }
    //the game is marked as over and no one can make any moves now.
    String username = authDAO.getAuth(authToken).username();
    ChessGame game = gameDAO.getGame(new JoinGame(null, gameID)).game();
    if(!game.active){
      ErrorMessage error = new ErrorMessage("the Game has already been resigned");
      connections.send(authToken, session, gameID, error);
    }
    else {
      game.active=false;
      try {
        gameDAO.updateGameItself(gameID, game);
      } catch (ResponseException e) {
        // decide what to do later if it is a problem.
      }
      //get game from db
      //set the active to false
      // put back to db
      //do same sort of thing with checkmate
      //also don't let observers resign

      var notification=new NotificationMessage();
      notification.setNotification(username + "has resigned the game");
      //shoudl the observer see or no?
      connections.broadcastInGame(authToken, session, gameID, notification, true);
    }
  }


  private String getTeamColor(String authToken, Integer gameId){
//    Set<Connection> setOfConnections = connections.getConnectionsForGame(gameId);
//    for (Connection connection : setOfConnections){
//      if(authToken.equals(connection.authToken)){
//        return connection.color;
//      }
//      //could mess some stuff up...
//    }
//    return "observer";
// have the above be the thing if the get connections isn't null. Because if the connections
    //are null the we can do the method below (ie it's the first one)
    GameData game = gameDAO.getGame(new JoinGame(null, gameId));
    String username = authDAO.getAuth(authToken).username();
    if(username.equals(game.whiteUsername())){
      return "white";
    }
    else if (username.equals(game.blackUsername())){
      return "black";
    }
    else{
      return "observer";
    }
  }

  private ChessGame.TeamColor getTeamEnum(String authToken, Integer gameId){
    String color = getTeamColor(authToken, gameId);
    if(color.equals("white")){
      return ChessGame.TeamColor.WHITE;
    }
    else{
      return ChessGame.TeamColor.BLACK;
    }
  }


  @OnWebSocketClose
  public void onClose(Session session, int statusCode, String reason) {
    System.out.println("WebSocket closed: " + statusCode + " " + reason);
  }

  @OnWebSocketError
  public void onError(Session session, Throwable error) {
    System.err.println("WebSocket error: " + error.getMessage());
    error.printStackTrace();
  }

  private String goBackToLetter(ChessPosition endPosition){
    int col = endPosition.getColumn();
    String letter = getLetter(col);
    return letter + endPosition.getRow();
  }

  private String getLetter(Integer col){
    if(col == 1){
      return "a";
    } else if(col ==2){
      return "b";
    } else if(col ==3){
      return "c";
    }else if(col ==4){
      return "d";
    }else if(col ==5){
      return "e";
    }else if(col ==6){
      return "f";
    }else if(col ==7){
      return "g";
    }else {
      return "h";
    }
  }

  private Boolean isObserver(String authToken, Integer gameId){
    GameData gameData =  gameDAO.getGame(new JoinGame(null , gameId));
    String username = authDAO.getAuth(authToken).username();

    if(gameData.whiteUsername() == null && gameData.blackUsername() == null){
      return true;
    }
    else if(gameData.whiteUsername() == null){
      return !gameData.blackUsername().equals(username);
    }
    else if(gameData.blackUsername() == null){
      return !gameData.whiteUsername().equals(username);
      //!Objects.equals(gameData.blackUsername(), username) && !gameData.whiteUsername().equals(username)
    }
    else if(!gameData.whiteUsername().equals(username) && !gameData.blackUsername().equals(username)){
      return true;
    }
    else{
      return false;
    }
  }


}
