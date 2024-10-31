package dataaccess;

import model.UserData;
import server.ResponseException;

import java.sql.SQLException;

public class SqlUserDAO implements UserDAO{

  public SqlUserDAO()  {
    try {
      configureDatabase();
    }
    catch (ResponseException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public void deleteAllUsers() {

  }

  @Override
  public void addUser(UserData newUser) throws Exception{
    try (var conn = DatabaseManager.getConnection()) {
      //might need to add some more specifications to this:
      if(newUser.username().matches("[a-zA-Z]+") ){
        var statement = "INSERT INTO userData (username, password, email)" +
                "VALUES(?,?,?)";
        try(var preparedStatement = conn.prepareStatement(statement)){
          preparedStatement.setString(1, newUser.username());
          preparedStatement.setString(2, newUser.password());
          preparedStatement.setString(3, newUser.email());
          preparedStatement.executeUpdate();
        }
      }

    }
    catch(SQLException ex){
      //make a new enum in the response exception. So it has a 500 status code.
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw r;
    }

  }

  @Override
  public UserData getUser(UserData user) {
    return null;
  }




  private final String[] createStatements = {
          //this is the table for the petshop
          //create one for my game data.
          //Then just go through and  have
          //              `id` int NOT NULL AUTO_INCREMENT,
          """
            CREATE TABLE IF NOT EXISTS  userData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL, 
              `email` varchar(256) NOT NULL, 
              PRIMARY KEY (`username`)
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
