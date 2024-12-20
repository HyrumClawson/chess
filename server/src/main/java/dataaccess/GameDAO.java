package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.JoinGame;
import model.ListingGameData;
import exception.ResponseException;

import java.util.ArrayList;

public interface GameDAO {
  void deleteAllGames() throws ResponseException;

  ArrayList<ListingGameData> getListOfGames();

  int addGame(GameData newGame) throws ResponseException;

  GameData getGame(JoinGame infoToJoin);

  void updateGame(JoinGame infoToJoin, String username, String team) throws ResponseException;

  void updateGameItself(Integer gameID, ChessGame game) throws ResponseException;

}
