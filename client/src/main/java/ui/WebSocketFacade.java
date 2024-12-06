package ui;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import exception.ResponseException;

import javax.swing.*;
import javax.websocket.Endpoint;
import javax.websocket.Session;
import java.io.IOException;

public abstract class WebSocketFacade extends Endpoint {
  Session session;
  NotificationHandler notificationHandler;
  //have the GamePlayUI implement notification handler... this will just handle communicating
  //with the server when things need to happen.
  //or have the client do it I guess.

  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
    try{

    }
    catch(Exception e){
      // change the exceptions to whatever it needs to catch.
    }
  }




  public void redrawChessBoard(String visitorName) throws ResponseException {
    /**
     * Redraws the chess board upon the user’s request.
     */
//    try {
//      var action = new Action(Action.Type.EXIT, visitorName);
//      this.session.getBasicRemote().sendText(new Gson().toJson(action));
//      this.session.close();
//    } catch (IOException ex) {
//      throw new ResponseException(500, ex.getMessage());
//    }
  }


  public void leave(){
    /**
     * Removes the user from the game (whether they are playing or
     * observing the game). The client transitions back to the Post-Login UI.
     */
  }

  public void makeMove() throws ResponseException {
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










}
