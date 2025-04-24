import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.*;


public class Display extends JComponent implements KeyListener, MouseListener {
    private final Image lebron;
    private final Image grid;
    private final Image[] colorsImages;

    private int width;
    private int height;

    LinkedList<Obstacle> obstacles;
    ArrayList<Player> players;

    int playerSize;

    boolean[] directions; // wasd
    boolean[] allowMove;

    double velocity;
    double acceleration;

    Client client;
    boolean start;
    boolean ready;
    int numPlayers;
    int playerNum;
    int color;
    Color[] colors;

    JFrame frame;

    long timeBefore;


    public Display(String ip) {
        String lebronName = "lebron.jpg";
        URL url1 = getClass().getResource(lebronName);
        if (url1 == null)
            throw new RuntimeException("Unable to load:  " + lebronName);
        lebron = new ImageIcon(url1).getImage();

        String gridName = "graph.jpg";
        URL url2 = getClass().getResource(gridName);
        if (url2 == null)
            throw new RuntimeException("Unable to load:  " + gridName);
        grid = new ImageIcon(url2).getImage();

        colorsImages = new Image[10];
        for(int i = 0; i < 10; ++i) {
            String colorsName = "Colors/Colors" + i + ".png";
            URL url3 = getClass().getResource(colorsName);
            if (url3 == null) {
                throw new RuntimeException("Unable to load:  " + colorsName);
            }
            colorsImages[i] = new ImageIcon(url3).getImage();
        }

        obstacles = new LinkedList<>();
        obstacles.add(new Obstacle(-500, 800, 1600, 900, true));

        obstacles.add(new Obstacle(0, 700, 1600, 750, true));

        frame = new JFrame(); // create window
        frame.setTitle("Together"); // set title of window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closing window will exit program
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().add(this); // add drawing region to window
        frame.pack(); // adjust window size to fit drawing region
        frame.setVisible(true); // show window

        Dimension size = frame.getSize();
        width = size.width;
        height = size.height;

        playerSize = 75;


        directions = new boolean[4];
        allowMove = new boolean[] { true, true, true, true };

        start = false;
        ready = false;

        timeBefore = System.currentTimeMillis();

        acceleration = -0.4;
        velocity = 0.0;

        color = 0;
        numPlayers = 1;
        colors = new Color[]{new Color(66, 0, 0), new Color(76, 0, 80), new Color(12, 0, 80), new Color(0, 58, 80), new Color(0, 80, 50), new Color(12, 80, 0), new Color(78, 77, 14), new Color(96, 54, 5), new Color(70, 70, 70), new Color(13, 13, 13)};
        players = new ArrayList<Player>();

        // Keyboard
        setFocusable(true); // indicates that Display can process key presses
        addKeyListener(this); // will notify Display when a key is pressed

        // Mouse
        addMouseListener(this); // will notify Display when the mouse is pressed
        client = new Client(ip, this);
        run();
    }

