package dataaccess;
import model.UserData;

public class MemoryUserDAO implements UserDAO{
  private String[][] listOfUsers =new String[0][0];
  public void deleteAllUsers() {
    listOfUsers = new String[0][0];
  }
}
