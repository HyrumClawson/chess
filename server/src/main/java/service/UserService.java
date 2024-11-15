package service;

import dataaccess.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import exception.ResponseException;

public class UserService {

  public void clearAllUserData(UserDAO userData) throws ResponseException {
//    try{
    try {
      userData.deleteAllUsers();
    } catch (Exception e) {
      throw new ResponseException(ResponseException.ExceptionType.OTHER);
    }
//    }
//    catch(Exception e){
//      //might need to change this and make a response exception from it.
//      throw new RuntimeException(e);
//    }

  }
  public void registerUser(UserDAO userData, model.UserData newUser) throws ResponseException /**BadRequest, AlreadyTaken**/ {
    if(newUser.username() == null || newUser.email() == null || newUser.password() == null) {
      throw new ResponseException(ResponseException.ExceptionType.BADREQUEST);
    }
    //if(null == findUser(userData, newUser)){
      try {
        userData.addUser(newUser);
      }
      catch (Exception e) {
        throw new ResponseException(ResponseException.ExceptionType.TAKEN);
      }
  }

  public void loginUser(UserDAO userAccess, model.UserData userLogin) throws ResponseException {
    model.UserData userFromDB;
    try{
      userFromDB = userAccess.getUser(userLogin);
    }
    catch(Exception ex){
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    }
    if("".equals(userFromDB.password())){
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    }
    if (!BCrypt.checkpw(userLogin.password(), userFromDB.password()) ){
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    }

  }

}
