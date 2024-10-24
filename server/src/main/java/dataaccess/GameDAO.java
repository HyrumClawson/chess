package dataaccess;

import model.GameData;
import model.ListingGameData;

import java.util.ArrayList;

public interface GameDAO {
  void deleteAllGames();

  ArrayList<ListingGameData> getListOfGames();

  int addGame(GameData newGame);
}
