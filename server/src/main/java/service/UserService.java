package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;

public class UserService {
  dataaccess.UserDAO userDataAccess = new dataaccess.MemoryUserDAO();
  public void clearAllUserData() throws DataAccessException {
    userDataAccess.deleteAllUsers();
  }
}
