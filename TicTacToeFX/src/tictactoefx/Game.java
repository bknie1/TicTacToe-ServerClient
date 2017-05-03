package tictactoefx;
//-----------------------------------------------------------------------------
import java.io.PrintStream;
/**
 * @author Brandon K.
 * Game creates 1 Board, Board hosts 9 squares.
 */
public class Game {
    public static Board board; // Global. One board per game. Comprised of squares.
    private static Boolean player_turn;
//-----------------------------------------------------------------------------
    Game() {
        this.board = new Board();
        this.player_turn = true;
        System.out.println("Starting game.");
    }
//-----------------------------------------------------------------------------
    /**
 * attempt_move() will return a state to the GUIController.
 * The GUIController will react accordingly:
 *      0 == Positive 'X' move. Will change.
 *      1 == Positive 'O' move. Will change.
 *      2 == Illegal move. No change.
 * Had to get a little creative here.
 */
    public int move(int sq) {
        boolean moved;
        System.out.println("Validating on " + sq + ".");
        //try {
            if(player_turn) { moved = board.move(sq, 'O'); }
            else { moved = board.move(sq, 'X'); }
            player_turn = !player_turn; // Flips between player 1, 2.
            if(moved && !player_turn) { return 0; } // Valid 'O' move.
            if(moved && player_turn) { return 1; }  // Valid 'X' move.
            return 2;
        //}
        //catch(Exception e) {
            //throw_error(System.out, "Illegal move.");
        //}
    }
//-----------------------------------------------------------------------------
    private void detect_winner() {
        // TODO
    }
//-----------------------------------------------------------------------------
    private void throw_error(PrintStream ps, String error) {
        ps.println("Error: " + error);
    }
}