package tictactoe;

import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
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
    private int port = 9909;                // Designated port.
    public Socket socket;                   // Endpoint for client comms.
    public ServerSocket server_socket;      // Endpoint for server comms.
    private DataInputStream dis;            // Connection input data.
    private DataOutputStream dos;           // Connection output data.

    //LOGIC
    private String[] board = new String[9]; // A board of 9 squares.
    
    private boolean player_turn = false;    // Local player's turn.
    private boolean server = true;          // Circle serves as the default host.
    private boolean client = false;         // Has a client been accepted?
    private boolean is_winner;              // Did the local player win?
    private boolean is_loser;               // Has the opponent won?
    private boolean comm_error = false;     // Communications error flag.
    
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
        try {
            // Request Address from User
            while(true) {
                address = JOptionPane.showInputDialog(
                        "Enter IP: ");
                String p = JOptionPane.showInputDialog(
                        "Enter Port (1024 - 65535): ");
                port = Integer.parseInt(p);
                System.out.println("Entered address: " + address + ":" + port);
                if(port > 1024 && port < 65535) { break; }
                System.out.println("Invalid address. Please try again.");
            }
        } catch(HeadlessException | NumberFormatException e) { 
            System.out.println("Error: " + e);
        }
        
        // Host game if none found.
        if(!connect_to_server()) { initialize_server(); }
        
        thread = new Thread(this, "Game");
        thread.start();
    }
    //--------------------------------------------------------------------------
    /**
     * Client hosts the game if client is the first connection.
     */
    @Override
    public void run() {
        while(true) {
            //tick();
            update_board();
            
            // If not server and no accepted client, host the game!
            if(!server && !client) {
                try { server_request_listener(); }
                catch (IOException e) { e.printStackTrace(); }
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
                if(server) board[square] = "X";
                else board[square] = "O";
                
                // Update GUI
                update_board();
                
                // Check for opponent win condition.
                if(server) {
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
     * Listens for requests from potential clients.
     * Accepts client connections. Once a connection is accepted we can
     * continue playing the game.
     */
    private void server_request_listener() throws IOException {
        Socket socket = null;
        try {
            socket = server_socket.accept();
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            client = true;
            System.out.println("Client requesting connection."
                             + "\nServer accepting connection.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------
    private boolean connect_to_server() {
        try {
            socket = new Socket(address, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            client = true;
        } catch (IOException e) {
            System.out.println("Unable to connect to " + address + ":" + port
                             + "\nStarting a server and waiting for client.");
            return false;
        }
        System.out.println("Client connected to the server.");
        return true;
    }
    //--------------------------------------------------------------------------
    /**
     * User hosts the game server if a server was not found.
     * Attempts to assign the input address to the local server.
     */
    private void initialize_server() {
        try {
            System.out.println("Initializing server.");
            server_socket = new ServerSocket(port, 8, InetAddress.getByName(address));
        } catch (IOException e) {
            e.printStackTrace();
        }
        player_turn = true; // Host always plays first.
        server = false;     // Start listening for a client. We no longer need a server.
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
    /**
     * For each existing item in board:
     * Updates the board and sets colors according to player status.
     * Deliberate use of a '==' instead of Equals to avoid issues with null.
     */
    private void update_board() {
        for(int i = 0; i < board.length; ++i) {
            if(board[i] == "X") {
                if(server) TicTacToe.gui.set(i, "X", Color.RED);
                else TicTacToe.gui.set(i, "X", Color.CYAN);
            }
            if(board[i] == "O") {
                if(server) TicTacToe.gui.set(i, "O", Color.CYAN);
                else TicTacToe.gui.set(i, "O", Color.RED);
            }
            else {
                TicTacToe.gui.set(i, "-", Color.DARKGREY);
            }
        }
    }
    //--------------------------------------------------------------------------
}