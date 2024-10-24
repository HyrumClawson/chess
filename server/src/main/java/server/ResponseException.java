package server;

public class ResponseException extends Exception{
  public Enum typeOfException;
  String message;

  public enum ExceptionType {
   TAKEN,
    BADREQUEST,
    UNAUTHORIZED,

  }


  public ResponseException(Enum type){

    this.typeOfException = type;
  }


}
