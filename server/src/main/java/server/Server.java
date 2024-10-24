package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.UserData;
import netscape.javascript.JSObject;
import service.*;
import service.Service;
import spark.*;

public class Server {
    AuthService authService = new AuthService();
    GameService gameService = new GameService();
    UserService userService = new UserService();
    AuthDAO AuthData = new MemoryAuthDAO();
    GameDAO GameData = new MemoryGameDAO();
    UserDAO UserData = new MemoryUserDAO();






    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        spark.Spark.post("/user",this::registrationHandler);
        spark.Spark.post("/session", this::loginHandler);
        spark.Spark.delete("/session", this::logoutHandler);
        spark.Spark.get("/game", (request,response)-> "H World");
        spark.Spark.post("/game", (request, response) -> "Hell World" ); //this is one gets called in the tests for login
        spark.Spark.put("/game", (request, response) -> "Hello World");
        spark.Spark.delete("/db", this::deleteDBHandler);
        Spark.exception(ResponseException.class, this::exceptionHandler);



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

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
                error = new Error("Error: unexpected");
                res.body(new Gson().toJson("Error: unexpected"));
        }

    }


    public Object deleteDBHandler(Request req, Response res) {
        try{
            authService.clearAllAuthData(AuthData);
            gameService.clearAllGameData(GameData);
            userService.clearAllUserData(UserData);
            res.status(200);
            return "";

        }
        catch (Exception e) {
            Error newError = new Error("Something went wonky");
            res.status(500);
            res.body(new Gson().toJson(newError));
            return (new Gson().toJson(newError));
        }


    }

    public Object registrationHandler(Request req, Response res) throws ResponseException/**ExceptionAlreadyTaken, BadRequest**/{
        var newUser=new Gson().fromJson(req.body(), model.UserData.class);
        userService.registerUser(UserData, newUser);
        var newAuth=authService.AddAuthData(AuthData, newUser);
        res.status(200);
        res.body(new Gson().toJson(newAuth));
        return (new Gson().toJson(newAuth));
    }

    public Object loginHandler(Request req, Response res) throws ResponseException {
        var userLogin = new Gson().fromJson(req.body(), model.UserData.class);
        userService.loginUser(UserData, userLogin);
        var newAuth = authService.AddAuthData(AuthData, userLogin);
        res.status(200);
        res.body(new Gson().toJson(newAuth));
        return (new Gson().toJson(newAuth));

    }
    public Object logoutHandler(Request req, Response res) throws ResponseException{
        var authToken = new Gson().fromJson(req.body(), String.class);
        authService.logoutAuth(AuthData, authToken);

        //return an empty like the clear
    }




     //Spark.post("/pet", this::addPet);
/**
    private Object addPet(Request req, Response res) throws ResponseException {
        var pet = new Gson().fromJson(req.body(), Pet.class);
        pet = service.addPet(pet);
        webSocketHandler.makeNoise(pet.name(), pet.sound());
        return new Gson().toJson(pet);
    }
 **/


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
