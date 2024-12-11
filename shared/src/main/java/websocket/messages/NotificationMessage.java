package websocket.messages;


public class NotificationMessage extends ServerMessage{
  String message;


  public NotificationMessage(){

    super(ServerMessageType.NOTIFICATION);

  }

  public void setNotification(String notification){
    this.message = notification;
  }

  public String getMessage(){
    return "Notification: " + message;
  }

}
