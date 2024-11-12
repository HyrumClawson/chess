package dataaccess;

import Exception.ResponseException;

import java.sql.SQLException;

public class ConfigureClass {
  public void configureSqlDatabase(String[] createStatements) throws ResponseException, DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException | DataAccessException ex) {
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage("Unable to configure database: %s" + ex.getMessage());
      throw r;
    }
  }
}
