package dataaccess;

public interface AuthDAO {
  public void deleteAllAuth();
  Object addnewAuth(model.UserData user);

  void deleteSingleAuth(String authToken);
}
