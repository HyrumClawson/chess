package server;

import com.google.gson.Gson;
import dataaccess.*;
import dataaccess.DatabaseManager;
import model.GameData;
import model.JoinGame;
import model.ListingGameData;
import model.UserData;
import netscape.javascript.JSObject;
import service.*;
//import service.Service;
import spark.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Server {
    AuthService authService = new AuthService();
    GameService gameService = new GameService();
    UserService userService = new UserService();
    AuthDAO authData = new SqlAuthDAO();//MemoryAuthDAO();
    GameDAO gameData = new SqlGameDAO();//MemoryGameDAO();
    UserDAO userData = new SqlUserDAO();//MemoryUserDAO();

//    public Server(){
//        configureDatabase();
//    }
//
//    private void configureDatabase() {
//        DatabaseManager.createDatabase();
//    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");



        //put database connection here?

        // Register your endpoints and handle exceptions here.

        spark.Spark.post("/user",this::registrationHandler);
        spark.Spark.post("/session", this::loginHandler);
        spark.Spark.delete("/session", this::logoutHandler);
        spark.Spark.get("/game", this::listGames);
        spark.Spark.post("/game", this::createGame); //this is one gets called in the tests for login
        spark.Spark.put("/game", this::joinGame);
        spark.Spark.delete("/db", this::deleteDBHandler);
        Spark.exception(ResponseException.class, this::exceptionHandler);



        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * eventually take all these things and put them in a separate class maybe? Got to
     * figure out the syntax to do that.
     */
    public void exceptionHandler(ResponseException ex, Request req, Response res){

        var exceptionType = ex.typeOfException;
        Error error;
        switch(exceptionType) {
            case ResponseException.ExceptionType.BADREQUEST :
                res.status(400);
                error = new Error("Error: bad request");
                res.body(new Gson().toJson(error));
                break;
            case ResponseException.ExceptionType.TAKEN:
                res.status(403);
                error = new Error("Error: already taken");
                res.body(new Gson().toJson(error));
                break;
            case ResponseException.ExceptionType.UNAUTHORIZED:
                res.status(401);
                error = new Error("Error: unauthorized");
                res.body(new Gson().toJson(error));
                break;
            default:
                res.status(500);
                error = new Error(ex.message);
                res.body(new Gson().toJson(error));
        }

    }


    public Object deleteDBHandler(Request req, Response res) throws ResponseException{
//        try{
            authService.clearAllAuthData(authData);
            gameService.clearAllGameData(gameData);
            userService.clearAllUserData(userData);
            res.status(200);
            return "";

//        }
//        catch (Exception e) {
//            Error newError = new Error(e.getMessage());
//            res.status(500);
//            res.body(new Gson().toJson(newError));
//            return (new Gson().toJson(newError));
//        }


    }

    public Object registrationHandler(Request req, Response res) throws ResponseException/**ExceptionAlreadyTaken, BadRequest**/{
        var newUser=new Gson().fromJson(req.body(), model.UserData.class);
        userService.registerUser(userData, newUser);
        var newAuth=authService.addAuthData(authData, newUser);
        res.status(200);
        res.body(new Gson().toJson(newAuth));
        return (new Gson().toJson(newAuth));
    }

    public Object loginHandler(Request req, Response res) throws ResponseException {
        var userLogin = new Gson().fromJson(req.body(), model.UserData.class);
        //userService.loginUser(userData, userLogin);
        var newAuth = authService.addAuthData(authData, userLogin);
        res.status(200);
        res.body(new Gson().toJson(newAuth));
        return (new Gson().toJson(newAuth));

    }
    public Object logoutHandler(Request req, Response res) throws ResponseException{
        String authToken = getAuthFromHeader(req);
        authService.logoutAuth(authData, authToken);
        res.status(200);
        return "";

        //return an empty like the clear
    }

    public Object listGames(Request req, Response res) throws ResponseException{
        String authToken = getAuthFromHeader(req);
        authService.isAuthDataThere(authData, authToken);
        ArrayList<ListingGameData> list = gameService.listAllGames(gameData);
        //created the object that holds the list.
        GamesList listObject = new GamesList(list);
        res.status(200);
        res.body(new Gson().toJson(listObject));
        return (new Gson().toJson(listObject));
    }

    public Object createGame(Request req, Response res) throws ResponseException{
        String authToken = getAuthFromHeader(req);
        model.GameData newGame = new Gson().fromJson(req.body(), model.GameData.class);
        //I'm waffling back and forth on whether I should I have it check for a bad
        //request first or after the authorization. Maybe change it later.
        authService.isAuthDataThere(authData, authToken);
        int gameId = gameService.createNewGame(gameData, newGame);
        GameID gameIDObject = new GameID(gameId);
        res.status(200);
        res.body(new Gson().toJson(gameIDObject));
        return new Gson().toJson(gameIDObject);

    }

    public Object joinGame(Request req, Response res) throws ResponseException{
        String authToken = getAuthFromHeader(req);
        String username = authService.getUserNameByToken(authData, authToken);
        model.JoinGame infoToJoin = new Gson().fromJson(req.body(), JoinGame.class);
        authService.isAuthDataThere(authData, authToken);
        gameService.joinGame(gameData, infoToJoin, username);
        res.status(200);
        return "";

    }


    public String getAuthFromHeader(Request req){
        String authToken = "";
        for (String headerName : req.headers()) {
            if(headerName.equals("Authorization")){
                authToken = req.headers(headerName);
            }
        }
        return authToken;
    }




    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
