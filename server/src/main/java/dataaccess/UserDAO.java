package dataaccess;

public interface UserDAO {
  void deleteAllUsers();
  void addUser(model.UserData newUser);

  Object getUser(model.UserData user);
}
