import java.io.*;
import java.net.*;

public class Server
{
    private ServerThread thread1;
    private ServerThread thread2;

    public Server()
    {
        try
        {
            //start listening for connections on port 9000
            ServerSocket serverSocket = new ServerSocket(9000);
            
            //accept connection from player 1
            Socket socket1 = serverSocket.accept();
            //create ServerThread for handling connection for player 1
            thread1 = new ServerThread(socket1, this, 1, 0, 0);
            
            //accept connection from player 2
            Socket socket2 = serverSocket.accept();
            //create ServerThread for handling connection for player 2
            thread2 = new ServerThread(socket2, this, 2, 10, 10);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //send message to both players
    public void sendToBoth(String message)
    {
        thread1.send(message);
        thread2.send(message);
    }
}