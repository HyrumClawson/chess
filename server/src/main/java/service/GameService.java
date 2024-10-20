package service;

import dataaccess.DataAccessException;

public class GameService {
  dataaccess.GameDAO gameDataAccess = new dataaccess.MemoryGameDAO();
  public void clearAllGameData() throws DataAccessException {
    gameDataAccess.deleteAllGames();
  }
}
