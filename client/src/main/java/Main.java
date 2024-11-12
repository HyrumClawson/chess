import chess.*;
import ui.PreLoginUI;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        PreLoginUI.run(serverUrl);
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
    }

    //make the repl loop class be called here. The repl class can just call the
    //actual 3 classes that determine the result of the given input.
    //go look at petshop and do something similar.
}