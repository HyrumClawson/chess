package dataaccess;
import model.AuthData;
import java.util.HashMap;

/**
 * authdata = string: authtoken, string username
 */
public class MemoryAuthDAO implements AuthDAO{
  final private HashMap<String, String> MapOfauthData = new HashMap<>();

  public void deleteAllAuth(){
    MapOfauthData.clear();
  }


}
