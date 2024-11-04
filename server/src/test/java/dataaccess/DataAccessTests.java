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
  public void getListOfGamesPositive(){
    GameData game = new GameData(0, "", "", "chessTime", new ChessGame());
    try{
      gameAccess.addGame(game);
      assertEquals(1, gameAccess.getListOfGames().size());
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void getListOfGamesNegative(){
    assertEquals(0, gameAccess.getListOfGames().size());
  }

  @Test
  public void addGamePositiveTest(){
    GameData game = new GameData(0, "", "", "chessTime", new ChessGame());
    try{

      assertEquals(1, gameAccess.addGame(game));
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void addGameNegativeTest(){
    GameData game = new GameData(0, "", "", "chessTime", new ChessGame());
    try{
      gameAccess.addGame(game);
      assertEquals(2, gameAccess.addGame(game));
    }
    catch(Exception e){
      throw new RuntimeException();
    }

  }

  @Test
  public void getGamePositiveTest(){
    GameData game = new GameData(0, "", "", "chessTime", new ChessGame());
    JoinGame infoToJoin = new JoinGame("WHITE", 1);
    try{
      gameAccess.addGame(game);
      assertEquals("chessTime", gameAccess.getGame(infoToJoin).gameName());
    }
    catch (Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void getGameNegativeTest(){
    JoinGame infoToJoin = new JoinGame("WHITE", 12342);
      assertNull(gameAccess.getGame(infoToJoin));
  }

  @Test
  public void updateGamePositiveTest(){
    GameData game = new GameData(0, "", "", "chessTime", new ChessGame());
    JoinGame infoToJoin = new JoinGame("WHITE", 1);
    try{
      gameAccess.addGame(game);
      gameAccess.updateGame(infoToJoin, "Dudewhowantstoplaywhite", "whiteUsername");
      assertEquals("Dudewhowantstoplaywhite", gameAccess.getGame(infoToJoin).whiteUsername());
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }

  @Test
  public void updateGameNegativeTest(){
    GameData game = new GameData(0, "", "", "chessTime", new ChessGame());
    JoinGame infoToJoin = new JoinGame("BLACK", 1);
    try{
      gameAccess.addGame(game);
      gameAccess.updateGame(infoToJoin, "Dudewhowantstoplayblack", "blackUsername");
      assertEquals("Dudewhowantstoplayblack", gameAccess.getGame(infoToJoin).blackUsername());
    }
    catch(Exception e){
      throw new RuntimeException();
    }
  }


  @Test
  public void deleteAllUsers(){

  }









}
