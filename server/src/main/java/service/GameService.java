package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.JoinGame;
import model.ListingGameData;
import server.ResponseException;

import java.util.ArrayList;

public class GameService {
  public void clearAllGameData(GameDAO gameData) {
    gameData.deleteAllGames();
  }

  public ArrayList<ListingGameData> listAllGames(GameDAO gameDataAccess) {
    return gameDataAccess.getListOfGames();
  }

  public int createNewGame(GameDAO gameDataAccess, GameData newGame) throws ResponseException {
    if(newGame.gameName() == null){
      throw new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    }
    else{
      return gameDataAccess.addGame(newGame);
    }
  }
  public void joinGame(GameDAO gameDataAccess, JoinGame infoToJoin, String username) throws ResponseException{
    GameData game = gameDataAccess.getGame(infoToJoin);
    if(infoToJoin.playerColor() == null){
      throw new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    }
    // also have to check if gameID exists in the gamedata
    //!gameDataAccess.gameIdExists(infoToJoin)
    else if(null == gameDataAccess.getGame(infoToJoin)){
      throw new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    }
    //gameDataAccess.playerTaken(infoToJoin)
    else{
      if(playerTaken(infoToJoin, game)){
        throw new ResponseException(ResponseException.ExceptionType.TAKEN);
      }
      addPlayerToGame(gameDataAccess, infoToJoin, username);
      //gameDataAccess.addPlayerToGame(infoToJoin, username);

    }

  }

  private boolean playerTaken(JoinGame infoToJoin, GameData game){

    return true;
  }



  private void addPlayerToGame(GameDAO gameAccess, JoinGame infoToJoin, String username) throws ResponseException{
    //so we need to update specifically
    //might need to refactor this I don't know yet.
    try {
      if (infoToJoin.playerColor().equals("WHITE")) {
        gameAccess.updateGame(infoToJoin, username, "whiteUsername");
      } else {
        gameAccess.updateGame(infoToJoin, username, "blackUsername");
      }
    }
    catch(ResponseException e){
      throw e;
    }
  }

}
