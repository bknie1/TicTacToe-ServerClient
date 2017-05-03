package tictactoefx;
//-----------------------------------------------------------------------------
import java.io.PrintStream;
/**
 * Game creates 1 Board, Board hosts 9 squares in an ArrayList.
 * Translates the interface into action. Game --> Board --> Squares.
 * Determines whether or not win/lose/draw conditions are met.
 * Intermediary between the game logic and interface.
 * May be used for later board scalability or additional game modes.
 * i.e. Board constructors for Tic Tac Toe, or Connect 4.
 * @author Brandon K.
 */
public class Game {
    public static Board board; // Global. One board per game. Comprised of squares.
    private static Boolean player_turn;
//-----------------------------------------------------------------------------
    Game() {
        Game.board = new Board(9);
        Game.player_turn = true;
        System.out.println("Starting new game.");
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
    protected int move(int sq) {
        boolean moved;
        //System.out.println("Validating on " + sq + ".");
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
        return -1; // All paths return regardless of likelihood.
    }
//-----------------------------------------------------------------------------
    /**
     * Defers to board to check the state of the game. Relays the data back
     * to the GUIController for GUI modification.
     * 0 - No end condition met. 3 in a row: False. Full: False.
     * 1 - Winner. 3 in a row: True. Full: False.
     * 2 - Draw. Full: True.
     * @param symbol Most recently used symbol.
     * @return The state of the game.
     */
    protected int game_state(char symbol) {
        int state = board.game_state(symbol);
        return state;
    }
//-----------------------------------------------------------------------------
    /**
     * Relays useful information about the game state.
     * @param ps The output destination.
     * @param state The numerical state of the game. To be translated.
     */
    private void print_state(PrintStream ps, int state) {
        switch(state) {
            case 0:
                ps.println("The game continues.");
                break;
            case 1:
                ps.println("Winner detected.");
                break;
            case 2:
                ps.println("Draw detected.");
                break;
        }
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