package tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//------------------------------------------------------------------------------
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