import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.*;


public class Display extends JComponent implements KeyListener, MouseListener {
    private Image lebron;
    private Image grid;
    private Image[] colorsImages;

    private int width;
    private int height;

    LinkedList<Obstacle> obstacles;
    ArrayList<Player> players;
    int playerWidth;
    int playerHeight;

    boolean[] directions; // wasd
    boolean[] allowMove;

    boolean jumping;
    int jumpHeight;
    int currentJumpHeight;

    int gravity;
    boolean falling;

    Client client;
    boolean start;
    boolean ready;
    int numPlayers;
    int playerNum;
    int color;
    Color[] colors;

    JFrame frame;


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
        obstacles.add(new Obstacle(0, 400, 700, 500, true));

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

        playerWidth = 50;
        playerHeight = 50;


        directions = new boolean[4];
        allowMove = new boolean[] { true, true, true, true };

        jumping = false;
        start = false;
        ready = false;
        jumpHeight = 200;
        currentJumpHeight = 0;

        gravity = 0;
        falling = false;
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
                g2d.setStroke(new BasicStroke(9));
                g2d.drawRect((players.get(i)).getX()-players.get(playerNum-1).getX()+width/2-playerWidth/2, (players.get(i)).getY()-players.get(playerNum-1).getY()+height/2-playerHeight/2, 50, 50);
                g.drawImage(lebron, (players.get(i)).getX()-players.get(playerNum-1).getX()+width/2-playerWidth/2, (players.get(i)).getY()-players.get(playerNum-1).getY()+height/2-playerHeight/2, 50, 50, null);
            }
        } else {
            double colorWidth = (double)width * (double)0.5F;
            double colorHeight = colorWidth / (double)colorsImages[color].getWidth(null) * (double)colorsImages[color].getHeight(null);
            g.drawImage(colorsImages[color], (int)((double)(width / 2) - colorWidth / (double)2.0F), (int)((double)(height / 2) - colorHeight / (double)2.0F), (int)colorWidth, (int)colorHeight, null);
        }

        // red rectangle on lebron image's border
        // Graphics2D g2d = (Graphics2D) g;
        // g2d.setColor(Color.RED);
        // g2d.setStroke(new BasicStroke(3));
        // g2d.drawRect(playerX, playerY, 50, 50);
        // g2d.setColor(Color.GREEN);
        // g2d.drawRect(player2X, player2Y, 50, 50);
    }

    public void run() {
        while(true) {
            Dimension size = frame.getSize();
            width = size.width;
            height = size.height;
            if (!start) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            } else {
                if (directions[0] && !jumping)
                    jumping = true;
                if (directions[1])
                    (players.get(playerNum - 1)).setX((players.get(playerNum - 1)).getX() - 1);
                if (directions[2])
                    (players.get(playerNum - 1)).setY((players.get(playerNum - 1)).getY() + 1);
                if (directions[3])
                    (players.get(playerNum - 1)).setX((players.get(playerNum - 1)).getX() + 1);

                if (jumping) {
                    if (currentJumpHeight == jumpHeight) {
                        falling = true;
                        jumping = false;
                        currentJumpHeight = 0;
                    } else {
                        currentJumpHeight++;
                        players.get(playerNum - 1).setY(players.get(playerNum - 1).getY()-1);
                    }
                }

                // Gravity
                if (falling && allowMove[2]) {
                    if (gravity != jumpHeight) {
                        players.get(playerNum - 1).setY(players.get(playerNum - 1).getY()+1);
                        gravity++;
                    }
                    else {
                        falling = false;
                        gravity = 0;
                    }
                }
                allowMove = new boolean[] { true, true, true, true };
                // Player-Player Collisions
                for (int i = 0; i < numPlayers; i++) {
                    if (i != playerNum - 1) {
                        // Left Border
                        if (players.get(playerNum - 1).getX() == players.get(i).getX() + playerWidth
                                && Math.abs(players.get(playerNum - 1).getY() - players.get(i).getY()) < playerHeight) {
                            directions[1] = false;
                            allowMove[1] = false;
                        }
                        // Right Border
                        if (players.get(playerNum - 1).getX() + playerWidth == players.get(i).getX()
                                && Math.abs(players.get(playerNum - 1).getY() - players.get(i).getY()) < playerHeight) {
                            directions[3] = false;
                            allowMove[3] = false;
                        }
                        // Top Border
                        if (players.get(playerNum - 1).getY() == players.get(i).getY() + playerHeight
                                && Math.abs(players.get(playerNum - 1).getX() - players.get(i).getX()) < playerWidth) {
                            directions[0] = false;
                            allowMove[0] = false;
                        }
                        // Bottom Border
                        if (players.get(playerNum - 1).getY() + playerHeight == players.get(i).getY()
                                && Math.abs(players.get(playerNum - 1).getX() - players.get(i).getX()) < playerWidth) {
                            directions[2] = false;
                            allowMove[2] = false;
                        }
                    }
                }
                // Player-Obstacle Collisions
                for (Obstacle obstacle : obstacles) {
                    int[] coords=obstacle.getCoords();
                    // Left Border
                    if (players.get(playerNum - 1).getX() == coords[2]
                            && (players.get(playerNum - 1).getY() +playerHeight<coords[1] || players.get(playerNum - 1).getY()-playerHeight>coords[3])){
                        directions[1] = false;
                        allowMove[1] = false;
                    }
                    // Right Border
                    if (players.get(playerNum - 1).getX()  + playerWidth == coords[0]
                            && (players.get(playerNum - 1).getY()+playerHeight<coords[1] || players.get(playerNum - 1).getY()-playerHeight>coords[3])) {
                        directions[3] = false;
                        allowMove[3] = false;
                    }
                    // Top Border
                    if (players.get(playerNum - 1).getY() == coords[3]
                            && (players.get(playerNum - 1).getX() +playerWidth<coords[0] || players.get(playerNum - 1).getX() -playerWidth>coords[2])) {
                        directions[0] = false;
                        allowMove[0] = false;
                    }
                    // Bottom Border
                    if (players.get(playerNum - 1).getY() + playerHeight == coords[1]
                            && (players.get(playerNum - 1).getX() +playerWidth<coords[0] || players.get(playerNum - 1).getX() -playerWidth>coords[2])) {
                        directions[2] = false;
                        allowMove[2] = false;
                    }
                }

                repaint();
                client.send("pos " + (players.get(playerNum - 1)).getX() + " " + (players.get(playerNum - 1)).getY() + " " + playerNum);

                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }
        }
    }

    public void updatePosition(int x, int y, int player) {
        (players.get(player - 1)).setX(x);
        (players.get(player - 1)).setY(y);
    }

    public void updateColor(int c, int player) {
        (players.get(player - 1)).setColor(c);
    }

    public void readyGame(int numPlayers, int playerNum) {
        ready = true;
        this.numPlayers = numPlayers;
        this.playerNum = playerNum;

        for(int i = 0; i < numPlayers; ++i) {
            int[] playerPosition = new int[]{i*(playerWidth+5), 0};
            players.add(new Player(playerPosition, i + 1, 0));
        }

        System.out.println("ready");
    }

    public void start() {
        start = true;
    }

    public void keyPressed(KeyEvent e) {
        // System.out.println(e.getKeyCode());
        if (e.getKeyCode() == 87 && !falling && allowMove[0]) // W -> Up
            directions[0] = true;
        if (e.getKeyCode() == 65 && allowMove[1]) // A -> Left
            directions[1] = true;
        if (e.getKeyCode() == 83 && allowMove[2]) // S -> Down
            directions[2] = true;
        if (e.getKeyCode() == 68 && allowMove[3]) // D -> Right
            directions[3] = true;


        if (ready && e.getKeyCode() == 32) {
            HashSet<Integer> colorSet = new HashSet();

            for(int i = 0; i < numPlayers; ++i) {
                colorSet.add((players.get(i)).getColor());
            }

            if (colorSet.size() == numPlayers) {
                client.send("start");
                start();
            }
        }

        if (!start && ready) {
            if (e.getKeyCode() == 39) {
                if (color == 9) {
                    color = 0;
                } else {
                    ++color;
                }

                repaint();
                client.send("color " + color + " " + playerNum);
                (players.get(playerNum - 1)).setColor(color);
            }

            if (e.getKeyCode() == 37) {
                if (color == 0) {
                    color = 9;
                } else {
                    --color;
                }

                repaint();
                client.send("color " + color + " " + playerNum);
                (players.get(playerNum - 1)).setColor(color);
            }
        }
}


    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 87) // W -> Up
            directions[0] = false;
        if (e.getKeyCode() == 65) // A -> Left
            directions[1] = false;
        if (e.getKeyCode() == 83) // S -> Down
            directions[2] = false;
        if (e.getKeyCode() == 68) // D -> Right
            directions[3] = false;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
