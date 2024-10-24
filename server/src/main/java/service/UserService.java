package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import server.Error;
import server.ResponseException;

public class UserService {

  public void clearAllUserData(UserDAO UserData) {
    UserData.deleteAllUsers();
  }
  public void registerUser(UserDAO userData, model.UserData newUser) throws ResponseException /**BadRequest, AlreadyTaken**/ {
    if(newUser.username() == null || newUser.email() == null || newUser.password() == null) {
      throw new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    }
    if(null == findUser(userData, newUser)){
      userData.addUser(newUser);

    }
    else{
      throw new ResponseException(ResponseException.ExceptionType.TAKEN);
    }



  }

  public void loginUser(UserDAO userAccess, model.UserData userLogin) throws ResponseException {
    model.UserData userFromDB = findUser(userAccess, userLogin);
    if(userFromDB == null){
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    }
    else if (!userFromDB.password().equals(userLogin.password()) ) {
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);

    }
    else{
      // do nothing I guess.
    }

  }

  public UserData findUser(UserDAO userData, model.UserData user){
    return (UserData) userData.getUser(user);
  }


}
