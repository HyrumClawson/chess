package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;

public class GameService {
  dataaccess.GameDAO gameDataAccess = new dataaccess.MemoryGameDAO();
  public void clearAllGameData(GameDAO GameData) {
    GameData.deleteAllGames();
  }
}
