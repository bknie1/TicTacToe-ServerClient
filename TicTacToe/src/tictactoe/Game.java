package tictactoe;

import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javafx.scene.paint.Color;
import javax.swing.JOptionPane;
//------------------------------------------------------------------------------
/**
 * The Tic Tac Toe game logic class.
 * Responsible for creating and sustaining network connections, determining the
 * role of the client in the game, and whether or not win conditions have been
 * met.
 */
public class Game implements Runnable {

    //NETWORK
    private String address = "localhost";       // Designated IP.
    private int port = 1337;                    // Designated port.
    public Socket socket;                       // Endpoint for client comms.
    public ServerSocket server_socket;          // Endpoint for server comms.
    private DataInputStream dis;                // Connection input data.
    protected DataOutputStream dos;             // Connection output data.
    protected int error_count = 0;              // >= 10 errors? Terminate game.

    //LOGIC
    protected String[] board = new String[9];   // A board of 9 squares.
    protected boolean player_turn = false;      // Local player's turn.
    protected boolean circle = true;            // X or O.
    private boolean client = false;             // Has a client been accepted?
    private boolean winner;                     // Did the local player win?
    private boolean loser;                      // Has the opponent won?
    private boolean comm_error = false;         // Communications error flag.
    
    private Thread thread;
    //--------------------------------------------------------------------------
    /**
     * 1. Asks the user to enter a valid IP configuration.
     *      a. Alternate input method per OS. Mac OS didn't like JOptionPane.
     * 2. Detects whether or not there is an existing host.
     *      a. If no host, become host.
     *      b. If host, connect to host and begin game.
     */
    Game() throws IOException {
        try {
            // Request Address from User.
            String os = System.getProperty("os.name");
            
            // If Windows, JOptionPane prompt.
            if(os.startsWith("Windows")) {
                while(true) {
                    if(os.startsWith("Windows")) {
                        address = JOptionPane.showInputDialog(
                        "Enter IP: ");
                        String p = JOptionPane.showInputDialog(
                        "Enter Port (1024 - 65535): ");
                        port = Integer.parseInt(p);
                    }
                    // If non-Windows Platform, simple console prompt.
                    else {
                        Scanner reader = new Scanner(System.in);
                        System.out.print("Enter IP: ");
                        address = reader.next();
                        System.out.print("\nEnter Port (1024 - 65535): ");
                        port = reader.nextInt();
                    }
                    
                    System.out.println("Entered address: " + address + ":" + port);
                    if(port > 1024 && port < 65535 && !address.equals("")) { break; }
                    System.out.println("Invalid address. Please try again.");
                }
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
            tick();
            update_board();
            
            // If not server and no accepted client, host and look!
            if(!circle && !client) {
                try { server_request_listener(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
    //--------------------------------------------------------------------------
    /**
     * Processes player turns and detects for win conditions.
     * Flags for termination the game if too many errors have occurred.
     */
    private void tick() {
        // Terminates the game if too many errors have occurred.
        if(error_count >= 10) { comm_error = true; }
        
        // If the game is still going and not your turn.
        if(!player_turn && !comm_error) {
            try {
                int square = dis.readInt();
                if(circle) board[square] = "X";
                else board[square] = "O";

                // Check for opponent win condition.
                if(detect_win("X")) { 
                    TicTacToe.gui.declare_winner("X");
                    wipe_board();
                    winner = true;
                }
                if(detect_win("O")) {
                    TicTacToe.gui.declare_winner("O");
                    wipe_board();
                    loser = true;
                }
                
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
        System.out.println("Listening for server request.");
        try {
            socket = server_socket.accept(); // Supposed to wait here.
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            client = true;
            TicTacToe.gui.print("Hosting the server. Client connected.");
            System.out.println("Client requesting connection."
                             + "\nServer accepting connection.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------
    /**
     * Determines whether or not there is an existing game server.
     * If there is no game server one will be initialized in another method.
     * @return Was the user able to connect?
     */
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
        TicTacToe.gui.print("Connected to the server.");
        return true;
    }
    //--------------------------------------------------------------------------
    /**
     * User hosts the game server if a server was not found.
     * Attempts to assign the input address to the local server.
     */
    private void initialize_server() {
        try {
            System.out.println("Initializing server."); // DEBUG
            server_socket = new ServerSocket(port, 8, InetAddress.getByName(address));
            System.out.println("Server initialized."); // DEBUG
            TicTacToe.gui.print("Hosting the server. Waiting for connection.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        player_turn = true; // Host always plays first.
        circle = false;     // Host is X.
    }
    //--------------------------------------------------------------------------
    /**
     * Deliberate use of '==' over Equals to avoid issues with null values.
     * @param symbol
     * @return 
     */
    protected boolean detect_win(String symbol) {
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
    protected boolean fill_state() {
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
    protected void update_board() {
        for(int i = 0; i < board.length; ++i) {
            if(board[i] == "X") {
                TicTacToe.gui.set(i, "X", Color.RED);
            }
            if(board[i] == "O") {
                TicTacToe.gui.set(i, "O", Color.CYAN);
            }
        }
    }
    //--------------------------------------------------------------------------
    /**
     * Checks for move receipt criteria. The player should not be able to make
     * moves unless certain criteria are met. This mitigates a lot of the
     * encapsulation concerns I had earlier.
     * @return 
     */
    public boolean is_turn() {
        return(client && player_turn && !comm_error && !winner && !loser);
    }
    //--------------------------------------------------------------------------
    /**
     * Resets the board for future games.
     * This is called but not fully implemented from the consumer's POV (yet).
     */
    protected void wipe_board() {
        for(int i = 0; i < board.length; ++i) {
            board[i] = null;
        }
    }
    //--------------------------------------------------------------------------
}