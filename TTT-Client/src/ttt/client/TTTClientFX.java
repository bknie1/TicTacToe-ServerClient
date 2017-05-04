package ttt.client;

import java.io.PrintStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//-----------------------------------------------------------------------------
/**
 * A simple, multiplayer Tic Tac Toe program in JavaFX. Client application.
 * Connects to a host server application.
 * The client simply displays the information and requests moves/game state to
 * update the GUI for the user.
 * @author Brandon K.
 */
public class TTTClientFX extends Application {
//-----------------------------------------------------------------------------
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe");
        stage.show();
    }
//-----------------------------------------------------------------------------
    /**
     * Defers to start() to implement the JavaFX framework.
     */
    public static void main() {
        launch();
    }
//-----------------------------------------------------------------------------
    /**
     * Connects to remote server.
     */
    static public void server_connect() {
        
    }
//-----------------------------------------------------------------------------
    /**
     * Requests a new game from the server.
     */
    static public void request_new_game() {
        // TODO
        System.out.println("Requesting new game.");
    }
//-----------------------------------------------------------------------------
    /**
     * Requests the game state from the server.
     */
    static public void request_state() {
        // TODO
        System.out.println("Requesting state.");
    }
//-----------------------------------------------------------------------------
    /**
     * Sends a move request to the server.
     * @return The validity of the move.
     */
    static public int request_move() {
        return 2; // Illegal move.
    }
//-----------------------------------------------------------------------------
    /**
     * Updates the client using server data.
     */
    static public void update() {
        
    }
//-----------------------------------------------------------------------------
    /**
     * General error handling.
     * @param ps Error print destination.
     * @param error The error itself.
     */
    private void throw_error(PrintStream ps, String error) {
        ps.println("Error: " + error);
    }
}