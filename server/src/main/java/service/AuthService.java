package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import server.ResponseException;

public class AuthService {


  public void clearAllAuthData(AuthDAO AuthData) {
    try {
      AuthData.deleteAllAuth();
    } catch (Exception e) {
      // do something here I guess. Throw it further up.
    }
  }

  public Object AddAuthData(AuthDAO authDB, model.UserData user) {
    return authDB.addnewAuth(user);
  }

  public void logoutAuth(AuthDAO authAccess, String authToken) throws ResponseException {
    if (!isAuthDataThere(authAccess, authToken)) {
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    } else {
      authAccess.deleteSingleAuth(authToken);
    }

  }

  public boolean isAuthDataThere(AuthDAO authAccess, String authToken) throws ResponseException {
    if (!authAccess.checkMapForAuth(authToken)) {
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    } else {
      return authAccess.checkMapForAuth(authToken);
    }
  }

    public String getUserNameByToken(AuthDAO authAccess, String authToken){
      return authAccess.getUsername(authToken);
    }

  }

