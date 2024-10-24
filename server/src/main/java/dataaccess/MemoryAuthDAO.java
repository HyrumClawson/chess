package dataaccess;
import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

/**
 * authdata = string: authtoken, string username
 */
public class MemoryAuthDAO implements AuthDAO{
  final private HashMap<String, String> MapOfauthData = new HashMap<>();

  public void deleteAllAuth(){
    MapOfauthData.clear();
  }
  public Object addnewAuth(model.UserData user){
    // here create an authToken with the given code in the specs
    // Then create a new model.AuthData object and add that to the map.
    // then return the new object you just created.
    String authtoken = UUID.randomUUID().toString();
    model.AuthData newAuthData = new model.AuthData(user.username(), authtoken);
    MapOfauthData.put(authtoken, user.username());

    return newAuthData;
  }
  public void deleteSingleAuth(String authToken){
    MapOfauthData.remove(authToken);
  }

  public boolean checkMapForAuth(String authToken){
    return MapOfauthData.containsKey(authToken);
  }


}
