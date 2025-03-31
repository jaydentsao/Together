import java.io.*;
import java.net.*;

public class ServerThread extends Thread
{
    private int playerNum;
    private BufferedReader in; //for receiving messages from this player's client
    private PrintWriter out; //for sending messages to this player's client
    private Server server; //for sending messages to opponent
    private int x;
    private int y;

    public ServerThread(Socket socket, Server server, int playerNum, int x, int y) throws IOException
    {
        this.server = server;
        this.playerNum = playerNum;
        this.x = x;
        this.y = y;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        start();
        //the run method has started
    }

    //keep receiving messages from this player's client
    public void run()
    {
        while (true)
        {
            try
            {
                String message = in.readLine();
                System.out.println("ServerThread " + playerNum + " received: " + message);

                //convert message string into array of tokens (originally separated by spaces)
                String[] tokens = message.split(" ");
                
                if (tokens[0].equals("pressed"))
                {
                    int key = Integer.parseInt(tokens[1]);
                    if (key == 32)
                    {
                        // space pressed
                        x = (int)(Math.random() * 400);
                        y = (int)(Math.random() * 400);
                        server.sendToBoth("set " + playerNum + " " + x + " " + y);
                    }
                }
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    //send message to this player's client
    public void send(String message)
    {
        System.out.println("ServerThread " + playerNum + " sending " + message);
        out.println(message);
    }
}