package dataaccess;

import model.GameData;
import model.JoinGame;
import model.ListingGameData;
import server.ResponseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlGameDAO implements GameDAO {

  public SqlGameDAO()  {
    try {
      configureDatabase();
    } catch (ResponseException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
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
  public int addGame(GameData newGame) {
    return 0;
  }

  @Override
  //this could just be getGAme as well and then we'll return the game above.
  public boolean gameIdExists(JoinGame infoToJoin) {
    return false;
  }

  @Override
  //this could just be getGame and then we'll return the game to above
  public boolean playerTaken(JoinGame infoToJoin) {
    return false;
  }

  @Override
  public void addPlayerToGame(JoinGame infoToJoin, String username) {

  }

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
