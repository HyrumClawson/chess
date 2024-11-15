package exception;

public class ResponseException extends Exception{
  public Enum typeOfException;
  public String message;
  public int statusCode;

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

  public String getMessage(){
    return message;
  }

  public void setStatusCode(int statusCode){
    this.statusCode = statusCode;
  }


}
