package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.JoinGame;
import model.ListingGameData;
import server.ResponseException;

import java.util.ArrayList;

public class GameService {
  //dataaccess.GameDAO gameDataAccess = new dataaccess.MemoryGameDAO();
  public void clearAllGameData(GameDAO GameData) {
    GameData.deleteAllGames();
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
    if(infoToJoin.playerColor() == null){
      throw new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    }
    // also have to check if gameID exists in the gamedata
    else if(!gameDataAccess.GameIDExists(infoToJoin)){
      throw new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    }
    else{
      if(gameDataAccess.playerTaken(infoToJoin)){
        throw new ResponseException(ResponseException.ExceptionType.TAKEN);
      }
      gameDataAccess.addPlayerToGame(infoToJoin, username);

    }

  }




}
