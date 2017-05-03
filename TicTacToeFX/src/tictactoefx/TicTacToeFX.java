package tictactoefx;

import java.io.PrintStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//-----------------------------------------------------------------------------
/**
 * A simple, multiplayer Tic Tac Toe program in JavaFX.
 * @author Brandon K.
 */
public class TicTacToeFX extends Application {
//-----------------------------------------------------------------------------
    public static Game game = new Game();
    /**
    * Builds the GUI from the FXML file.
    * @param stage The JavaFX stage.
    */
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
     * General error handling.
     * @param ps Error print destination.
     * @param error The error itself.
     */
    private void throw_error(PrintStream ps, String error) {
        ps.println("Error: " + error);
    }
}