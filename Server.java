import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
    private ServerThread[] threads;

    public Server()
    {
        try
        {
            ArrayList<ServerThread> list=new ArrayList<>();
            for (int i = 0; i < 2; i++) {
            //start listening for connections on port 9000
                ServerSocket serverSocket = new ServerSocket(9000);

                //create ServerThread for handling connection for player 1
                list.add(new ServerThread(serverSocket.accept(), this, i));
            }
            threads=(ServerThread[])((list.toArray()));
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