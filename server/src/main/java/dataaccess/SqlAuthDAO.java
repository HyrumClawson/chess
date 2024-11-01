package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.ResponseException;

import java.sql.SQLException;
import java.util.UUID;

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
  public void deleteAllAuth() throws Exception{
    //might not work cause of the extra thing could have a branch if needed.
    deleteFunction("TRUNCATE TABLE authData", null);
  }

  @Override
  public AuthData addnewAuth(UserData user) {
    try (var conn=DatabaseManager.getConnection()) {
      //might need to add some more specifications to this:
        var statement="INSERT INTO authData (username, authToken)" +
                "VALUES(?,?)";
        try (var preparedStatement=conn.prepareStatement(statement)) {
          String authtoken = UUID.randomUUID().toString();
          preparedStatement.setString(1, user.username());
          preparedStatement.setString(2, authtoken);
          preparedStatement.executeUpdate();
          return new AuthData(user.username(), authtoken);
        }
    }
    catch(SQLException | DataAccessException ex){
      return null;
    }
  }

  @Override
  public void deleteSingleAuth(String authToken) throws Exception{
    deleteFunction("DELETE FROM pet WHERE id=?", authToken);

  }

  public AuthData getAuth(String authToken){
    try(var conn = DatabaseManager.getConnection()){
      try( var preparedStatement = conn.prepareStatement("SELECT " +
              "username, authToken FROM authData WHERE authToken=?")){
        preparedStatement.setString(1, authToken);
        try(var rs = preparedStatement.executeQuery()){
          String username = rs.getString("username");
          String dbAuthToken = rs.getString("password");
          return new AuthData(username, dbAuthToken);
        }
      }
    }
    catch (SQLException | DataAccessException ex){
      return null;
    }
  }



//  @Override
//  //this could be simplified with just a simple getauth and handle it in the service
//  public boolean checkMapForAuth(String authToken) {
//    return false;
//  }
//
//  @Override
//  //this could be simplified with just getauth function. Handle it in the service.
//  public String getUsername(String authToken) {
//    return null;
//  }

  private void deleteFunction(String statement, String authToken) throws Exception{
    try( var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement(statement)) {
        preparedStatement.setString(1, authToken);
        preparedStatement.executeUpdate();
      }
    }
    catch(SQLException ex){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw r;
    }
  }


  private final String[] createStatements = {
          //this is the table for the petshop
          //create one for my game data.
          //Then just go through and  have
          ////              `id` int NOT NULL AUTO_INCREMENT,
          """
            CREATE TABLE IF NOT EXISTS  authData (
              username VARCHAR(256) NOT NULL,
              authToken VARCHAR(256) NOT NULL,
              PRIMARY KEY (authToken)
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
