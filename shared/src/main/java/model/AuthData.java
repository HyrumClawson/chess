package model;

public record AuthData(String username, String authToken) {

  @Override
  public String username() {
    return username;
  }

  @Override
  public String authToken() {
    return authToken;
  }
}
