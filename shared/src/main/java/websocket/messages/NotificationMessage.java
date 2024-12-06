package websocket.messages;


public class NotificationMessage extends ServerMessage{
  String notification;


  public NotificationMessage(){

    super(ServerMessageType.NOTIFICATION);

  }

  public void setNotification(String notification){
    this.notification = notification;
  }

}
