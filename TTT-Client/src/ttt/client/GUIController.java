package ttt.client;
//-----------------------------------------------------------------------------
import javafx.scene.paint.Color;
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
    private MenuItem mi_connect;
    @FXML
    private MenuItem mi_quit;
    @FXML
    private MenuItem mi_reset;
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
    private boolean game_over;
//-----------------------------------------------------------------------------
    /**
     * Initializes the controller class.
     * @param url FXML
     * @param rb FXML Resource
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        game_over = false;
        //System.out.println("GUI created.");
    }    
//-----------------------------------------------------------------------------
    /**
     * Click --> Source --> move attempt.
     * Uses a simple regex to isolate the clicked element id.
     * Mostly as a proof of concept and to make the switch case pretty.
     * Continues until game_over == true.
     */
    @FXML
    private void click_square(MouseEvent event) {
        if(!game_over) {
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
        int move_state = TTTClientFX.request_move();
        if(move_state != 2) {
            switch(move_state) {        // GUI reflects changes, if any.
                case 0 :
                    //System.out.println("Changing text to 'O'.");
                    set_symbol(square, "O", Color.CYAN);
                    break;
                case 1 :
                    //System.out.println("Changing text to 'X'.");
                    set_symbol(square, "X", Color.RED);
                    break;
            }
            game_state(move_state); // Check for a win after a valid move.
        }
        else { System.out.println("Text remains unchanged."); }
    }
//-----------------------------------------------------------------------------
    /**
     * Called by GUIController move().
     * Changes the appearance of the game board to reflect new moves.
     */
    private void set_symbol(int square, String symbol, Color color) {
        try {
            switch(square) {
                case 0:
                    t00.setText(symbol);
                    t00.setFill(color);
                    break;
                case 1:
                    t01.setText(symbol);
                    t01.setFill(color);
                    break;
                case 2:
                    t02.setText(symbol);
                    t02.setFill(color);
                    break;
                case 3:
                    t10.setText(symbol);
                    t10.setFill(color);
                    break;
                case 4:
                    t11.setText(symbol);
                    t11.setFill(color);
                    break;
                case 5:
                    t12.setText(symbol);
                    t12.setFill(color);
                    break;
                case 6:
                    t20.setText(symbol);
                    t20.setFill(color);
                    break;
                case 7:
                    t21.setText(symbol);
                    t21.setFill(color);
                    break;
                case 8:
                    t22.setText(symbol);
                    t22.setFill(color);
                    break;
            }
            //System.out.println("Text changed.");
        } catch (Exception e) { throw_error(System.out, "Changing text."); }
    }
//-----------------------------------------------------------------------------
    /**
     * Return values are used to modify the GUI.
     */
    private void game_state(int move_state) {
        int game_state = 0;
        char symbol;
        if(move_state == 0) { symbol = 'O'; }
        else { symbol = 'X'; }
        TTTClientFX.request_state();
        
        switch(game_state) {
        case 0 : // No winner.
            // System.out.println("No winner yet!"); // Optional
            break;
        case 1 : // A winner.
            System.out.println(symbol + " wins!");
            declare_winner(symbol);
            game_over();
            break;
        case 2 : // Draw.
            System.out.println("It was a draw.");
            declare_draw();
            game_over();
            break;
        }
    }
//-----------------------------------------------------------------------------
    private void declare_winner(char symbol) {
        int white_queen = 0x2655;
        clear_squares();
        switch(symbol) {
            case 'O' :
                t01.setText(String.valueOf(Character.toChars(white_queen)));
                t01.setFill(Color.GOLD);
                t11.setText("O");
                t11.setFill(Color.CYAN);
                t20.setText("W");
                t20.setFill(Color.CYAN);
                t21.setText("I");
                t21.setFill(Color.CYAN);
                t22.setText("N");
                t22.setFill(Color.CYAN);
                break;
            case 'X' :
                t01.setText(String.valueOf(Character.toChars(white_queen)));
                t01.setFill(Color.GOLD);
                t11.setText("X");
                t11.setFill(Color.RED);
                t20.setText("W");
                t20.setFill(Color.RED);
                t21.setText("I");
                t21.setFill(Color.RED);
                t22.setText("N");
                t22.setFill(Color.RED);
                break;
        }
    }
//-----------------------------------------------------------------------------
    private void declare_draw() {
        int white_queen = 0x2655;
        int black_queen = 0x265B;
        clear_squares();
        t10.setText(String.valueOf(Character.toChars(white_queen)));
        t10.setFill(Color.WHITE);
        t11.setText("D");
        t11.setFill(Color.PURPLE);
        t12.setText(String.valueOf(Character.toChars(black_queen)));
        t12.setFill(Color.BLACK);
        t12.setStroke(Color.WHITE);
        t12.setStrokeWidth(1);
    }
//-----------------------------------------------------------------------------
    /**
     * Reacts to game end condition. Modifies GUI.
     */
    private void game_over() {
        game_over = true;
        System.out.println("The game has ended.");
    }
//-----------------------------------------------------------------------------
    @FXML
    /**
     * Menu action that defers to game to perform a reset of the current game
     * mode.
     */
    private void reset() {
        TTTClientFX.request_new_game();
        clear_squares();
        game_over = false;
    }
//-----------------------------------------------------------------------------
    private void clear_squares() {
        t00.setText("");
        t01.setText("");
        t02.setText("");
        t10.setText("");
        t11.setText("");
        t12.setText("");
        t20.setText("");
        t21.setText("");
        t22.setText("");
    }
//-----------------------------------------------------------------------------
    @FXML
    private void connect() {
        // TODO
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