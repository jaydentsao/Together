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
            thread1 = new ServerThread(socket1, this, 1);

            //accept connection from player 2
            Socket socket2 = serverSocket.accept();
            //create ServerThread for handling connection for player 2
            thread2 = new ServerThread(socket2, this, 2);

            //tell player 1 to go first
            thread1.send("go");
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //send message to other player
    public void sendToOpponent(String message, int playerNum)
    {
        if (playerNum == 1)
            thread2.send(message);
        else
            thread1.send(message);
    }
}