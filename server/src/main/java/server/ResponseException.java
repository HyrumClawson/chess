package server;

public class ResponseException extends Exception{
  Enum typeOfException;
  String message;

  public enum ExceptionType {
   TAKEN,
    BADREQUEST,

  }

  public ResponseException(Enum type){
    this.typeOfException = type;
  }
}
