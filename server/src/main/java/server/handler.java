package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Spark;

public class handler {
  public void handler(){}
  private Object deleteDBHandler(Request req, Response res) throws DataAccessException {
    var object = new Gson().fromJson(req.body(), Object.class);

    return new Gson().toJson(object);
  }

}
