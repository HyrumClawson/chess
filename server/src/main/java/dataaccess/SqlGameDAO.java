package dataaccess;

import chess.*;
import com.google.gson.Gson;
import model.GameData;
import model.JoinGame;
import model.ListingGameData;
import exception.ResponseException;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlGameDAO implements GameDAO {

  public SqlGameDAO()  {
    try {
      configureDatabase();
    }
    catch (ResponseException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAllGames() throws ResponseException{
    String statement = "TRUNCATE TABLE gameData";
    try( var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement(statement)) {
        preparedStatement.executeUpdate();
      }
    }
    catch(SQLException |DataAccessException ex){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw r;
    }

  }

  @Override
  public ArrayList<ListingGameData> getListOfGames() {
    ArrayList<ListingGameData> listToReturn = new ArrayList<>();
    try(var conn = DatabaseManager.getConnection()){
      try( var preparedStatement =
                   conn.prepareStatement("SELECT gameID, whiteUsername," +
                           "blackUsername, gameName, gameItself" +
                           " FROM gameData")){
        try(var rs = preparedStatement.executeQuery()){
          while(rs.next()){
            int gameID = rs.getInt("gameID");
            String white = rs.getString("whiteUsername");
            String black = rs.getString("blackUsername");
            String gameName = rs.getString("gameName");
            listToReturn.add(new ListingGameData(gameID, white, black, gameName));
          }
          return listToReturn;
        }
      }
    }
    catch (SQLException | DataAccessException ex){
      return null;
    }
  }

  @Override
  public int addGame(GameData newGame) throws ResponseException{
    /**need to convert the game to a json string first, and then when we're getting it
    back we need to convert it back to a Chessgame class.*/
    ChessGame gameItself = new ChessGame();
    String jsonGameString = new Gson().toJson(gameItself);

    try (var conn=DatabaseManager.getConnection()) {
      /**might need to add some more specifications to this:*/
      var statement="INSERT INTO gameData (whiteUsername, blackUsername, gameName, gameItself)" +
              "VALUES(?,?,?,?)";
      try (var preparedStatement= conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, newGame.whiteUsername());
        preparedStatement.setString(2, newGame.blackUsername());
        preparedStatement.setString(3, newGame.gameName());
        preparedStatement.setString(4, jsonGameString);
        preparedStatement.executeUpdate();
        var result=preparedStatement.getGeneratedKeys();
        int gameID=0;
        if (result.next()) {
          gameID=result.getInt(1);
        }
        return gameID;
      }
    }
    catch(SQLException | DataAccessException ex){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw r;
        /**throw response exception here and just catch and throw all the way up*/

    }
  }

  public GameData getGame(JoinGame infoToJoin){
    GameData game = new GameData(0, "", "", "", new ChessGame());
    try(var conn = DatabaseManager.getConnection()){
      try( var preparedStatement =
                   conn.prepareStatement("SELECT gameID, whiteUsername," +
                           "blackUsername, gameName, gameItself" +
                           " FROM gameData WHERE gameID=?")){
        preparedStatement.setInt(1, infoToJoin.gameID());
        try(var rs = preparedStatement.executeQuery()){
          while(rs.next()){
            int gameID = rs.getInt("gameID");
            String white = rs.getString("whiteUsername");
            String black = rs.getString("blackUsername");
            String gameName = rs.getString("gameName");
            String jsonGameString = rs.getString("gameItself");
            ChessGame gameItselfObject = new Gson().fromJson(jsonGameString, ChessGame.class);
            game = new GameData(gameID, white, black, gameName, gameItselfObject);
          }
          if(game.gameID() == 0){
            return null;
          }
          else {
            return game;
          }
        }
      }
    }
    catch (SQLException | DataAccessException ex){
      return null;
    }

  }

  /**
   *Need to find a sneaky way to add a way to update a game here
   * "UPDATE gameData SET " + team + " = ?, gameItself = ? WHERE gameID = ?";
   * something like that would modify the statement
   * then just add a setString method after serializing the game to json
   * also got to find a way to return the actual instance of the chessgame.
   * could also just add a second function that only ever gets called by
   * the websocket "service" that passes in the updated game.
   */
  public void updateGame(JoinGame infoToJoin, String username, String team) throws ResponseException{
    String statement = "UPDATE gameData SET "+ team + " = ? WHERE gameID = ?";
    try( var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement(statement)) {
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, infoToJoin.gameID());
        preparedStatement.executeUpdate();
      }
    }
    catch(SQLException | DataAccessException ex){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw r;
    }

  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  gameData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `gameItself` MEDIUMTEXT NOT NULL,
              PRIMARY KEY (`gameID`)
              )\s
            """
  };
  private void configureDatabase() throws ResponseException, DataAccessException {
    ConfigureClass configureThis = new ConfigureClass();
    configureThis.configureSqlDatabase(createStatements);

  }
}
