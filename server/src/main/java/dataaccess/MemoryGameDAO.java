package dataaccess;
import java.util.ArrayList;
import java.util.HashMap;

import chess.ChessGame;
import model.GameData;
import model.ListingGameData;

import java.util.Random;
import java.util.UUID;

public class MemoryGameDAO implements GameDAO{
  final private HashMap<String, String[]> MapOfGames = new HashMap<>();
//  private model.GameData[] listOfGames = new model.GameData[0];
  private ArrayList<model.GameData> listOfGames = new ArrayList<>();
  public void deleteAllGames(){
    // I don't know which data structure would be better.
    listOfGames.clear();

  }
  public ArrayList<ListingGameData> getListOfGames(){
    ArrayList<ListingGameData> listOfChangedGames = new ArrayList<>();
    for(GameData game : listOfGames){
      ListingGameData changedGame = new ListingGameData(game.gameID(), game.whiteUsername(),
      game.blackUsername(), game.gameName());
      listOfChangedGames.add(changedGame);
    }
    return listOfChangedGames;
  }

  public int addGame(GameData newGame){
    Random rand = new Random();
    int newGameID = rand.nextInt(100000); //might be a bit of an overkill

    boolean IDAlreadyExists = false;
    for (GameData game : listOfGames){
      if(newGameID == (game.gameID())){
        IDAlreadyExists = true;
        break;
      }
    }

    while (IDAlreadyExists){
      newGameID =rand.nextInt(1000000);
      for (GameData game : listOfGames){
        if(newGameID != (game.gameID())){
          IDAlreadyExists = true;
          break;
        }
        else {
          IDAlreadyExists = false;
        }
      }
    }
    ChessGame game = new ChessGame();

    model.GameData gameToBeAdded = new GameData(newGameID,
            null, null, newGame.gameName(), game);
    listOfGames.add(gameToBeAdded);
    return newGameID;
  }

}
