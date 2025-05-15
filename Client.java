import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Client extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame;
    private JButton dipButton;
    private JButton boomButton;
    private Display display;

    public Client(String ipAddress, Display d) {
        try {
            this.display = d;
            Socket socket = new Socket(ipAddress, 9000);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            while(true) {
                String message = this.in.readLine();
                String[] tokens = message.split(" ");
                if (tokens[0].equals("pos")) {
                    this.display.updatePosition(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
                }

                if (tokens[0].equals("ready")) {
                    this.display.readyGame(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                }

                if (tokens[0].equals("lebron")) {
                    this.display.updateLebron(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                }

                if (tokens[0].equals("name") && tokens.length > 2) {
                    this.display.updateName(tokens[1], Integer.parseInt(tokens[2]));
                }

                if (tokens[0].equals("start")) {
                    this.display.start();
                }

                if(tokens[0].equals("died")) {
                    this.display.died();
                }

                if(tokens[0].equals("complete")) {
                    this.display.completed(Integer.parseInt(tokens[1]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message) {
        this.out.println(message);
    }
}
