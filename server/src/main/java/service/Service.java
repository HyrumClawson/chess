package service;

import dataaccess.DataAccessException;


public class Service {
  AuthService authServiceObject;
  GameService gameServiceObject;
  UserService userServiceObject;


  public Service (AuthService authServiceObject, GameService gameServiceObject, UserService userServiceObject){
    this.authServiceObject = authServiceObject;
    this.gameServiceObject = gameServiceObject;
    this.userServiceObject = userServiceObject;
  }
}
