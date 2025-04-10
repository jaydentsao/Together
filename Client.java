import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends Thread
//        implements ActionListener
{
    private BufferedReader in; //for receiving messages from the server
    private PrintWriter out; //for sending messages to the server
    private JFrame frame;
    private JButton dipButton;
    private JButton boomButton;
    private Display display;

    public Client(String ipAddress, Display d)
    {
        try
        {
            display = d;

            //connect to server running on port 9000 of given ipAddress
            Socket socket = new Socket(ipAddress, 9000);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            start();
            //the run method has started
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //keep receiving messages from server
    public void run()
    {
        try
        {
            while (true)
            {
                String message = in.readLine();
                //System.out.println("Client " + hashCode() + " received: " + message);

                //convert message string into array of tokens (originally separated by spaces)
                String[] tokens = message.split(" ");
                if (tokens[0].equals("pos"))
                {
                    display.updatePosition(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
                    //System.out.println(Integer.parseInt(tokens[1]) + " " + Integer.parseInt(tokens[2]));
                }
                if(tokens[0].equals("go")){
                    display.startGame(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                }
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //send message to server
    public void send(String message)
    {
        //System.out.println("Client sending: " + message);
        out.println(message);
    }

}