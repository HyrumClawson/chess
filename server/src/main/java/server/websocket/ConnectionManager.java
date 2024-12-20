package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.JoinGame;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
//import webSocketMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  //gameId integer is the key and then
  // go and make the connection class
  public final ConcurrentHashMap<Integer, Set<Connection>> gameConnections = new ConcurrentHashMap<>();
  private Set<Session> gamelessSessions;


  public void addSessionToGame(String authToken, Integer gameId, Session session, String color) {
    //check to see if null first.
    Connection connection = new Connection(authToken, session, color);
    if(gameConnections.get(gameId) == null){
      Set<Connection> newSet = new HashSet<>();
      newSet.add(connection);
      gameConnections.put(gameId, newSet);
    }
    else{
      Set<Connection> setOfConnections = gameConnections.get(gameId);
      setOfConnections.add(connection);
      //var connection = new Connection(visitorName, session);
      //gameConnections.put(visitorName, connection);
      gameConnections.put(gameId, setOfConnections);
    }

  }


  /**
   *maybe just pass in a connection...
   */

  public void removeSessionFromGame(String authToken, Integer gameId, Session session) {
    //gamelessSessions.add(session);
    Set<Connection> setOfConnections = gameConnections.get(gameId);
    setOfConnections.removeIf(connection -> Objects.equals(connection.authToken, authToken));
    gameConnections.put(gameId, setOfConnections);
  }

  //server only stores sessions inside the map here, technically don't have to remove

  public Set<Connection> getConnectionsForGame(Integer gameId){
    //just for now, change this soon.
    return gameConnections.get(gameId);
  }


// I might need to change the NO

  //might want to come back and change it so it just sends the
  public void broadcastInGame(String authToken, Session excludeSession, Integer gameId,
                              ServerMessage message, Boolean everyone) throws IOException {
    // if weird replace with connectio nthing
    //bruh this might kill a lot of things.
    Boolean loadGame = false;
    LoadGameMessage loadGameMessage = new LoadGameMessage(new ChessGame());
    if(message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
      loadGame = true;
      loadGameMessage = (LoadGameMessage) message;
    }
    var removeList = new ArrayList<Connection>();
    Set<Connection> setOfConnections = gameConnections.get(gameId);
    for (Connection connection : setOfConnections) {
      if (connection.session.isOpen()) {
        if(everyone){
          if(loadGame){
            loadGameMessage.setColorOnTop(connection.color);
            connection.session.getRemote().sendString(new Gson().toJson(loadGameMessage));
          }
          else{
            connection.session.getRemote().sendString(new Gson().toJson(message) );
          }

        }
        else{
          if (!connection.session.equals(excludeSession)) {
            connection.session.getRemote().sendString(new Gson().toJson(message));
          }
        }
      } else {
        removeList.add(connection);
      }
    }
  //   Clean up any connections that were left open.
    for (var c : removeList) {
      Set<Connection> connections = gameConnections.get(gameId);
      connections.remove(c);
    }
  }
  /**
   * add a send function similar to the broadcast method but that sends to one specific
   * connection

   */
  public void send(String authToken, Session session, Integer gameId, ServerMessage message) throws IOException{
    var removeList = new ArrayList<Connection>();
      Set<Connection> setOfConnections = gameConnections.get(gameId);
      for (Connection connection : setOfConnections) {
        if (connection.session.isOpen()) {
          if (connection.authToken.equals(authToken)) {
            connection.session.getRemote().sendString(new Gson().toJson(message));
          }
        } else {
          removeList.add(connection);
        }
      }
      for (var c : removeList) {
        Set<Connection> connections = gameConnections.get(gameId);
        connections.remove(c);
      }

  }

}

