import java.io.*;
import java.net.*;

public class ServerThread extends Thread
{
    private int playerNum;
    private BufferedReader in;  //for receiving messages from this player's client
    private PrintWriter out;  //for sending messages to this player's client
    private Server server;  //for sending messages to opponent

    public ServerThread(Socket socket, Server server, int playerNum) throws IOException
    {
        this.server = server;
        this.playerNum = playerNum;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        start();
        //the run method has started
    }

    //keep receiving messages from this player's client
    public void run()
    {
        try
        {
            while (true)
            {
                String message = in.readLine();
                System.out.println("ServerThread " + playerNum + " received: " + message);
                
                //convert message string into array of tokens (originally separated by spaces)
                String[] tokens = message.split(" ");
                
                if (tokens[0].equals("dip"))
                    server.sendToOpponent("dip", playerNum);
                else if (tokens[0].equals("boom"))
                    server.sendToOpponent("boom", playerNum);
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //send message to this player's client
    public void send(String message)
    {
        System.out.println("ServerThread " + playerNum + " sending: " + message);
        out.println(message);
    }
}