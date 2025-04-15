import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.LinkedList;

public class Display extends JComponent implements KeyListener, MouseListener {
    private Image lebron;
    private Image grid;
    private int width;
    private int height;

    LinkedList<Obstacle> obstacles;
    int[] playerXs;
    int[] playerYs;
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

    int numPlayers;
    int playerNum;

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

        obstacles = new LinkedList<>();

        // Window
        JFrame frame = new JFrame(); // create window
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

        // int playerX;
        // int playerY;
        // int player2X;
        // int player2Y;

        directions = new boolean[4];
        allowMove = new boolean[] { true, true, true, true };

        jumping = false;
        start = false;

        jumpHeight = 200;
        currentJumpHeight = 0;

        gravity = 0;
        falling = false;

        numPlayers = 1;

        // Keyboard
        setFocusable(true); // indicates that Display can process key presses
        addKeyListener(this); // will notify Display when a key is pressed

        // Mouse
        addMouseListener(this); // will notify Display when the mouse is pressed
        client = new Client(ip, this);
        run();
    }

    public void paintComponent(Graphics g) {
        // int lebronWidth;
        // int lebronHeight;
        // if(lebron.getWidth(null) > lebron.getHeight(null)){
        // lebronWidth = (lebron.getHeight(null)/height)*lebron.getWidth(null);
        // lebronHeight = height;
        // }
        // else {
        // lebronWidth = width;
        // lebronHeight =
        // (int)((width/(double)lebron.getWidth(null))*(double)lebron.getHeight(null));
        // System.out.println(lebron.getWidth(null));
        // }
        // g.drawImage(lebron, 0, 0, lebronWidth, lebronHeight, null);

        g.drawImage(grid, 0, 0, grid.getWidth(null) * 4, grid.getHeight(null) * 4, null);
        if (start) {
            for (int i = 0; i < numPlayers; i++) {
                g.drawImage(lebron, playerXs[i], playerYs[i], playerWidth, playerHeight, null);
            }
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
        while (true) {
            if (!start) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            } else {
                // if(directions[0]) mapOffsetY++;
                // System.out.println(directions[0] + " " + directions[1] + " " + directions[2]
                // + " " + directions[3]);
                if (directions[0] && !jumping)
                    jumping = true; // W Pressed & Not Currently Jumping
                if (directions[1])
                    playerXs[playerNum - 1]--;
                if (directions[2])
                    playerYs[playerNum - 1]++;
                if (directions[3])
                    playerXs[playerNum - 1]++;

                // Jumping
                if (jumping) {
                    if (currentJumpHeight == jumpHeight) {
                        falling = true;
                        jumping = false;
                        currentJumpHeight = 0;
                    } else {
                        currentJumpHeight++;
                        playerYs[playerNum - 1]--;
                    }

                }

                // Gravity
                if (falling) {
                    if (gravity != jumpHeight) {
                        playerYs[playerNum - 1]++;
                        gravity++;
                    } else {
                        falling = false;
                        gravity = 0;
                    }
                }
                allowMove = new boolean[] { true, true, true, true };
                // Player-Player Collisions
                for (int i = 0; i < numPlayers; i++) {
                    if (i != playerNum - 1) {
                        // Left Border
                        if (playerXs[playerNum - 1] == playerXs[i] + playerWidth
                                && Math.abs(playerYs[playerNum - 1] - playerYs[i]) < playerHeight) {
                            directions[1] = false;
                            allowMove[1] = false;
                        }
                        // Right Border
                        if (playerXs[playerNum - 1] + playerWidth == playerXs[i]
                                && Math.abs(playerYs[playerNum - 1] - playerYs[i]) < playerHeight) {
                            directions[3] = false;
                            allowMove[3] = false;
                        }
                        // Top Border
                        if (playerYs[playerNum - 1] == playerYs[i] + playerHeight
                                && Math.abs(playerXs[playerNum - 1] - playerXs[i]) < playerWidth) {
                            directions[0] = false;
                            allowMove[0] = false;
                        }
                        // Bottom Border
                        if (playerYs[playerNum - 1] + playerHeight == playerYs[i]
                                && Math.abs(playerXs[playerNum - 1] - playerXs[i]) < playerWidth) {
                            directions[2] = false;
                            allowMove[2] = false;
                        }
                    }
                }
                // Player-Obstacle Collisions
                for (Obstacle obstacle : obstacles) {
                    int[] coords=obstacle.getCoords();
                    // Left Border
                    if (playerXs[playerNum - 1] == coords[2]
                            && (playerYs[playerNum - 1]+playerHeight<coords[1] || playerYs[playerNum - 1]-playerHeight>coords[3])){
                        directions[1] = false;
                        allowMove[1] = false;
                    }
                    // Right Border
                    if (playerXs[playerNum - 1] + playerWidth == coords[0]
                            && (playerYs[playerNum - 1]+playerHeight<coords[1] || playerYs[playerNum - 1]-playerHeight>coords[3])) {
                        directions[3] = false;
                        allowMove[3] = false;
                    }
                    // Top Border
                    if (playerYs[playerNum - 1] == coords[3]
                            && (playerXs[playerNum - 1]+playerWidth<coords[0] || playerXs[playerNum - 1]-playerWidth>coords[2])) {
                        directions[0] = false;
                        allowMove[0] = false;
                    }
                    // Bottom Border
                    if (playerYs[playerNum - 1] + playerHeight == coords[1]
                            && (playerXs[playerNum - 1]+playerWidth<coords[0] || playerXs[playerNum - 1]-playerWidth>coords[2])) {
                        directions[2] = false;
                        allowMove[2] = false;
                    }
                }

                repaint();
                client.send("pos " + playerXs[playerNum - 1] + " " + playerYs[playerNum - 1] + " " + playerNum);
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }
        }
    }

    public void updatePosition(int x, int y, int player) {
        playerXs[player - 1] = x;
        playerYs[player - 1] = y;
    }

    public void startGame(int players, int playerNum) {
        start = true;
        numPlayers = players;
        this.playerNum = playerNum;
        playerXs = new int[numPlayers];
        for (int i = 0; i < playerXs.length; i++) {
            playerXs[i] = i * playerWidth;
        }
        playerYs = new int[numPlayers];
        System.out.println("starting");
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
