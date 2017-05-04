package ttt.server;

import java.io.PrintStream;

//-----------------------------------------------------------------------------
/**
 * A simple, online Tic Tac Toe program in JavaFX.
 * This is the server. It doesn't actually need the GUI JavaFX stuff.
 * @author Brandon K.
 */
public class TTTServer {
//-----------------------------------------------------------------------------
    public static Game game;
//-----------------------------------------------------------------------------
    /**
     * This is where we want to listen for connections.
     */
    public static void main(String[] args) {
        new_game();
        while (true) {
            // Let people connect?
        }
    }
//-----------------------------------------------------------------------------
    /**
     * Starts a new game.
     */
    static public void new_game() {
        game = new Game();
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