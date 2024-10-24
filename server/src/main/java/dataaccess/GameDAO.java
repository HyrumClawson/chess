package dataaccess;

import model.GameData;

public interface GameDAO {
  void deleteAllGames();

  GameData[] getListOfGames();
}
