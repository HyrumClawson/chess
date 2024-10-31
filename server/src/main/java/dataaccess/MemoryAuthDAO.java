package dataaccess;
import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

/**
 * authdata = string: authtoken, string username
 */
public class MemoryAuthDAO implements AuthDAO{

  final private HashMap<String, String> mapOfAuthData = new HashMap<>();

  public void deleteAllAuth(){
    mapOfAuthData.clear();
  }
  public AuthData addnewAuth(model.UserData user){
    // here create an authToken with the given code in the specs
    // Then create a new model.AuthData object and add that to the map.
    // then return the new object you just created.
    String authtoken = UUID.randomUUID().toString();
    model.AuthData newAuthData = new model.AuthData(user.username(), authtoken);
    mapOfAuthData.put(authtoken, user.username());

    return newAuthData;
  }
  public void deleteSingleAuth(String authToken){
    mapOfAuthData.remove(authToken);
  }

  public boolean checkMapForAuth(String authToken){
    return mapOfAuthData.containsKey(authToken);
  }

  public String getUsername(String authToken){
    return mapOfAuthData.get(authToken);
  }



}
