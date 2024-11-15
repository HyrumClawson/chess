package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import exception.ResponseException;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlUserDAO implements UserDAO{
  Connection conn;

  public SqlUserDAO()  {
    try {
      configureDatabase();
    }
    catch (ResponseException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public void deleteAllUsers() throws Exception{
    //might just need to make a new var conn here... we'll see.
    try( var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("TRUNCATE TABLE userData")) {
        preparedStatement.executeUpdate();
      }
    }
    catch(SQLException ex){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw r;
    }
  }

  @Override
  public void addUser(UserData newUser) throws ResponseException, DataAccessException{
    try (var conn = DatabaseManager.getConnection()) {
      //might need to add some more specifications to this:
      if(newUser.username().matches("[a-zA-Z]+") ){
        var statement = "INSERT INTO userData (username, password, email)" +
                "VALUES(?,?,?)";
        String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
        try(var preparedStatement = conn.prepareStatement(statement)){
          preparedStatement.setString(1, newUser.username());
          preparedStatement.setString(2, hashedPassword);
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
  public UserData getUser(UserData user) throws Exception {
    UserData userFromDb = new UserData("","", "");
    try(var conn = DatabaseManager.getConnection()){
      try( var preparedStatement = conn.prepareStatement("SELECT " +
              "username, password, email FROM userData WHERE username=?")){
        preparedStatement.setString(1, user.username());
        try(var rs = preparedStatement.executeQuery()){
          while(rs.next()) {
            String username=rs.getString("username");
            String hashedPass=rs.getString("password");
            String email=rs.getString("email");
            userFromDb = new UserData(username, hashedPass, email);
          }
          return userFromDb;
        }
      }
    }
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
    try ( var conn = DatabaseManager.getConnection()) {
      this.conn = conn;
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
