package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import server.ResponseException;
import dataaccess.DatabaseManager;

public class AuthService {

  public void clearAllAuthData(AuthDAO authData) {
    try {
      authData.deleteAllAuth();
    } catch (Exception e) {
      // do something here I guess. Throw it further up.
    }
  }

  public AuthData addAuthData(AuthDAO authDB, model.UserData user) {
//    AuthData returnAuthData = authDB.addnewAuth(user);
//    if(null == returnAuthData){
//      authDB.
//    }
    return authDB.addnewAuth(user);
  }

  public void logoutAuth(AuthDAO authAccess, String authToken) throws ResponseException {
    try{
      authAccess.deleteSingleAuth(authToken);
    }
    catch(Exception ex){
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    }

//    if (!isAuthDataThere(authAccess, authToken)) {
//      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
//    } else {
//      authAccess.deleteSingleAuth(authToken);
//    }

  }

  public boolean isAuthDataThere(AuthDAO authAccess, String authToken) throws ResponseException {
    //!authAccess.checkMapForAuth(authToken)
    if (null == authAccess.getAuth(authToken)) {
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    } else {
      //I feel like this should work, but
      return true;
    }
  }

    public String getUserNameByToken(AuthDAO authAccess, String authToken) throws ResponseException {
      AuthData auth = authAccess.getAuth(authToken);
      if (auth == null) {
        throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
      }
      else {
        return auth.username();
      }
    }


  }

