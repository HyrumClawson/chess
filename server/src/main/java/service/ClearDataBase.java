package service;

import dataaccess.*;

public class ClearDataBase {
//  private final AuthDAO authObject;
//  private final GameDAO gameObject;
//
//  private final UserDAO userObject;

//  public ClearDataBase(AuthDAO authObject, GameDAO gameObject, UserDAO userObject){
//    this.authObject = authObject;
//    this.gameObject = gameObject;
//    this.userObject = userObject;
//
//  }

  public void clearDataBase() throws DataAccessException {
    if(Boolean.FALSE){
      throw new DataAccessException("something went wrong");
    }
    //authObject.deleteAllAuth();

    /**
     * now just need to add the code that goes to dataAccess and then deletes all the game, user
     * and auth data.
     */
  }

}
