import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
    private ServerThread[] threads;
    private int numPlayers;

    public Server(int numPlayers)
    {
        this.numPlayers = numPlayers;
        try
        {
            threads= new ServerThread[numPlayers];
            ServerSocket serverSocket = new ServerSocket(9000);
            for (int i = 0; i < numPlayers; i++) {
            //start listening for connections on port 9000
                Socket socket = serverSocket.accept();

                //create ServerThread for handling connection for player 1
                threads[i]= new ServerThread(socket, this, i+1);
            }
            for (int i = 0; i < numPlayers; i++) {
                threads[i].send("go");
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //send message to other player
    public void sendToAll(String message, ServerThread from)
    {
        for (int i = 0; i < numPlayers; i++) {
            if(!from.equals(threads[i])) threads[i].send(message);
        }
    }
}