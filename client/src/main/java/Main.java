import chess.*;
import ui.PreLoginUI;

public class Main {
    static PreLoginUI preLogin;
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        preLogin = new PreLoginUI(serverUrl);
        preLogin.run();
    }

    /**make the repl loop class be called here. The repl class can just call the
    actual 3 classes that determine the result of the given input.
    go look at petshop and do something similar. */
}