package tictactoefx;
//-----------------------------------------------------------------------------
import java.io.PrintStream;
/**
 * Game creates 1 Board, Board hosts 9 squares in an ArrayList.
 * Translates the interface into action. Game --> Board --> Squares.
 * Determines whether or not win/lose/draw conditions are met.
 * @author Brandon K.

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
    * Translates GUI interaction into logical moves by deferring to board.
    * Game represents the physical game space.
    * The player attempts to make a move. Game defers to board to see if
    * a move is possible. Board returns a status.
    * The status is used to determine if a valid move occurred.
    * The interface may modify the GUI based on the status:
    *      0 == Positive 'X' move. Will display 'X'.
    *      1 == Positive 'O' move. Will display 'O'.
    *      2 == Illegal move. No change.
    * @param sq The target square.
    * @return Returns the state of the move for the GUI.
    */
    public int move(int sq) {
        boolean moved;
        System.out.println("Validating on " + sq + ".");
        try {
            if(player_turn) { moved = board.move(sq, 'O'); }
            else { moved = board.move(sq, 'X'); }
            player_turn = !player_turn; // Flips between player 1, 2.
            if(moved && !player_turn) { return 0; } // Valid 'O' move.
            if(moved && player_turn) { return 1; }  // Valid 'X' move.
            return 2;
        }
        catch(Exception e) {
            throw_error(System.out, "Illegal move.");
        }
        return -1; // Due diligence.
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