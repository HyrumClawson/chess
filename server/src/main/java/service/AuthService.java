package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

public class AuthService {
  public void clearAllAuthData(AuthDAO AuthData) {
    try {
      AuthData.deleteAllAuth();
    }
    catch (Exception e) {
      // do something here I guess. Throw it further up.
    }
  }
  public Object AddAuthData(AuthDAO authDB, model.UserData user){
    return authDB.addnewAuth(user);
  }


}
