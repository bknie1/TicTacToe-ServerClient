package tictactoefx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//-----------------------------------------------------------------------------
/**
 * @author Brandon K.
 */
public class TicTacToeFX extends Application {
//-----------------------------------------------------------------------------
    public static Game game = new Game();
    /**
    * Builds the GUI from the FXML file.
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
     * Arguments not applicable.
     */
    public static void main() {
        launch();
    }
//-----------------------------------------------------------------------------
}