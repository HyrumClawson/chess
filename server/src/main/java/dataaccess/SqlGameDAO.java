package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.JoinGame;
import model.ListingGameData;
import server.ResponseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

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

//  public void someMethod() throws Exception {
//    try (Connection conn=DatabaseManager.getConnection()) {
//      conn.prepareStatement("yerp");
//    }
//    catch (DataAccessException e) {
//      //for now have it throw an unauthorized, but maybe change it to some other status code.
//      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
//    }
//  }


  @Override
  public void deleteAllGames() {

  }

  @Override
  public ArrayList<ListingGameData> getListOfGames() {
    return null;
  }

  @Override
  public int addGame(GameData newGame) throws ResponseException{
    //need to convert the game to a json string first, and then when we're getting it
    //back we need to convert it back to a Chessgame class.
    ChessGame gameItself = new ChessGame();
    String jsonGameString = new Gson().toJson(gameItself);

    try (var conn=DatabaseManager.getConnection()) {
      //might need to add some more specifications to this:
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
        //throw response exception here and just catch and throw all the way up

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

//  @Override
//  //this could just be getGAme as well and then we'll return the game above.
//  public boolean gameIdExists(JoinGame infoToJoin) {
//    return false;
//  }
//
//  @Override
//  //this could just be getGame and then we'll return the game to above
//  public boolean playerTaken(JoinGame infoToJoin) {
//    return false;
//  }

//  @Override
//  public void addPlayerToGame(JoinGame infoToJoin, String username) {
//
//  }

  private int executeUpdate (String statement, Object ... params) throws ResponseException {
//    try (var conn = DatabaseManager.getConnection()) {
//      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
//        for (var i = 0; i < params.length; i++) {
//          var param = params[i];
//          if (param instanceof String p) ps.setString(i + 1, p);
//          else if (param instanceof Integer p) ps.setInt(i + 1, p);
//          else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
//          else if (param == null) ps.setNull(i + 1, NULL);
//        }
//        ps.executeUpdate();
//
//        var rs = ps.getGeneratedKeys();
//        if (rs.next()) {
//          return rs.getInt(1);
//        }
//        return 0;
//      }
//    }
//    catch (SQLException | DataAccessException  e) {
//      server.ResponseException r = new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
//      r.setMessage(String.format("unable to update database: %s, %s", statement, e.getMessage()));
//      throw r;
//      //throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
//    }

    return 0;
  }





  private final String[] createStatements = {
          //this is the table for the petshop
          //create one for my game data.
          //Then just go through and  have
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
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      //just for now.
      ResponseException r = new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
      r.setMessage("Unable to configure database: %s" + ex.getMessage());
      throw r;
      //throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
    }
  }
}
