package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import server.Error;
import server.ResponseException;

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

//    }
//    else{
//      throw new ResponseException(ResponseException.ExceptionType.TAKEN);
//    }



  }

  public void loginUser(UserDAO userAccess, model.UserData userLogin) throws ResponseException {
    model.UserData userFromDB;
    try{
      userFromDB = userAccess.getUser(userLogin);
    }
    catch(Exception ex){
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    }
    //    if(userFromDB == null){
    //      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
    //    }
    //    else
    if (!userFromDB.password().equals(userLogin.password()) ) {
      throw new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);

    }

  }

  public UserData findUser(UserDAO userData, model.UserData user) {
    //I might be screwing this all up something fierce.
    try {
      return (UserData) userData.getUser(user);
    }
    catch(Exception e){
      return null;
    }
  }


}
