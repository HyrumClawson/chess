package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Test;
import server.ResponseException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceTests {
  AuthService a = new AuthService();
  GameService g = new GameService();
  UserService u = new UserService();

  UserDAO userDataAccess = new MemoryUserDAO();
  AuthDAO authDataAccess = new MemoryAuthDAO();
  GameDAO gameDataAccess = new MemoryGameDAO();

  private final service.Service service = new service.Service(a,g,u);



  @Test
  public void positiveDeleteTest() {
    try {
      g.clearAllGameData(gameDataAccess);
      a.clearAllAuthData(authDataAccess);
      u.clearAllUserData(userDataAccess);
      assertEquals(0, g.listAllGames(gameDataAccess).size());
    }
    catch (ResponseException e){
      throw new RuntimeException(e);
    }


  }

  @Test
  public void positiveRegisterTest(){
    model.UserData newUser = new model.UserData("Hyrum", "53628384", "hc@mail");
    try {
      u.registerUser(userDataAccess, newUser);
      UserData userFromDB = userDataAccess.getUser(newUser);
      assertEquals(newUser.username(), userFromDB.username());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void alreadyTakenRegisterTest(){
    model.UserData newUser = new UserData("Hyrum", "53628384", "hc@mail");
    try {
      u.registerUser(userDataAccess, newUser );
    } catch (ResponseException e) {
      assertEquals(ResponseException.ExceptionType.TAKEN,
              e.typeOfException, "Threw a Taken response exception");
    }
  }


  @Test
  public void positiveLoginTest(){

    model.UserData loginUser = new UserData("Hyrum", "53628384", "hc@mail");
    try {
      u.registerUser(userDataAccess, loginUser);
      AuthData newAuth = a.addAuthData(authDataAccess, loginUser);
      u.loginUser(userDataAccess, loginUser);
      //assertEquals(authDataAccess.getUsername(newAuth.authToken()), loginUser.username());
    } catch (ResponseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void unauthorizedLoginTest(){
    model.UserData loginUser = new UserData("Hyrum", "53628384", "hc@mail");
    try {
      u.loginUser(userDataAccess, loginUser);
    } catch (ResponseException e) {
      assertEquals(ResponseException.ExceptionType.UNAUTHORIZED,
              e.typeOfException, "Threw a Taken response exception");
    }
  }


  @Test
  public void postiveLogoutTest(){
    model.UserData loginUser = new UserData("Hyrum", "53628384", "hc@mail");
    try {
      u.registerUser(userDataAccess, loginUser);
      u.loginUser(userDataAccess, loginUser);
      AuthData newAuth = a.addAuthData(authDataAccess, loginUser);
      a.logoutAuth(authDataAccess, newAuth.authToken());
     // assertEquals(false, authDataAccess.checkMapForAuth(newAuth.authToken()));
    } catch (ResponseException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void unauthorizedLogoutTest(){
    model.UserData loginUser = new UserData("Hyrum", "53628384", "hc@mail");
    try {
      u.registerUser(userDataAccess, loginUser);
      u.loginUser(userDataAccess, loginUser);
      AuthData newAuth = a.addAuthData(authDataAccess, loginUser);
      a.logoutAuth(authDataAccess, "notthetoken");
    }
    catch (ResponseException e) {
      assertEquals(ResponseException.ExceptionType.UNAUTHORIZED,
              e.typeOfException, "Threw a un response exception");
    }
  }

  @Test
  public void positiveListGames(){
    model.GameData game = new GameData(123, "white",
            "black", "funtimes", new ChessGame());
    gameDataAccess.addGame(game);
    ArrayList<ListingGameData> list = gameDataAccess.getListOfGames();
    assertEquals(1, list.size());
  }

  @Test
  public void unAuthorizedListGames(){
    try {
      a.isAuthDataThere(authDataAccess, "not a token");
      g.listAllGames(gameDataAccess);
    } catch (ResponseException e) {
      assertEquals(ResponseException.ExceptionType.UNAUTHORIZED,
              e.typeOfException, "Threw a un response exception");
    }

  }

  @Test
  public void positiveCreateGame(){
    model.GameData game = new GameData(123, null,
            "black", "funtimes", new ChessGame());
    gameDataAccess.addGame(game);
    int gameID = gameDataAccess.getListOfGames().get(0).gameID();
    JoinGame info = new JoinGame("BLACK", gameID);
    assertTrue( gameDataAccess.gameIdExists(info));
  }

  @Test
  public void badRequestCreateGame(){
    try {
      model.GameData game = new GameData(123, "white",
              "black", null, new ChessGame());
      g.createNewGame(gameDataAccess, game);
    }
    catch (ResponseException e) {
      assertEquals(ResponseException.ExceptionType.BADREQUEST,
              e.typeOfException, "Threw a bad request exception");
    }
  }

  @Test
  public void positiveJoinGame(){
    model.GameData game = new GameData(123, null,
            "black", "funtimes", new ChessGame());
    gameDataAccess.addGame(game);
    int gameID = gameDataAccess.getListOfGames().get(0).gameID();
    JoinGame info = new JoinGame("WHITE", gameID);
    gameDataAccess.addPlayerToGame(info, "newguy");
    ListingGameData gameWeAdded = gameDataAccess.getListOfGames().get(0);
    assertEquals("newguy", gameWeAdded.whiteUsername());
  }

  @Test
  public void alreadyTakenJoinGame(){
    try {
      model.GameData game = new GameData(12345, null,
              "black", "funtimes", new ChessGame());
      gameDataAccess.addGame(game);
      int gameID = gameDataAccess.getListOfGames().get(0).gameID();
      JoinGame info = new JoinGame("BLACK", gameID);
      g.joinGame(gameDataAccess, info, "myName" );
    } catch (ResponseException e) {
        assertEquals(ResponseException.ExceptionType.TAKEN, e.typeOfException,
                "Threw a taken error");
    }
  }





}


