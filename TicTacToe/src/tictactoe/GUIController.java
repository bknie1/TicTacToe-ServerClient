package tictactoe;
//-----------------------------------------------------------------------------
import java.io.IOException;
import javafx.scene.paint.Color;
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
    @FXML
    public Text t_output;
    
    Game game;
//-----------------------------------------------------------------------------
    /**
     * Initializes the controller class.
     * @param url FXML
     * @param rb FXML Resource
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    //-------------------------------------------------------------------------
    public void connect() throws IOException {
        game = new Game();
        //game.run(); // UNNECESSARY. Implements runnable will do this automatically!
    }
    /**
     * Click --> Source --> move attempt.
     * Uses a simple regex to isolate the clicked element id.
     * Mostly as a proof of concept and to make the switch case pretty.
     * Continues until game_over == true.
     */
    @FXML
    private void click_square(MouseEvent event) {
        // Element ID regex: (t[\\d]\\w)
        String source = event.getSource().toString();
        Pattern patt = Pattern.compile("(t[\\d]\\w)");
        Matcher matcher = patt.matcher(source);
        if (matcher.find()) { source = matcher.group(); }
        else { source = ""; }
        System.out.println("Mouse pressed. Source: " + source);
       if(game.is_turn()) {
            switch(source) {
                case("t00"):
                    assign_to_board(0);
                    break;
                case("t01"):
                    assign_to_board(1);
                    break;
                case("t02"):
                    assign_to_board(2);
                    break;
                case("t10"):
                    assign_to_board(3);
                    break;
                case("t11"):
                    assign_to_board(4);
                    break;
                case("t12"):
                    assign_to_board(5);
                    break;
                case("t20"):
                    assign_to_board(6);
                    break;
                case("t21"):
                    assign_to_board(7);
                    break;
                case("t22"):
                    assign_to_board(8);
                    break;
            }
        }
    }
//-----------------------------------------------------------------------------
    /**
     * I'm not happy with how heavily this is reaching into game.
     * But it seems to be the only way to use FXML.
     * @param square 
     */
    private void assign_to_board(int square) {
        // If assignment can occur...
        if(game.board[square] == null) {
            if (!game.circle) game.board[square] = "X";
            else game.board[square] = "O";
            game.player_turn = false;

            try {
                    game.dos.writeInt(square);
                    game.dos.flush();
            } catch (IOException e1) {
                    ++game.error_count;
                    e1.printStackTrace();
            }

            System.out.println("Move sent.");
            if(game.detect_win("O"));
            else if(game.detect_win("X")); { declare_winner("X"); }
            if(game.fill_state()) { declare_draw(); }
        }
    }
//-----------------------------------------------------------------------------
    /**
     * Called by GUIController move().
     * Changes the appearance of the game board to reflect new moves.
     * @param square
     * @param symbol
     * @param color
     */
    public void set(int square, String symbol, Color color) {
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
    }
//-----------------------------------------------------------------------------
    public void declare_winner(String symbol) {
        int white_queen = 0x2655;
        clear_squares();
        switch(symbol) {
            case "O" :
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
            case "X" :
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
    public void declare_draw() {
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
    public void clear_squares() {
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
    private void quit() {
        System.out.println("Terminating application.");
        System.exit(0);
    }
    //-------------------------------------------------------------------------
    public void print(String output) {
        t_output.setText(output);
    }
}