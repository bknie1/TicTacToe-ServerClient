package tictactoefx;

import java.io.PrintStream;

/**
 * @author Brandon K.
 * Exists on the game board. Simply hosts Square data.
 * Modified as moves are made.
 */
//-----------------------------------------------------------------------------
public class Square {
    protected int row;
    protected int col;
    protected char status; // X, O, or E (empty).
    protected Boolean fixed; // Fixed once a move is made. Used to validate moves.
//-----------------------------------------------------------------------------
    /**
     * Square constructor assigns base values.
     * Modifiable and empty by default.
     * @param row x coordinate, if desired. Unused in logic.
     * @param col y coordinate, if desired. Unused in logic.
     */
    Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.status = 'E';
        this.fixed = false;
    }
//-----------------------------------------------------------------------------
    /**
     * Prints the Square. Called by Board print().
     * @param ps Print destination.
     */
    protected void print(PrintStream ps) {
        ps.println("[" + row + "," + col + "]\t\tStatus: " + status + 
                "\tFixed: " + fixed);
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