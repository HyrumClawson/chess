package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import server.Error;

public class UserService {

  public void clearAllUserData(UserDAO UserData) {

    UserData.deleteAllUsers();
  }
  public Error registerUser(UserDAO userData, model.UserData newUser){
    try{
      if(null == findUser(userData, newUser)){
        userData.addUser(newUser);
        return null;
      }
      else{
        throw new Exception();
      }
    }
    catch(Exception e){
      return new Error(403,"Error: already taken");
    }


  }


  public Object findUser(UserDAO userData, model.UserData user){
    return userData.getUser(user);
  }
}
