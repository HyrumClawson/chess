package ui;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
@ClientEndpoint
public  class WebSocketFacade extends Endpoint {
  public Session session;
  NotificationHandler notificationHandler;
  String authToken;
  Integer gameId;

  public WebSocketFacade(String url, NotificationHandler notificationHandler, String authToken, Integer gameId) throws ResponseException {
    this.authToken = authToken;
    this.gameId = gameId;

    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/ws");
      this.notificationHandler = notificationHandler;
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      //Notification notification = new Gson().fromJson(message, Notification.class);
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          //Notification notification = new Gson().fromJson(message, Notification.class);
          //ServerMessage messageClass;
          notificationHandler.notify(message);
        }
      });

    } catch (DeploymentException | IOException | URISyntaxException ex) {
      //fix later
      throw new ResponseException(ResponseException.ExceptionType.OTHER);
    }


  }

  
  public void connect() throws ResponseException {
    try {
      //will have to fix this up a bit...

      //come back and add this. to the sessions if this don't work.
      if(session.isOpen() ){
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
        String jsonString = new Gson().toJson(command);
        UserGameCommand command1 = new Gson().fromJson(jsonString, UserGameCommand.class);
        if(UserGameCommand.CommandType.CONNECT == command1.getCommandType()){
          System.out.println("It serialized correctly... soo");
        }
        else{
          System.out.println("It's not serializing correctly and must find new way to serialize");
        }
        //System.out.println("here's the jsonstring" + jsonString);
        //add the string back after we're done.
        session.getBasicRemote().sendText(jsonString);
      }
      else{
        System.out.println("Yo what the frick it's not doing the thing. The connection" +
                "is already closed\n");
      }
    }

    catch (IOException ex) {
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw  r;
    }
  }



  public void leave(){
    /**
     * Removes the user from the game (whether they are playing or
     * observing the game). The client transitions back to the Post-Login UI.
     */
    //this is probably the place to close the connection.
  }

  public void makeMove(ChessMove move) throws ResponseException {
    try{
      if(session.isOpen()){
        MakeMoveCommand moveCommand = new MakeMoveCommand(move, authToken, gameId);
        String jsonString = new Gson().toJson(moveCommand);
        session.getBasicRemote().sendText(jsonString);
      }
    }
    catch(Exception e){
      //come back if things get weird.
      throw new ResponseException(ResponseException.ExceptionType.OTHER);
    }
    /**
     * Allow the user to input what move they want to make. The board
     * is updated to reflect the result of the move, and the board automatically
     * updates on all clients involved in the game.
     */
//    try {
//      var action = new Action(Action.Type.ENTER, visitorName);
//      this.session.getBasicRemote().sendText(new Gson().toJson(action));
//    } catch (IOException ex) {
//      throw new ResponseException(500, ex.getMessage());
//    }
  }

  public void resign(){
    /**
     * Prompts the user to confirm they want to resign. If they do, the user
     * forfeits the game and the game is over. Does not cause the user to leave the game.
     */
  }


  @Override
  public void onClose(Session session, CloseReason closeReason) {
    System.out.println("Disconnected from server: " + closeReason.getReasonPhrase());
  }

  @Override
  public void onError(Session session, Throwable throwable) {
    throwable.printStackTrace();
  }

  // Handle incoming messages from the server
//  @Override
//  public void onMessage(Session session, String message) {
//    System.out.println("Received from server: " + message);
//  }





  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
    System.out.println("WebSocket opened: " + session.getId());

  }
}
