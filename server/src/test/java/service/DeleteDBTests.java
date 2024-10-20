package service;

import dataaccess.DataAccessException;
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
  private final service.Service service = new service.Service(a,g,u);



  @Test
  public void PositiveDeleteTest() {
    try {
      service.clearDataBase();
    }
    catch(DataAccessException e){

    }
  }


}
