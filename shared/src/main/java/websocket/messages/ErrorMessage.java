package websocket.messages;

public class ErrorMessage extends ServerMessage{

  String errorMessage;
  public ErrorMessage(String message){

    super(ServerMessageType.ERROR);
    errorMessage = message;
  }

  public void setErrorMessage(String message){
    errorMessage = message;
  }

  public String getErrorMessage(){
    return errorMessage;
  }
  //add getter add setter

}