    public void paintComponent(Graphics g) {

        if(start)
            g.drawImage(grid, players.get(playerNum-1).getX()*-1, (players.get(playerNum-1)).getY()*-1, grid.getWidth(null) * 4, grid.getHeight(null) * 4, null);
        else
            g.drawImage(grid, 0, 0, grid.getWidth(null) * 4, grid.getHeight(null) * 4, null);

        if (start) {
            for(int i = 0; i < numPlayers; ++i) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(colors[(players.get(i)).getColor()]);
                int stroke = 5;
                g2d.setStroke(new BasicStroke(stroke));

                int imageX = players.get(i).getX()-players.get(playerNum-1).getX()+width/2-playerSize/2;
                int imageY = players.get(i).getY()-players.get(playerNum-1).getY()+height/2-playerSize/2;
                g.drawImage(lebron, imageX, imageY, playerSize, playerSize, null);
                g2d.drawRect(imageX+stroke-(stroke/2), imageY+stroke-(stroke/2), playerSize-stroke, playerSize-stroke);

            }
            for (Obstacle obstacle : obstacles) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(new Color(0,0,0));
                int[] coords=obstacle.getCoords();
                g2d.fillRect(coords[0]-players.get(playerNum-1).getX()+width/2-playerSize/2, coords[1]-players.get(playerNum-1).getY()+height/2-playerSize/2, coords[2]-coords[0], coords[3]-coords[1]);
            }
        } else {
            double colorWidth = (double)width * (double)0.5F;
            double colorHeight = colorWidth / (double)colorsImages[color].getWidth(null) * (double)colorsImages[color].getHeight(null);
            g.drawImage(colorsImages[color], (int)((double)(width / 2) - colorWidth / (double)2.0F), (int)((double)(height / 2) - colorHeight / (double)2.0F), (int)colorWidth, (int)colorHeight, null);
        }
    }

    public void run() {
        while(true) {
            long timePassed = System.currentTimeMillis() - timeBefore;
            Dimension size = frame.getSize();
            width = size.width;
            height = size.height;
            if (start && timePassed > 17) {
                timeBefore = System.currentTimeMillis();
                //System.out.println(timePassed);
                if (directions[0] && allowMove[0])
                    velocity = 10;
                if (directions[1] && allowMove[1])
                    (players.get(playerNum - 1)).setX((players.get(playerNum - 1)).getX() - 8);
                if (directions[3] && allowMove[3])
                    (players.get(playerNum - 1)).setX((players.get(playerNum - 1)).getX() + 8);


                allowMove = new boolean[]{false, true, true, true};
                // Player-Player Collisions
                for (int i = 0; i < numPlayers; i++) {
                    if (i != playerNum - 1) {
                        // Left Border
                        if (players.get(playerNum - 1).getX() == players.get(i).getX() + playerSize
                                && Math.abs(players.get(playerNum - 1).getY() - players.get(i).getY()) < playerSize) {
                            allowMove[1] = false;
                        }
                        // Right Border
                        if (players.get(playerNum - 1).getX() + playerSize == players.get(i).getX()
                                && Math.abs(players.get(playerNum - 1).getY() - players.get(i).getY()) < playerSize) {
                            allowMove[3] = false;
                        }
                        // Top Border
                        if (players.get(playerNum - 1).getY() == players.get(i).getY() + playerSize
                                && Math.abs(players.get(playerNum - 1).getX() - players.get(i).getX()) < playerSize) {

                        }
                        // Bottom Border
                        if (players.get(playerNum - 1).getY() + playerSize == players.get(i).getY()
                                && Math.abs(players.get(playerNum - 1).getX() - players.get(i).getX()) < playerSize) {

                        }
                    }
                }
                // Player-Obstacle Collisions
                boolean onObstacle = false;
                for (Obstacle obstacle : obstacles) {
                    int[] coords = obstacle.getCoords();
                    // Left Border
                    if (players.get(playerNum - 1).getX() == coords[2]
                            && (players.get(playerNum - 1).getY()<=coords[3] && players.get(playerNum - 1).getY()+playerSize>=coords[1])){
                        allowMove[1] = false;
                    }
                    // Right Border
                    if (players.get(playerNum - 1).getX()  + playerSize == coords[0]
                            && (players.get(playerNum - 1).getY()<=coords[3] && players.get(playerNum - 1).getY()+playerSize>=coords[1])) {
                        allowMove[3] = false;
                    }
                    // Top Border
                    if (players.get(playerNum - 1).getY() == coords[3]
                            && (players.get(playerNum - 1).getX() +playerSize>=coords[0] || players.get(playerNum - 1).getX()<=coords[2])) {

                    }
                    // Bottom Border
                    if (players.get(playerNum - 1).getY() + playerSize - velocity >= coords[1] && players.get(playerNum - 1).getY() + playerSize - velocity <= coords[3]
                            && players.get(playerNum - 1).getX() +playerSize>=coords[0] && players.get(playerNum - 1).getX()<=coords[2]) {
                        players.get(playerNum - 1).setY(coords[1]-playerSize);
                        onObstacle = true;
                        allowMove[0] = true;
                    }
                }

                if(!onObstacle) {
                    if (velocity > -20) velocity += acceleration;
                    allowMove[0] = false;
                } else {
                    velocity = 0;
                    allowMove[0] = true;
                }

                players.get(playerNum - 1).setY(players.get(playerNum - 1).getY() - (int)velocity);


                repaint();
                client.send("pos " + (players.get(playerNum - 1)).getX() + " " + (players.get(playerNum - 1)).getY() + " " + playerNum);

            }
            try { Thread.sleep(1); }
            catch (Exception e) { }
        }
    }

    public void updatePosition(int x, int y, int player) {
        players.get(player - 1).setX(x);
        players.get(player - 1).setY(y);
    }

    public void updateColor(int c, int player) {
        players.get(player - 1).setColor(c);
    }

    public void readyGame(int numPlayers, int playerNum) {
        ready = true;
        this.numPlayers = numPlayers;
        this.playerNum = playerNum;

        for(int i = 0; i < numPlayers; ++i) {
            int[] playerPosition = new int[]{i*(playerSize+5), 0};
            players.add(new Player(playerPosition, i + 1, 0));
        }
        //System.out.println("ready");
    }

    public void start() {
        start = true;
    }

    public void keyPressed(KeyEvent e) {
        // System.out.println(e.getKeyCode());
        //movement
        if (e.getKeyCode() == 87) // W -> Up
            directions[0] = true;
        if (e.getKeyCode() == 65) // A -> Left
            directions[1] = true;
        if (e.getKeyCode() == 68) // D -> Right
            directions[3] = true;

        //color select
        if (!start && ready) {
            if (e.getKeyCode() == 39) {
                if (color == 9)
                    color = 0;
                else
                    color++;
                repaint();
                client.send("color " + color + " " + playerNum);
                players.get(playerNum - 1).setColor(color);
            }

            if (e.getKeyCode() == 37) {
                if (color == 0)
                    color = 9;
                else
                    color--;
                repaint();
                client.send("color " + color + " " + playerNum);
                players.get(playerNum - 1).setColor(color);
            }
        }

        //game start
        if (ready && e.getKeyCode() == 32) {
            HashSet<Integer> colorSet = new HashSet<Integer>();
            for(int i = 0; i < numPlayers; ++i)
                colorSet.add((players.get(i)).getColor());
            if (colorSet.size() == numPlayers) {
                client.send("start");
                start();
            }
        }
}


    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 87) // W -> Up
            directions[0] = false;
        if (e.getKeyCode() == 65) // A -> Left
            directions[1] = false;
        if (e.getKeyCode() == 68) // D -> Right
            directions[3] = false;
    }

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
