import chess.*;

public class Main {
    public static void main(String[] args) {
        server.Server server = new server.Server();
        server.run(8082);
    }
}