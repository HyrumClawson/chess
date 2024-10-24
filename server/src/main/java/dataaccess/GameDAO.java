package dataaccess;

import model.GameData;
import model.JoinGame;
import model.ListingGameData;

import java.util.ArrayList;

public interface GameDAO {
  void deleteAllGames();

  ArrayList<ListingGameData> getListOfGames();

  int addGame(GameData newGame);

  boolean gameIdExists(JoinGame infoToJoin);

  boolean playerTaken(JoinGame infoToJoin);

  void addPlayerToGame(JoinGame infoToJoin, String username);
}
