package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import netscape.javascript.JSObject;
import service.ClearDataBase;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        spark.Spark.post("/user", (request, response) -> "Hello World");
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
     * @param req
     * @param res
     * @return
     * @throws DataAccessException
     */
    private Object deleteDBHandler(Request req, Response res) throws DataAccessException {
        var deleteReq = new Gson().fromJson(req.body(), Object.class);
        ClearDataBase service = new ClearDataBase();
        deleteReq = service.clearDataBase();

        var object = new Gson().fromJson(req.body(), Object.class);

        return new Gson().toJson(deleteReq);
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
