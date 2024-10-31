package dataaccess;

import model.UserData;

public interface UserDAO {
  void deleteAllUsers();
  void addUser(model.UserData newUser) throws Exception;

  UserData getUser(model.UserData user);
}
