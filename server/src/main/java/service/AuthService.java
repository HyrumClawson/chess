package service;

import dataaccess.DataAccessException;

public class AuthService {
  dataaccess.AuthDAO authDataAccess = new dataaccess.MemoryAuthDAO();
  public void clearAllAuthData() throws DataAccessException {
    authDataAccess.deleteAllAuth();

  }
}
