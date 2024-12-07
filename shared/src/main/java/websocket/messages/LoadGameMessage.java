package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
  ChessGame game;


  public LoadGameMessage(ChessGame game){
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

}