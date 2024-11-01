package dataaccess;

import model.AuthData;

public interface AuthDAO {
  public void deleteAllAuth() throws Exception;
  AuthData addnewAuth(model.UserData user);

  void deleteSingleAuth(String authToken) throws Exception;

//  boolean checkMapForAuth(String authToken);
//  String getUsername(String authToken);

  AuthData getAuth(String authToken);


}
