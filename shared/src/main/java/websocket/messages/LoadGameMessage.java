package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
  ChessGame game;
  String color;


  public LoadGameMessage(ChessGame game){
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

  public void setColorOnTop(String color){
    if(color.equals("white") || color.equals("observer")){
      this.color = "black";
    }
    else{
      this.color = "white";
    }
  }

  public ChessGame getGame(){
    return game;
  }
  public String getColor(){
    return color;
  }

}