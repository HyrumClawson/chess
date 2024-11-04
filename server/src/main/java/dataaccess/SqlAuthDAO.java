package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.ResponseException;

import java.sql.ResultSet;
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
  public void deleteAllAuth() throws ResponseException{
    //might not work cause of the extra thing could have a branch if needed.
    String statement = "TRUNCATE TABLE authData";
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
  public AuthData addnewAuth(UserData user) {
    String authtoken;
    try (var conn=DatabaseManager.getConnection()) {
      //might need to add some more specifications to this:
        var statement="INSERT INTO authData (username, authToken)" +
                "VALUES(?,?)";
        try (var preparedStatement=conn.prepareStatement(statement)) {
          authtoken = UUID.randomUUID().toString();
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
  public void deleteSingleAuth(String authToken) throws ResponseException{
    String statement = "DELETE FROM authData WHERE authToken=?";
    int resultSet;
    try( var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement(statement)) {
        preparedStatement.setString(1, authToken);
        resultSet = preparedStatement.executeUpdate();

      }
    }
    // just seeing if this works to make it better. Come back and change if not
    catch(SQLException | DataAccessException ex){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw r;
    }
    //might need to change this a bit.
    if(resultSet == 0){
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    }

  }

  public AuthData getAuth(String authToken){
    String username = "";
    try(var conn = DatabaseManager.getConnection()){
      try( var preparedStatement =
                   conn.prepareStatement("SELECT username, authToken FROM authData WHERE authToken=?")){
        preparedStatement.setString(1, authToken);
        try(var rs = preparedStatement.executeQuery()){
          while(rs.next()){
            username = rs.getString("username");
          }
          if(username == ""){
            return null;
          }
          else {
            return new AuthData(username, authToken);
          }
          }
      }
    }
    catch (SQLException | DataAccessException ex){
      return null;
    }
  }



  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  authData (
              username VARCHAR(256) NOT NULL,
              authToken VARCHAR(256) NOT NULL,
              PRIMARY KEY (authToken)
              )\s
            """
  };

  private void configureDatabase() throws ResponseException, DataAccessException {
    ConfigureClass configure = new ConfigureClass();
    configure.configureSqlDatabase(createStatements);
  }

}
