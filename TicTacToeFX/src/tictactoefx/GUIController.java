package tictactoefx;
//-----------------------------------------------------------------------------
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import static tictactoefx.TicTacToeFX.game;
//-----------------------------------------------------------------------------
/**
 * FXML Controller class
 * @author Brandon K.
 * Strictly deals with user interaction with the game GUI.
 * Handles small negligible program actions like quitting.
 * Defers to game when valid input is detected.
 */
public class GUIController implements Initializable {
    @FXML
    private MenuItem miconnect;
    @FXML
    private MenuItem miquit;
    @FXML
    private Text t00;
    @FXML
    private Text t01;
    @FXML
    private Text t02;
    @FXML
    private Text t10;
    @FXML
    private Text t11;
    @FXML
    private Text t12;
    @FXML
    private Text t20;
    @FXML
    private Text t21;
    @FXML
    private Text t22;
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
     * Uses a simple regex to isolate the clicked element id.
     * Mostly as a proof of concept and to make the switch case pretty.
     */
    @FXML
    private void click_square(MouseEvent event) {
        try {
            // Element ID regex: (t[\\d]\\w)
            String source = event.getSource().toString();
            Pattern patt = Pattern.compile("(t[\\d]\\w)");
            Matcher matcher = patt.matcher(source);
            if (matcher.find()) { source = matcher.group(); }
            else { source = ""; }
            System.out.println("Mouse pressed. Source: " + source);

            int move_state = 2;
            int square = 0;
            switch(source) {
                case("t00"):
                    move(0);
                    break;
                case("t01"):
                    move(1);
                    break;
                case("t02"):
                    move(2);
                    break;
                case("t10"):
                    move(3);
                    break;
                case("t11"):
                    move(4);
                    break;
                case("t12"):
                    move(5);
                    break;
                case("t20"):
                    move(6);
                    break;
                case("t21"):
                    move(7);
                    break;
                case("t22"):
                    move(8);
                    break;
                default :
                    System.out.println("Error: Invalid square.");
                    break;
            }
        }
        catch(Exception e) {
            throw_error(System.out, "Invalid move."); 
        }
    }
//-----------------------------------------------------------------------------
    /**
     * Called by GUIController click_square().
     * Defers to game to check the validity of the move before making changes.
     *  * Game move() returns a state to the GUIController to make GUI changes.
     *      The GUIController will react accordingly:
     *      0 == Positive 'X' move. Will display 'X'.
     *      1 == Positive 'O' move. Will display 'O'.
     *      2 == Illegal move. No change.
     */
    private void move(int square) {
        int move_state = 2;
        move_state = game.move(square); // Game validates the move.
        if(move_state != 2) {
            switch(move_state) {        // GUI reflects changes, if any.
                case 0 :
                    System.out.println("Changing text to 'O'.");
                    set_symbol(square, 'O');
                    break;
                case 1 :
                    System.out.println("Changing text to 'X'.");
                    set_symbol(square, 'X');
                    break;
            }
        }
        else { System.out.println("Text remains unchanged."); }
    }
//-----------------------------------------------------------------------------
    /**
     * Called by GUIController move().
     * Changes the appearance of the game board to reflect new moves.
     */
    private void set_symbol(int square, char symbol) {
        //try {
            String current_sym = "-";
            if(symbol == 'O') { current_sym = "O"; }
            if(symbol == 'X') { current_sym = "X"; }
            
            switch(square) {
                case 0:
                    t00.setText(current_sym);
                    break;
                case 1:
                    t01.setText(current_sym);
                    break;
                case 2:
                    t02.setText(current_sym);
                    break;
                case 3:
                    t10.setText(current_sym);
                    break;
                case 4:
                    t11.setText(current_sym);
                    break;
                case 5:
                    t12.setText(current_sym);
                    break;
                case 6:
                    t20.setText(current_sym);
                    break;
                case 7:
                    t21.setText(current_sym);
                    break;
                case 8:
                    t22.setText(current_sym);
                    break;
            }
            System.out.println("Text changed.");
        //} catch (Exception e) { throw_error(System.out, "Image loading."); }
    }
//-----------------------------------------------------------------------------
    @FXML
    private void quit() {
        System.out.println("Terminating application.");
        System.exit(0);
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