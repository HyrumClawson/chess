package dataaccess;

import model.UserData;

public interface UserDAO {
  void deleteAllUsers();
  void addUser(model.UserData newUser);

  UserData getUser(model.UserData user);
}
