import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends JComponent implements Runnable, KeyListener
{
    private BufferedReader in; //for receiving messages from the server
    private PrintWriter out; //for sending messages to the server

    //player 1 coordinates
    private int x1;
    private int y1;

    //player 2 coordinates
    private int x2;
    private int y2;

    public Client(String ipAddress)
    {
        try
        {
            //connect to server running on port 9000 of given ipAddress
            Socket socket = new Socket(ipAddress, 9000);
            
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        new Thread(this).start();
        //the run method has started

        JFrame frame = new JFrame("Space Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 400));
        setFocusable(true);
        addKeyListener(this);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void paintComponent(Graphics g)
    {
        //draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 400, 400);
        
        //draw player 1
        g.setColor(Color.RED);
        g.fillRect(x1, y1, 10, 10);
        
        //draw player 2
        g.setColor(Color.CYAN);
        g.fillRect(x2, y2, 10, 10);
    }

    //keep receiving messages from server
    public void run()
    {
        while (true)
        {
            try
            {
                String message = in.readLine();
                System.out.println("Client received: " + message);

                //convert message string into array of tokens (originally separated by spaces)
                String[] tokens = message.split(" ");
                
                if (tokens[0].equals("set"))
                {
                    int playerNum = Integer.parseInt(tokens[1]);
                    int x = Integer.parseInt(tokens[2]);
                    int y = Integer.parseInt(tokens[3]);
                    if (playerNum == 1)
                    {
                        x1 = x;
                        y1 = y;
                    }
                    else
                    {
                        x2 = x;
                        y2 = y;
                    }
                }
                
                repaint();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    //called when key is pressed
    public void keyPressed(KeyEvent e)
    {
        send("pressed " + e.getKeyCode());
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
    }

    //send message to server
    public void send(String message)
    {
        System.out.println("Client sending: " + message);
        out.println(message);
    }
}