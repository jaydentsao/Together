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
            while (true){
                long time=System.currentTimeMillis();
            //start listening for connections on port 9000
                ServerSocket serverSocket = new ServerSocket(9000);

                //create ServerThread for handling connection for player 1
                list.add(new ServerThread(serverSocket.accept(), this, 1));
                if(System.currentTimeMillis()-time>10000) break;
            }
            threads=(ServerThread[])((list.toArray()));
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //send message to other player
    public void sendToAll(String message)
    {
        for (ServerThread thread : threads) {
            thread.send(message);
        }
    }
}