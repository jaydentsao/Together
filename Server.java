import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
    private ServerThread[] threads;

    public Server(int numPlayers)
    {
        try
        {
            threads= new ServerThread[numPlayers];
            ServerSocket serverSocket = new ServerSocket(9000);
            for (int i = 1; i < numPlayers+1; i++) {
            //start listening for connections on port 9000
                Socket socket = serverSocket.accept();

                //create ServerThread for handling connection for player 1
                threads[i-1]=(new ServerThread(socket, this, i));
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
        for (ServerThread thread : threads) {
            if(from.equals(thread)) continue;
            thread.send(message);
        }
    }
}