package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.GameData;
import model.JoinGame;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;


@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();
  AuthDAO authDAO;
  GameDAO gameDAO;

  public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO){
    this.authDAO = authDAO;
    this.gameDAO = gameDAO;

  }
  // error messages for Errors
  // messages fro notifications
  // the TA told me that I do indeed need to swtich the perspectives for the chessboard
  // I guess I'll figure out how to do that later.



  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {

    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    MakeMoveCommand move = new Gson().fromJson(message, MakeMoveCommand.class);
    switch (action.getCommandType()) {
      /**
       * add the cases for when a player does the following, and the respective actions
       * makes a moves
       * joins a game (under the connect subclass of the userGameCommand)
       * observes a game(also under connect)
       * Leave a game
       * resign a game.
       */
      case CONNECT -> connect(action.getAuthToken(), session, action.getGameID());
      case MAKE_MOVE -> makeMove(move.getAuthToken(), session, move.getMove(), move.getGameID());
      case LEAVE -> leaveGame(action.getAuthToken(), session, action.getGameID());
      case RESIGN -> resignGame(action.getAuthToken(), session, action.getGameID());

    }
  }

  private void connect(String authToken, Session session, Integer gameID) throws IOException {
    /**
     * need to call service to add stuff
     */
    try{
//      JoinGame join = new JoinGame()
//      gameDAO.updateGame();
    }
    catch(Exception e){

    }

    connections.addSessionToGame(authToken, gameID, session);
    ChessGame game = new ChessGame();
    NotificationMessage notification = new NotificationMessage();
    LoadGameMessage loadGame = new LoadGameMessage(game);
    connections.send(authToken, session, gameID, loadGame);
    connections.broadcastInGame(authToken,session, gameID, notification);

//    connections.addSessionToGame(gameID, );
//    connections.add();
//    var loadGame = new LoadGameMessage();
    //look at the phase 3 specs
//    connections.add(visitorName, session);
//    var message = String.format("%s is in the shop", visitorName);
//    var notification = new Notification(Notification.Type.ARRIVAL, message);
//    connections.broadcast(visitorName, notification);
  }

  private void makeMove(String authToken, Session session, ChessMove move, Integer gameID) throws IOException{
    JoinGame getGame = new JoinGame(null, gameID);
    ChessGame game = gameDAO.getGame(getGame).game();
    String username = authDAO.getAuth(authToken).username();
    String stringVersion = move.toString();

    var notification = new NotificationMessage();
    try{
      game.makeMove(move);
    }
    catch(InvalidMoveException e){
      var exceptionType = e.typeOfException;
      switch (exceptionType){
        case InvalidMoveException.ExceptionType.STARTNULL :
          notification.setNotification("Trying to move from empty square");
          connections.send(authToken, session, gameID, notification);
        case InvalidMoveException.ExceptionType.WRONGTURN:
          notification.setNotification("Not your turn");
          connections.send(authToken, session, gameID, notification);
        case InvalidMoveException.ExceptionType.INVALIDMOVE:
          notification.setNotification("Invalid move");
          connections.send(authToken, session, gameID, notification);
        default:
          notification.setNotification(username + "has moved" + stringVersion);
          connections.broadcastInGame(authToken, session, gameID, notification);
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

    var reloadGame = new LoadGameMessage(game);
  }

  private void leaveGame(String authToken, Session session, Integer gameID){
    var notification = new NotificationMessage();
  }

  private void resignGame(String authToken, Session session, Integer gameID){
    var notification = new NotificationMessage();
  }

//  private void exit(String visitorName) throws IOException {
//    connections.remove(visitorName);
//    var message = String.format("%s left the shop", visitorName);
//    var notification = new NotificationMessage();
//    connections.broadcast(visitorName, notification);
//  }

//  public void makeNoise(String petName, String sound) throws ResponseException {
//    try {
//      var message = String.format("%s says %s", petName, sound);
//      var notification = new NotificationMessage();
//      connections.broadcast("", notification);
//    } catch (Exception ex) {
//      //fix this later...
//      throw new ResponseException(ResponseException.ExceptionType.OTHER);
//    }
//  }

  private String getTeamColor(String authToken, Integer gameId){
    
    return "";
  }


}
