package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Test;

public class ServiceTests {
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
