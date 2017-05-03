package tictactoefx;

import java.io.PrintStream;

/**
 * @author Brandon K.
 * Exists on the game board. Simply hosts data.
 */
//-----------------------------------------------------------------------------
public class Square {
    protected int row;
    protected int col;
    protected char status; // X, O, or E (empty).
    protected Boolean fixed; // Fixed once a move is made. Used to validate moves.
//-----------------------------------------------------------------------------
    Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.status = 'E';
        this.fixed = false;
    }
//-----------------------------------------------------------------------------
    protected void print(PrintStream ps) {
        ps.println("[" + row + "," + col + "]\t\tStatus: " + status + 
                "\tFixed: " + fixed);
    }
}