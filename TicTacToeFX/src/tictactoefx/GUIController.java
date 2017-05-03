package tictactoefx;
//-----------------------------------------------------------------------------
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;
import static tictactoefx.TicTacToeFX.game;
//-----------------------------------------------------------------------------
/**
 * FXML Controller class
 * @author Brandon K.
 */
public class GUIController implements Initializable {
    @FXML
    private MenuItem mi_connect;
    @FXML
    private MenuItem mi_quit;
    @FXML
    private Pane sq_0_0;
    @FXML
    private ImageView img_0_0;
    @FXML
    private Pane sq_0_1;
    @FXML
    private ImageView img_0_1;
    @FXML
    private Pane sq_0_2;
    @FXML
    private ImageView img_0_2;
    @FXML
    private Pane sq_1_0;
    @FXML
    private ImageView img_1_0;
    @FXML
    private Pane sq_1_1;
    @FXML
    private ImageView img_1_1;
    @FXML
    private Pane sq_1_2;
    @FXML
    private ImageView img_1_2;
    @FXML
    private Pane sq_2_0;
    @FXML
    private ImageView img_2_0;
    @FXML
    private Pane sq_2_1;
    @FXML
    private ImageView img_2_1;
    @FXML
    private Pane sq_2_2;
    @FXML
    private ImageView img_2_2;
//-----------------------------------------------------------------------------
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("GUI created.");
    }    
//-----------------------------------------------------------------------------
    /**
     * Click --> Source --> move attempt.
     */
    @FXML
    private void click_square(MouseEvent event) {

        System.out.println("Mouse pressed.");
        //try {
            int move_state = 2;
            int square = 0;
            String source = event.getSource().toString(); // Click location.
            //String[] sq = pressed.split("\\s*(img)/g"); // Isolating var. name.
            switch(source) {
                case("ImageView[id=img_0_0, styleClass=image-view]"):
                    move(0);
                    break;
                case("ImageView[id=img_0_1, styleClass=image-view]"):
                    move(1);
                    break;
                case("ImageView[id=img_0_2, styleClass=image-view]"):
                    move(2);
                    break;
                case("ImageView[id=img_1_0, styleClass=image-view]"):
                    move(3);
                    break;
                case("ImageView[id=img_1_1, styleClass=image-view]"):
                    move(4);
                    break;
                case("ImageView[id=img_1_2, styleClass=image-view]"):
                    move(5);
                    break;
                case("ImageView[id=img_2_0, styleClass=image-view]"):
                    move(6);
                    break;
                case("ImageView[id=img_2_1, styleClass=image-view]"):
                    move(7);
                    break;
                case("ImageView[id=img_2_2, styleClass=image-view]"):
                    move(8);
                    break;
                default :
                    System.out.println("Error: Invalid square.");
                    break;
            }
        //}
//        catch(Exception e) {
//            System.out.println("Error: Invalid move.");  
//        }
    }
//-----------------------------------------------------------------------------
    /**
     * Defers to game to check the validity of the move before making changes.
     */
    private void move(int square) {
        int move_state = 2;
        move_state = game.move(square);
        if(move_state != 2) {
            switch(move_state) {
                case 0 :
                    System.out.println("Changing image to 'O'.");
                    set_symbol(square, 'O');
                    break;
                case 1 :
                    System.out.println("Changing image to 'X'.");
                    set_symbol(square, 'X');
                    break;
            }
        }
        else { System.out.println("Image remains unchanged."); }
    }
//-----------------------------------------------------------------------------
    /**
     * Changes the appearance of the game board to reflect new moves.
     */
    private void set_symbol(int square, char symbol) {
        //try {
            ImageView current_img = null;
            if(symbol == 'O') { current_img = new ImageView("O.png"); }
            if(symbol == 'X') { current_img = new ImageView("X.png"); }
            System.out.println("Changing square symbol.");
            switch(square) {
                case 0:
                    img_0_0 = current_img;
                    break;
                case 1:
                    img_0_1 = current_img;
                    break;
                case 2:
                    img_0_2 = current_img;
                    break;
                case 3:
                    img_1_0 = current_img;
                    break;
                case 4:
                    img_1_1 = current_img;
                    break;
                case 5:
                    img_1_2 = current_img;
                    break;
                case 6:
                    img_2_0 = current_img;
                    break;
                case 7:
                    img_2_1 = current_img;
                    break;
                case 8:
                    img_2_2 = current_img;
                    break;
            }
        //} catch (Exception e) { throw_error(System.out, "Image loading."); }
    }
//-----------------------------------------------------------------------------
    @FXML
    private void quit() {
        System.out.println("Terminating application.");
        System.exit(0);
    }
//-----------------------------------------------------------------------------
    private void throw_error(PrintStream ps, String error) {
        ps.println("Error: " + error);
    }
}