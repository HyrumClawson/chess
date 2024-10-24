package dataaccess;
import java.util.HashMap;
import model.GameData;

public class MemoryGameDAO implements GameDAO{
  final private HashMap<String, String[]> MapOfGames = new HashMap<>();
  private model.GameData[] listOfGames =new model.GameData[0];
  public void deleteAllGames(){
    // I don't know which data structure would be better.
    listOfGames = new model.GameData[0];
    MapOfGames.clear();
  }
  public GameData[] getListOfGames(){
    return listOfGames;
  }

}
