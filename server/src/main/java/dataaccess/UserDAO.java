package dataaccess;

import model.UserData;

public interface UserDAO {
  void deleteAllUsers() throws Exception;
  void addUser(model.UserData newUser) throws Exception;

  UserData getUser(model.UserData user);
}
