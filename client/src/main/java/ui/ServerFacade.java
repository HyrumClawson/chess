package ui;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import model.*;

import Exception.ResponseException;
import org.junit.jupiter.api.function.Executable;


import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ServerFacade {
  private final String serverUrl;
  private String authToken;

  public ServerFacade(String url) {
    serverUrl = url;
  }
  /** build the server facade first apparently */
  //go review video of clientHttp;
  //if register is successful it should return a authdata object. If not it will throw a
  //response Exception usually.

  public AuthData register(UserData request) throws ResponseException {
    var path = "/user";

    AuthData authData= this.makeRequest("POST", path, request, AuthData.class);
    authToken = authData.authToken();
    return authData;
  }

  public void deleteAllData() throws ResponseException {
    var path = "/db";
    this.makeRequest("DELETE", path, null, null);
  }

  public AuthData login(UserData request) throws ResponseException{
    var path = "/session";
    AuthData authData= this.makeRequest("POST", path, request, AuthData.class);
    authToken = authData.authToken();
    return authData;
  }

  public void logout() throws ResponseException {
    var path = "/session";
    this.makeRequest("DELETE", path, null, null);
    authToken = null;
  }

  public ArrayList<ListingGameData> listGames() throws ResponseException{
    var path = "/game";
    record ListGamesResponse(ArrayList<ListingGameData> games) {}
    var response = this.makeRequest("GET", path, null, ListGamesResponse.class);
    return response.games();
  }

  public GameID addGame(GameData request) throws ResponseException {
    //might need to finagle it a bit here. Might be int
    var path = "/game";
    GameID gameIdObject = this.makeRequest("POST", path, request, GameID.class);
    return gameIdObject;

  }

  public void joinGame(JoinGame request) throws ResponseException {
    var path = "/game";
    this.makeRequest("PUT", path, request, null);
  }













  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      if(authToken != null){
        http.setRequestProperty("Authorization", authToken);
      }
      http.setRequestMethod(method);
      http.setDoOutput(true);
      writeBody(request, http);

      //set the string to be the authtoken

      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    }
    catch(IOException | URISyntaxException ex){
      ResponseException r = new ResponseException(ResponseException.ExceptionType.OTHER);
      r.setMessage(ex.getMessage());
      throw r;
    }

    catch (ResponseException ex) {
      ResponseException r = new ResponseException(ex.typeOfException);
      r.setMessage(ex.getMessage());
      throw r;
    }
  }


  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    int status = http.getResponseCode();
    String response = http.getResponseMessage();
    //System.out.println(response);
    if (!isSuccessful(status)) {
      //need to fix this
      ResponseException r;
      if(status == 400){
        r = new ResponseException(ResponseException.ExceptionType.BADREQUEST);
        r.setStatusCode(status);
        r.setMessage("failure: " + response);
      }
      else if(status == 401){
        r = new ResponseException(ResponseException.ExceptionType.UNAUTHORIZED);
        r.setStatusCode(status);
        r.setMessage("failure: " + response);
      }
      else if(status == 403){
        r = new ResponseException(ResponseException.ExceptionType.TAKEN);
        r.setStatusCode(status);
        r.setMessage("failure: " + "Already Taken");
      }
      else{
        r = new ResponseException(ResponseException.ExceptionType.OTHER);
        r.setStatusCode(status);
        r.setMessage(response);
      }

      throw r;
    }
  }


  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }

  //add a function that decides what enum to set the response exception to


  public String getAuthToken() {
    return authToken;
  }
}





