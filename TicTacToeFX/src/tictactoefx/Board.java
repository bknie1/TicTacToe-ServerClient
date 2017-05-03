package tictactoefx;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Board is comprised of squares.
 * Board handles the logic.
 * Board is responsible for move validation and win detection.
 * Only once instance of board exists per game.
 * @author Brandon K.
 */
public class Board {
    final int board_size; // Const board size.
    ArrayList<Square> squares; // A simple array would also work.
    static int turns;
//-----------------------------------------------------------------------------
    Board() {
        this.board_size = 9;
        this.squares = new ArrayList<Square>();
        this.turns = 1;
        build(); // Populates the board.
        print(System.out);
        System.out.println("Board built.");
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
     * Validates/places the logical symbol on the board.
     * @param sq The target square.
     * @param symbol The symbol to be placed.
     * @return Returns move validation status.
     */
    protected Boolean move(int sq, char symbol) {
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
     * Win/lose/draw detection. Sweeps the board for final plays.
     */
    private void game_end() {
        
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