package dataaccess;
import java.util.ArrayList;
import java.util.HashMap;

import chess.ChessGame;
import model.GameData;
import model.JoinGame;
import model.ListingGameData;

import java.util.Random;
import java.util.UUID;
//make it not abstract if i ever need to.
public abstract class MemoryGameDAO implements GameDAO{
//  private model.GameData[] listOfGames = new model.GameData[0];
  private ArrayList<model.GameData> listOfGames = new ArrayList<>();
  public void deleteAllGames(){
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

    boolean idAlreadyExists = false;
    for (GameData game : listOfGames){
      if(newGameID == (game.gameID())){
        idAlreadyExists = true;
        break;
      }
    }

    while (idAlreadyExists){
      newGameID =rand.nextInt(1000000);
      for (GameData game : listOfGames){
        if(newGameID != (game.gameID())){
          idAlreadyExists = true;
          break;
        }
        else {
          idAlreadyExists = false;
        }
      }
    }
    ChessGame game = new ChessGame();

    model.GameData gameToBeAdded = new GameData(newGameID,
            null, null, newGame.gameName(), game);
    listOfGames.add(gameToBeAdded);
    return newGameID;
  }

  public boolean playerTaken(JoinGame infoToJoin){
    //WHITE/BLACK
    boolean wantsWhite;
    if("WHITE".equals(infoToJoin.playerColor())){
      wantsWhite = true;
    }
    else{
      wantsWhite = false;
    }

    for(GameData game: listOfGames){
      if(game.gameID() == infoToJoin.gameID()){
        if(game.whiteUsername() != null && wantsWhite){
          return true;
        }
        else if(game.blackUsername() != null && !wantsWhite){
          return true;
        }
      }
    }
    return false;
  }

  public void addPlayerToGame(JoinGame infoToJoin, String username){
    boolean isWhite;
    if("WHITE".equals(infoToJoin.playerColor())){
      isWhite = true;
    }
    else{
      isWhite = false;
    }

    for(int i = 0; i < listOfGames.size(); i++){
      if(listOfGames.get(i).gameID() == infoToJoin.gameID()){

      }
    }

    int i = 0;
    for(GameData game : listOfGames){
      if(game.gameID() == infoToJoin.gameID()){
        if(isWhite){
          GameData changedGame = new GameData(game.gameID(), username,
                  game.blackUsername(), game.gameName(), game.game());
          listOfGames.remove(i);
          listOfGames.add(i, changedGame);
          //game.whiteUsername(username);
        }
        else{
          GameData changedGame = new GameData(game.gameID(), game.whiteUsername(),
                  username, game.gameName(), game.game());
          listOfGames.remove(i);
          listOfGames.add(i, changedGame);
          //set black username with username.
        }
      }
      i++;
    }
  }
  public GameData getGame(JoinGame infoToJoin){
    return null;
  }

  public void updateGame(JoinGame infoToJoin, String username){

  }




}
