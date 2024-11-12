package client;

import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import Exception.ResponseException;


public class ServerFacadeTests {

    private static Server server;
    ServerFacade serverFacade = new ServerFacade("http://localhost:8080");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }
    @BeforeEach
    public void setUp(){
        try {
            serverFacade.deleteAllData();
            serverFacade.logout();

        }
        catch(ResponseException e){

        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


    @Test
    public void deleteDbTest(){
        UserData user = new UserData("hyc", "123", "hyc@mail");
        try{
            serverFacade.register(user);
            serverFacade.deleteAllData();
            Assertions.assertEquals("hyc", serverFacade.register(user).username(), "didn't " +
                    "successfully delete the registered user");
        }
        catch(ResponseException e){

        }
    }

    @Test
    public void registerTestPositive(){
        UserData user = new UserData("hyc", "123", "hyc@mail");
        try{
            Assertions.assertEquals("hyc", serverFacade.register(user).username(),"correct username " +
                    "wasn't returned");
        }
        catch(ResponseException e){

        }


    }
    @Test
    public void registerTestNegative(){
        UserData user = new UserData("hyc", "123", "hyc@mail");
        try{
            serverFacade.register(user);
            //System.out.println("It worked for the first register");
            serverFacade.register(user);
            //System.out.println("It worked");
            //Assertions.assertEquals("hyc", serverFacade.register(user).username());
        }
        catch(ResponseException e){
            Assertions.assertEquals("failure: 403", e.getMessage());
        }

    }

    @Test
    public void loginTestPositive(){
        UserData user = new UserData("hyc", "123", "hyc@mail");
        try{
            serverFacade.register(user);
            Assertions.assertEquals("hyc", serverFacade.login(user).username());
        }
        catch(ResponseException e){

        }

    }

    @Test
    public void loginTestNegative(){
        UserData user = new UserData("hyc", "123", "hyc@mail");
        UserData userWithBadPass = new UserData("hyc", "1", "");
        try{
            serverFacade.register(user);
            serverFacade.login(userWithBadPass);
        }
        catch(ResponseException e){
            Assertions.assertEquals("failure: 401", e.getMessage());
        }
    }


    @Test
    public void logoutTestPositive(){
        UserData user = new UserData("hyc", "123", "hyc@mail");
        try {
            serverFacade.register(user);
            serverFacade.logout();
            Assertions.assertNull(serverFacade.getAuthToken());

        }
        catch(ResponseException e){

        }
    }

    @Test
    public void logoutTestNegative(){
        try {
            serverFacade.logout();

        }
        catch(ResponseException e){
            Assertions.assertEquals("failure: 401", e.getMessage());
        }
    }

    @Test
    public void listGamesPositive(){

    }


}
