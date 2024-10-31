package dataaccess;

import model.AuthData;
import model.UserData;
import server.ResponseException;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO{

  public SqlAuthDAO()  {
    try {
      configureDatabase();
    }
    catch (ResponseException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAllAuth() {

  }

  @Override
  public AuthData addnewAuth(UserData user) {
    return null;
  }

  @Override
  public void deleteSingleAuth(String authToken) {

  }

  @Override
  //this could be simplified with just a simple getauth and handle it in the service
  public boolean checkMapForAuth(String authToken) {
    return false;
  }

  @Override
  //this could be
  public String getUsername(String authToken) {
    return null;
  }


  private final String[] createStatements = {
          //this is the table for the petshop
          //create one for my game data.
          //Then just go through and  have
          """
            CREATE TABLE IF NOT EXISTS  authData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256),
              PRIMARY KEY (`authToken`),
              INDEX(authToken)
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
