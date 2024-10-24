package dataaccess;

import model.AuthData;

public interface AuthDAO {
  public void deleteAllAuth();
  AuthData addnewAuth(model.UserData user);

  void deleteSingleAuth(String authToken);

  boolean checkMapForAuth(String authToken);
  String getUsername(String authToken);
}
