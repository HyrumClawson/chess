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
        spark.Spark.post("/session", (request, response)-> "Hello World");
        spark.Spark.delete("/session", (request, response)-> "Hello World");
        spark.Spark.get("/game", (request,response)-> "Hello World");
        spark.Spark.post("/game", (request, response) -> "Hello World" );
        spark.Spark.put("/game", (request, response) -> "Hello World");
        spark.Spark.delete("/db", this::deleteDBHandler);



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * eventually take all these things and put them in a separate class maybe? Got to
     * figure out the syntax to do that.
     */
    public Object deleteDBHandler(Request req, Response res) {
        try{
            authService.clearAllAuthData(AuthData);
            gameService.clearAllGameData(GameData);
            userService.clearAllUserData(UserData);
            res.status(200);
            return "";

        }
        catch (Exception e) {
            Error newError = new Error(500,"Something went wonky");
            res.status(newError.statusCode());
            res.body(new Gson().toJson(newError));
            return (new Gson().toJson(newError));
        }


    }

    public Object registrationHandler(Request req, Response res){
//        try{
            var newUser = new Gson().fromJson(req.body(), model.UserData.class);
            try{
                if(newUser.username() ==null || newUser.email() == null || newUser.password() == null ){
                    throw new Exception();
                }

            }
            catch(Exception e){
                Error newError = new Error(400,"Error: bad request");
                res.status(newError.statusCode());
                res.body(new Gson().toJson(newError));
                return (new Gson().toJson(newError));

            }

            Error userReturnValue = userService.registerUser(UserData, newUser);

            try{
                if(userReturnValue != null){
                    throw new Exception();
                }


            }
            catch(Exception e){
                res.status(userReturnValue.statusCode());
                res.body(new Gson().toJson(userReturnValue));
                return (new Gson().toJson(userReturnValue));
            }


            var newAuth = authService.AddAuthData(AuthData, newUser);
            res.status(200);
            res.body(new Gson().toJson(newAuth));
            return (new Gson().toJson(newAuth));
//        }
//        catch(Exception e){
//            res.status(400);
//            Error newError = new Error("Error: bad request");
//            res.body(new Gson().toJson(newError));
//            return (new Gson().toJson(newError));
//
//
//        }
//        catch(Exception e){
//            return "";
//        }
//        catch(Exception e){
//            return "";
//        }


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
