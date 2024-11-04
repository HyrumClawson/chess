package dataaccess;
import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ResponseException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class DataAccessTests {
  AuthDAO authAccess = new SqlAuthDAO();
  GameDAO gameAccess = new SqlGameDAO();
  UserDAO userAccess = new SqlUserDAO();

  @BeforeEach
  public void setup(){
    try{
      authAccess.deleteAllAuth();
      gameAccess.deleteAllGames();
      userAccess.deleteAllUsers();
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void deleteAllAuthTest(){
    UserData user1 = new UserData("yessir", "1203i12", "@yourmom");
    try{
      AuthData newAuth = authAccess.addnewAuth(user1);
      authAccess.deleteAllAuth();
      assertNull(authAccess.getAuth(newAuth.authToken()));
    }
    catch (Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void addNewAuthPositiveTest(){
    UserData user1 = new UserData("yessir", "1203i12", "@yourmom");
    try{
      AuthData newAuth = authAccess.addnewAuth(user1);
      AuthData authStuff = authAccess.getAuth(newAuth.authToken());
      assertEquals("yessir", authStuff.username());
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void addNewAuthNegativeTest() throws ResponseException {
    UserData user1 = new UserData("yessir", "1203i12", "@yourmom");
    UserData user2 = new UserData("yessir", "1203i12", "@yourmom");
//I changed the initial table to be unique for the username.
      AuthData newAuth = authAccess.addnewAuth(user1);
      AuthData newAuth2 = authAccess.addnewAuth(user2);
      assertEquals(newAuth2.username(), newAuth.username());

//      AuthData authStuff = authAccess.getAuth(newAuth.authToken());
//      assertEquals("yessir", authStuff.username());

//    catch(ResponseException e){
//      assertEquals(ResponseException.ExceptionType.TAKEN,
//              e.typeOfException, "Threw a Taken response exception");
//    }
  }

  @Test
  public void deleteSingleAuthPositiveTest(){
    UserData user1 = new UserData("yessir", "1203i12", "@yourmom");
    AuthData newAuth = authAccess.addnewAuth(user1);
    try {
      authAccess.deleteSingleAuth(newAuth.authToken());
      assertNull(authAccess.getAuth(newAuth.authToken()));
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void deleteSingleAuthNegativeTest() throws Exception{
    AuthData newAuth = new AuthData("yerp", "faketoken");
    try {
      authAccess.deleteSingleAuth(newAuth.authToken());
      assertNull(authAccess.getAuth(newAuth.authToken()));
    }
    catch(ResponseException e){
      assertEquals(ResponseException.ExceptionType.UNAUTHORIZED, e.typeOfException);
    }
  }

  @Test
  public void getAuthPositiveTest(){
    UserData user1 = new UserData("yessir", "1203i12", "@yourmom");
    try{
      AuthData newAuth = authAccess.addnewAuth(user1);
      assertEquals(newAuth.username(), authAccess.getAuth(newAuth.authToken()).username());
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void getAuthNegativeTest(){
    AuthData newAuth = new AuthData("fakeusername", "fakeToken");
    try{
      assertNull(authAccess.getAuth(newAuth.authToken()));
    }
    catch(Exception e){
      throw new RuntimeException();
    }

  }

  @Test
  public void deleteAllGamesTest(){
    GameData game = new GameData(0, "", "", "chessTime", new ChessGame());
    try{
      gameAccess.addGame(game);
      gameAccess.deleteAllGames();
      assertEquals(0, gameAccess.getListOfGames().size());
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void 

}
