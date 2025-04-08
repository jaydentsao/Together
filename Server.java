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
            for (int i = 0; i < numPlayers; i++) {
            //start listening for connections on port 9000
                Socket socket = serverSocket.accept();

                //create ServerThread for handling connection for player 1
                threads[i]=(new ServerThread(socket, this, i+1));
            }
            sendToAll("go", threads[0]);
            sendToAll("go", threads[1]);
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
            if(!from.equals(thread)) thread.send(message);
        }
    }
}