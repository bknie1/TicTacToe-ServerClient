package tictactoe;

import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javax.swing.JOptionPane;
//------------------------------------------------------------------------------
public class Game implements Runnable {

    //NETWORK
    private String address = "localhost";   // Designated IP.
    private int port;                       // Designated port.
    public Socket socket;                  // Endpoint for client comms.
    public ServerSocket server_socket;     // Endpoint for server comms.
    private DataInputStream dis;            // Connection input data.
    private DataOutputStream dos;           // Connection output data.

    //LOGIC
    private boolean player_turn;            // Local player's turn.
    private String[] board = new String[9]; // A board of 9 squares.
    private boolean is_server = false;      // Circle serves as the default host.
    private boolean client_accepted = false;// Has a client been accepted?
    private boolean comm_error = false;     // Communications error flag.
    private boolean is_winner;              // Did the local player win?
    private boolean is_loser;               // Has the opponent won?
    private int error_count = 0;            // >= 10 errors? Terminate game.

    private Thread thread;
    //--------------------------------------------------------------------------
    /**
     * 1. Asks the user to enter a valid IP configuration.
     * 2. Detects whether or not there is an existing host.
     *      a. If no host, become host.
     *      b. If host, connect to host and begin game.
     */
    Game() throws IOException {
        port = 9099;
        try {
            address = "127.0.0.1";
            //address = JOptionPane.showInputDialog("Enter IP: ");
            System.out.println("IP entered: " + address);
            socket = new Socket(address, port);
            System.out.println("Client sending request : " + socket.getInetAddress());
        } catch(HeadlessException | NumberFormatException e) { 
            System.out.println("Error: " + e);
        }
        TicTacToe.gui.print("Valid address configuration.");
    }
    //--------------------------------------------------------------------------
    /**
     * Client hosts the game if client is the first connection.
     */
    @Override
    public void run() {
        while(true) {
            tick();
            update_board();
            // If not server and no accepted client, host the game!
            if(!is_server && !client_accepted) {
                try {
                    server_request_listener();
                } catch (IOException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    //--------------------------------------------------------------------------
    private void tick() {
        // Terminates the game if too many errors have occurred.
        if(error_count >= 10) { comm_error = true; }
        
        // Check for a tie first to avoid conflict with a previous winner.
        if(fill_state()) { TicTacToe.gui.declare_draw(); }
        
        // If the game is still going and not your turn.
        if(!player_turn && !comm_error) {
            try {
                // Reads and places opponent move.
                int square = dis.readInt();
                if(is_server) board[square] = "X";
                else board[square] = "O";
                
                // Update GUI
                update_board();
                
                // Check for opponent win condition.
                if(is_server) {
                    if(detect_win("X")) { TicTacToe.gui.declare_winner("X"); }
                }
                else {
                    if(detect_win("O")) { TicTacToe.gui.declare_winner("O"); }
                }

                // Toggles the turn state so that the other player can move.
                player_turn = true;
                
            } catch(IOException e) {
                e.printStackTrace();
                ++error_count;
            }
        }
    }
    //--------------------------------------------------------------------------
    /**
     * For each existing item in board:
     * Updates the board and sets colors according to player status.
     * Deliberate use of a '==' instead of Equals to avoid issues with null.
     */
    private void update_board() {
        for(int i = 0; i < board.length; ++i) {
            if(board[i] == "X") {
                if(is_server) TicTacToe.gui.set(i, "X", Color.RED);
                else TicTacToe.gui.set(i, "X", Color.CYAN);
            }
            if(board[i] == "O") {
                if(is_server) TicTacToe.gui.set(i, "O", Color.CYAN);
                else TicTacToe.gui.set(i, "O", Color.RED);
            }
            else {
                TicTacToe.gui.set(i, "-", Color.DARKGREY);
            }
        }
    }
    //--------------------------------------------------------------------------
    /**
     * Client becomes the game host (server).
     * Server searches for incoming connection requests.
     * The server socket will hang until a connection is made.
     */
    private void server_request_listener() throws IOException {
        TicTacToe.gui.print("No game detected. Hosting game.");
        try {
            System.out.println("Server is listening on port ================ 9090");
            server_socket = new ServerSocket(port);
            socket = server_socket.accept(); // Blocks until something returns.
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            client_accepted = true;
            is_server = true;
            TicTacToe.gui.print("Client detected.");
        } catch(IOException e) { e.printStackTrace(); }
    }
    //--------------------------------------------------------------------------
    /**
     * Deliberate use of '==' over Equals to avoid issues with null values.
     * @param symbol
     * @return 
     */
    private boolean detect_win(String symbol) {
        return
            (board[0] == symbol && board[0] == board[1] && board[0] == board[2])
            ||(board[3] == symbol && board[3] == board[4] && board[3] == board[5])
            ||(board[6] == symbol && board[6] == board[7] && board[6] == board[8])
            ||(board[0] == symbol && board[0] == board[3] && board[0] == board[6])
            ||(board[1] == symbol && board[1] == board[4] && board[1] == board[7])
            ||(board[2] == symbol && board[2] == board[5] && board[2] == board[8])
            ||(board[0] == symbol && board[0] == board[4] && board[0] == board[8])
            ||(board[2] == symbol && board[2] == board[4] && board[2] == board[6]);
    }
    //--------------------------------------------------------------------------
    /**
     * Determines whether or not the board is full.
     * If there's at least one empty square the method will return false.
     * @return Filled?
     */
    private boolean fill_state() {
        for(int i = 0; i < board.length; ++i) {
            if(board[i] == null) { return false; }
        }
        return true;
    }
    //--------------------------------------------------------------------------
}