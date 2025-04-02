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

    public Client(String ipAddress)
    {
        try
        {
//            frame = new JFrame("Dip Dip Boom");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            frame.getContentPane().setLayout(new GridLayout(1, 2));
//
//            Font font = new Font(null, Font.PLAIN, 72);
//
//            dipButton = new JButton("Dip");
//            dipButton.setFont(font);
//            dipButton.setEnabled(false);
//            dipButton.setActionCommand("dip");
//            dipButton.addActionListener(this);
//            frame.getContentPane().add(dipButton);
//
//            boomButton = new JButton("Boom");
//            boomButton.setFont(font);
//            boomButton.setEnabled(false);
//            boomButton.setActionCommand("boom");
//            boomButton.addActionListener(this);
//            frame.getContentPane().add(boomButton);
//
//            frame.pack();

            //display = new Display();

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
                System.out.println("Client received: " + message);

                //convert message string into array of tokens (originally separated by spaces)
                String[] tokens = message.split(" ");

                if (tokens[0].equals("go"))
                {
                    dipButton.setEnabled(true);
                    boomButton.setEnabled(true);
                }
                else if (tokens[0].equals("dip"))
                {
                    JOptionPane.showMessageDialog(frame, "Opponent played \"dip\"");
                    dipButton.setEnabled(true);
                    boomButton.setEnabled(true);
                }
                else if (tokens[0].equals("boom"))
                {
                    JOptionPane.showMessageDialog(frame, "Opponent won by playing \"boom\"");
                    System.exit(0);
                }
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //send message to server
    private void send(String message)
    {
        System.out.println("Client sending: " + message);
        out.println(message);
    }

    public void actionPerformed(String e){
        send(e);
    }

    //called when button is pressed
//    public void actionPerformed(ActionEvent e)
//    {
//        String command = e.getActionCommand();
//        if (command.equals("dip"))
//        {
//            dipButton.setEnabled(false);
//            boomButton.setEnabled(false);
//            send("dip");
//        }
//        else if (command.equals("boom"))
//        {
//            dipButton.setEnabled(false);
//            boomButton.setEnabled(false);
//            send("boom");
//            JOptionPane.showMessageDialog(frame, "You won by playing \"boom\"");
//            System.exit(0);
//        }
//    }
}