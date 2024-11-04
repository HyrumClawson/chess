package server;

public class ResponseException extends Exception{
  public Enum typeOfException;
  String message;

  public enum ExceptionType {
   TAKEN,
    BADREQUEST,
    UNAUTHORIZED,

    OTHER,

  }


  public ResponseException(Enum type){

    this.typeOfException = type;
  }

  public void setMessage(String message){
    this.message = message;
  }


}
