package ttt.server;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Board is comprised of squares.
 * Board handles the logic.
 * Board is responsible for move validation and win detection.
 * Only once instance of board exists per game.
 * If I could use pointers I would create clusters for reach row, col, diag.
 * It would be easier to determine win conditions.
 * @author Brandon K.
 */
public class Board {
    final int board_size; // Const board size.
    ArrayList<Square> squares; // A simple array would also work.
    static int turns;
//-----------------------------------------------------------------------------
    Board(final int boardsize) {
        this.board_size = boardsize;
        this.squares = new ArrayList<>();
        Board.turns = 1;
        build(); // Populates the board.
        print(System.out); // DEBUG
        System.out.println("Board built."); // DEBUG
    }
//-----------------------------------------------------------------------------
    private void build() {
        int x = 0;
        int y = 0;
        for(int i = 0; i < board_size; ++i) {
            squares.add(new Square(x, y));
            if(y != 2) { ++y; }
            else { ++x; y = 0; }
        }
    }
//-----------------------------------------------------------------------------
    /**
     * Removes all squares from the board. Used to reset the game.
     */
    protected void clear() {
        squares.removeAll(squares);
    }
//-----------------------------------------------------------------------------
    /**
     * Validates/places the logical symbol on the board.
     * @param sq The target square.
     * @param symbol The symbol to be placed.
     * @return Returns move validation status.
     */
    protected boolean move(int sq, char symbol) {
        boolean valid = false;
        Square s = squares.get(sq);
        if(!s.fixed) {// If it isn't occupied we can place.
            s.fixed = true;
            s.status = symbol;
            switch(symbol) {
                case 'X':
                    System.out.println("Placing X.");
                    
                    break;
                case 'O':
                    System.out.println("Placing O.");
                    break;
            }
            return true;
        }
        else {
            System.out.println("Illegal move.");
            return false;
        }
    }
//-----------------------------------------------------------------------------
    /**
     * |0|1|2|
     * |3|4|5|
     * |6|7|8|
     * Or, if I have the time to go back and use a modulus, you might be looking
     * at something like:
     * |1|2|3|
     * |4|5|6|
     * |7|8|9|
     * Win/lose/draw detection. Sweeps the board for final plays.
     * Could use a Magic Square? Masking with parse int?
     * Quick and dirty: Exhaustive condition tree.
     * Normally I would use pointers to cluster these in C++.
     * @param symbol The most recent play.
     * @return Returns the state of the game.
     * 0 - No end condition met. 3 in a row: False. Full: False.
     * 1 - Winner. 3 in a row: True. Full: False.
     * 2 - Draw. Full: True.
     */
    protected int game_state(char symbol) {
        char status;
        boolean win = false;
        boolean full;
        // First Row
        //System.out.println("\nRow 1 Cluster:"); // DEBUG
        for(int i = 0, w = 0; i <= 2; ++i) {
            status = squares.get(i).status;
            if(status == symbol) { ++w; }
            if(w == 3) { win = true; }
            //System.out.print(" " + i); // DEBUG
        }
        // Second Row
        //System.out.println("\nRow 2 Cluster:");
        for(int i = 3, w = 0; i <= 5; ++i) {
            status = squares.get(i).status;
            if(status == symbol) { ++w; }
            if(w == 3) { win = true; }
            //System.out.print(" " + i); // DEBUG
        }
        // Third Row
        System.out.println("\nRow 3 Cluster:");
        for(int i = 6, w = 0; i <= 8; ++i) {
            status = squares.get(i).status;
            if(status == symbol) { ++w; }
            if(w == 3) { win = true; }
            System.out.print(" " + i); // DEBUG
        }
        // First Column
        //System.out.println("\nCol 1 Cluster:"); // DEBUG
        for(int i = 0, w = 0; i <= 6; i += 3) {
            status = squares.get(i).status;
            if(status == symbol) { ++w; }
            if(w == 3) { win = true; }
            // System.out.print(" " + i); // DEBUG
        }
        // Second Column
       //System.out.println("\nCol 2 Cluster:"); // DEBUG
        for(int i = 1, w = 0; i <= 7; i += 3) {
            status = squares.get(i).status;
            if(status == symbol) { ++w; }
            if(w == 3) { win = true; }
            //System.out.print(" " + i);  // DEBUG
        }
        // Third Column
        //System.out.println("\nCol 3 Cluster:"); // DEBUG
        for(int i = 2, w = 0; i <= 8; i += 3) {
            status = squares.get(i).status;
            if(status == symbol) { ++w; }
            if(w == 3) { win = true; }
            //System.out.print(" " + i);  // DEBUG
        }
        // Left-Down Diagonal
        //System.out.println("\nDiag 1 Cluster:"); // DEBUG
        for(int i = 0, w = 0; i <= 8; i += 4) {
            status = squares.get(i).status;
            if(status == symbol) { ++w; }
            if(w == 3) { win = true; }
            //System.out.print(" " + i); // DEBUG
        }
        // Right-Down Diagonal
        //System.out.println("\nDiag 2 Cluster:"); // DEBUG
        for(int i = 2, w = 0; i <= 6; i += 2) {
            status = squares.get(i).status;
            if(status == symbol) { ++w; }
            if(w == 3) { win = true; }
            //System.out.print(" " + i); // DEBUG
        }
        //---------------------------------------
        full = fill_state(); // Checks board fill state.
        
        // 0 - No end condition met. 3 in a row: False. Full: False.
        // 1 - Winner. 3 in a row: True. Full: False.
        // 2 - Draw. Full: True.
        if(win == false && full == false) { return 0; }
        else if(win == true) { return 1; }
        else { return 2; }
    }
//-----------------------------------------------------------------------------
    /**
     * Checks to see if the board has been filled with moves.
     * @return 
     */
    private boolean fill_state() {
        boolean fill_state = true;
        for(Square square : squares) {
            // System.out.println(square.status); // DEBUG
            if(square.status == 'E') { fill_state = false; }
        }
        //System.out.println("\nFull: " + fill_state); // DEBUG
        return fill_state;
    }
//-----------------------------------------------------------------------------
    /**
     * Prints the board. Calls each Square print().
     * @param ps Print destination.
     */
    private void print(PrintStream ps) {
        int i = 0;
        for (Square sq: squares) {
            ps.print("Square " + i + ": ");
            sq.print(System.out);
            ++i;
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