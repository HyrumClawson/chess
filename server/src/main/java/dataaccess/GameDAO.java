package dataaccess;

import model.GameData;
import model.JoinGame;
import model.ListingGameData;
import server.ResponseException;

import java.util.ArrayList;

public interface GameDAO {
  void deleteAllGames() throws ResponseException;

  ArrayList<ListingGameData> getListOfGames();

  int addGame(GameData newGame) throws ResponseException;

  GameData getGame(JoinGame infoToJoin);

  void updateGame(JoinGame infoToJoin, String username, String team) throws ResponseException;

//  boolean gameIdExists(JoinGame infoToJoin);
//
//  boolean playerTaken(JoinGame infoToJoin);
//
//  void addPlayerToGame(JoinGame infoToJoin, String username);
}
