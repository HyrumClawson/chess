package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.ValueSource;
import spark.Request;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteDBTests {
  AuthService a = new AuthService();
  GameService g = new GameService();
  UserService u = new UserService();

  UserDAO userDataAccess = new MemoryUserDAO();

  private final service.Service service = new service.Service(a,g,u);



  @Test
  public void PositiveDeleteTest() {
    try {
      service.clearDataBase();
    }
    catch(DataAccessException e){

    }
  }

//  @Test
//  public void PositiveRegisterTest(){
//    model.UserData newUser = new UserData("Hyrum", "53628384", "hc@mail");
//    assertEquals(null,  u.registerUser(userDataAccess, newUser ));
//    //u.registerUser(userDataAccess, newUser );
//  }
//  public void AlreadyTakenRegisterTest(){
//    model.UserData newUser = new UserData("Hyrum", "53628384", "hc@mail");
//    u.registerUser(userDataAccess, newUser );
//    assertEquals(403, u.registerUser(userDataAccess, newUser).statusCode());
//    assertEquals("Already taken", u.registerUser(userDataAccess, newUser).message());
//  }


}
