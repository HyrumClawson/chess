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
    else if(getTeamEnum(authToken, gameID) !=
            gameDAO.getGame(new JoinGame(null, gameID)).game().getBoard().
                    getPiece(move.getStartPosition()).getTeamColor()){
      ErrorMessage error = new ErrorMessage("Not allowed to move");
      connections.send(authToken, session, gameID, error);

    }
    else if(isObserver(authToken, gameID)){

    }
    else {
      JoinGame getGame=new JoinGame(null, gameID);
      ChessGame game=gameDAO.getGame(getGame).game();
      String username=authDAO.getAuth(authToken).username();
      String stringVersion=move.toString();


      var notification=new NotificationMessage();

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
        if (!game.active) {
          //maybe change this
          var error = new ErrorMessage("Game has been resigned, cannot play anymore");
          //either make an error or a notification idk;
          //might need this to be a send rather than a broadcast
          connections.broadcastInGame(authToken, session, gameID, error, true);
        }
        else if(game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)){
          String winner;
          String loser;
          if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
            winner = "White ";
            loser = " Black";
          }
          else{
            winner = "Black ";
            loser = " White";
          }
          notification.setNotification(winner + "has put" + loser + " in checkmate");
          //might need this to be a send rather than a broadcast
          connections.broadcastInGame(authToken, session, gameID, notification, true);
        }
        else {
          game.makeMove(move);
          if(game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)){
            game.active = false;
          }
          gameDAO.updateGameItself(gameID, game);
          var reloadGame=new LoadGameMessage(game);
          //+ stringVersion
          notification.setNotification(username + "has moved");
          connections.broadcastInGame(authToken, session, gameID, notification, false);
          connections.broadcastInGame(authToken, session, gameID, reloadGame, true);

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

    //we'll see if this works just setting the player username back to null.
    gameDAO.updateGame(new JoinGame(color, gameID), null, color);
    notification.setNotification(username + "has left the game");
    connections.broadcastInGame(authToken, session,gameID, notification, false);
  }

  private void resignGame(String authToken, Session session, Integer gameID) throws IOException{
    //the game is marked as over and no one can make any moves now.
    String username = authDAO.getAuth(authToken).username();
    //get game from db
    //set the active to false
    // put back to db
    //do same sort of thing with checkmate
    //also don't let observers resign

    endedGameIds.add(gameID);

    var notification = new NotificationMessage();
    notification.setNotification(username + "has resigned the game");
    connections.broadcastInGame(authToken, session, gameID, notification, true);
  }


  private String getTeamColor(String authToken, Integer gameId){
    GameData game = gameDAO.getGame(new JoinGame(null, gameId));
    String username = authDAO.getAuth(authToken).username();
    if(username.equals(game.whiteUsername())){
      return "white";
    }
    else{
      return "black";
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

  private Boolean isObserver(String authToken, Integer gameId){
    GameData gameData =  gameDAO.getGame(new JoinGame(null , gameId));
    String username = authDAO.getAuth(authToken).username();
    if(!Objects.equals(gameData.blackUsername(), username) && !gameData.whiteUsername().equals(username)){
      return true;
    }
    else{
      return false;
    }
  }


}
