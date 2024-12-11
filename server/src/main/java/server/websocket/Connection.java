package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
  public String authToken;
  public Session session;
  public String color;

  public Connection(String authToken, Session session, String color) {
    this.authToken = authToken;
    this.session = session;
    this.color = color;
  }

  public void send(String msg) throws IOException {
    session.getRemote().sendString(msg);
  }
}
