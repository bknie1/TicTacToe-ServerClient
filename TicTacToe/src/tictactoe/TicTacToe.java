package tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//------------------------------------------------------------------------------
/**
 * MVC Tic Tac Toe Server-Client implementation.
 * Classes:
 *      TicTacToe.java      - Main; Responsible for GUI construction via FXML.
 *      Game.java           - Model; responsible for the game's logic prcessing.
 *      GUIController.java  - Controller/View; the GUI interface. Mouse Events, drawing.
 */
public class TicTacToe extends Application {
    public static GUIController gui;
    //--------------------------------------------------------------------------
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
        Parent root = loader.load();
        gui = (GUIController)loader.getController();
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);
        stage.show();
    }
    //--------------------------------------------------------------------------
    public static void main(String[] args) {
        launch(args);
    }
}