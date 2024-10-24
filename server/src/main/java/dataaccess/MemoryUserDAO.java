package dataaccess;
import model.UserData;
import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO{
  private  List<UserData> listOfUsers = new ArrayList<>();
  public void deleteAllUsers() {
    listOfUsers = new ArrayList<>();
  }
  public void addUser(model.UserData newUser){
    listOfUsers.add(newUser);

    //first we check to see if there's already userdata associated with username

  }
  public UserData getUser(model.UserData user){
    for(UserData e : listOfUsers){
      if(e.username().equals(user.username())){
        return e;
        //return e.username();
        // it's somewhere here where we would throw an error, because the username
        //is already taken. And then we'd add info to it and throw it up to a higher
        //level to add more stuff and eventually catch it as
      }
    }
    return null;
  }
}
