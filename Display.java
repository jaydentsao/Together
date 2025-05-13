import javax.swing.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.util.*;


public class Display extends JComponent implements KeyListener, MouseListener {
    private final Image lebron;
    private final Image grid;
    private final Image[] colorsImages;
    private final Image[] levelImages;
    private static final double k=0.015;

    private int width;
    private int height;

    ArrayList<Obstacle> obstacles;
    ArrayList<Player> players;

    int playerSize;

    boolean[] directions; // wasd
    boolean[] allowMove;

    double velocity;
    double acceleration;
    int speed;

    Client client;
    boolean start;
    boolean ready;
    int numPlayers;
    int playerNum;
    int color;
    Color[] colors;

    JFrame frame;

    long timeBefore;

    boolean[] finished;
    int level;


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
        levelImages = new Image[2];
        for(int i = 0; i < 2; ++i) {
            String levelName = "Levels/level" + i + ".png";
            URL url4 = getClass().getResource(levelName);
            if (url4 == null) {
                throw new RuntimeException("Unable to load:  " + levelName);
            }
            levelImages[i] = new ImageIcon(url4).getImage();
        }

        obstacles = new ArrayList<>();
//        obstacles.add(new Obstacle(-500, 860, 1600, 960, true));
//        obstacles.add(new Obstacle(0, 700, 1600, 750, true));

        obstacles.add(new Obstacle(-1463, -463, -1413, 537, 0, 0));
        obstacles.add(new Obstacle(-1413, 487, -913, 537, 0, 0));
        obstacles.add(new Obstacle(987, 487, 1087, 537, 0, 0));
        obstacles.add(new Obstacle(1087, 487, 1387, 537, 1, 0));
        obstacles.add(new Obstacle(1387, 487, 1487, 537, 0, 0));
        obstacles.add(new Obstacle(1487, -463, 1537, 537, 0, 0));

        obstacles.add(new Obstacle(-777, 401, -577, 451, 0, 1));
        obstacles.add(new Obstacle(-449, 345, -249, 395, 0, 1));
        obstacles.add(new Obstacle(-143, 297, 57, 347, 0, 1));
        obstacles.add(new Obstacle(231, 453, 431, 503, 0, 1));
        obstacles.add(new Obstacle(577, 405, 777, 455, 0, 1));

        obstacles.add(new Obstacle(-860, 319, -660, 369, 0, 2));
        obstacles.add(new Obstacle(-612, 112, -412, 162, 0, 2));
        obstacles.add(new Obstacle(-357, -113, -157, -63, 0, 2));
        obstacles.add(new Obstacle(-126, -365, 74, -315, 0, 2));
        obstacles.add(new Obstacle(223, 487, 373, 537, 0, 2));
        obstacles.add(new Obstacle(373, 487, 423, 537, 2, 2));
        obstacles.add(new Obstacle(423, 487, 623, 537, 0, 2));
        obstacles.add(new Obstacle(623, 487, 673, 537, 2, 2));
        obstacles.add(new Obstacle(673, 487, 823, 537, 0, 2));


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
        speed = 8;

        color = 0;
        numPlayers = 1;
        colors = new Color[]{new Color(66, 0, 0), new Color(76, 0, 80), new Color(12, 0, 80), new Color(0, 58, 80), new Color(0, 80, 50), new Color(12, 80, 0), new Color(78, 77, 14), new Color(96, 54, 5), new Color(70, 70, 70), new Color(13, 13, 13)};
        players = new ArrayList<Player>();

        level = 0;

        // Keyboard
        setFocusable(true); // indicates that Display can process key presses
        addKeyListener(this); // will notify Display when a key is pressed

        // Mouse
        addMouseListener(this); // will notify Display when the mouse is pressed
        client = new Client(ip, this);
        

        //JFX
        JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        Platform.runLater(() -> {
                System.out.print("jfx");
                File file = new File("misc/lebronKawaii.mp4"); 
                Media media = new Media(file.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                StackPane root = new StackPane();
                root.getChildren().add(mediaView);
                mediaView.fitWidthProperty().bind(root.widthProperty());
                mediaView.fitHeightProperty().bind(root.heightProperty());
                mediaView.setPreserveRatio(true);
                Scene scene = new Scene(root);
                fxPanel.setScene(scene);
                mediaPlayer.play();
        });

        run();
    }

    public void paintComponent(Graphics g) {

        if(start)
            g.drawImage(levelImages[level], players.get(playerNum).getX()*-1 - levelImages[level].getWidth(null)/2 + width/2, (players.get(playerNum)).getY()*-1 - levelImages[level].getHeight(null)/2 + height/2, null);

        if (start) {
            for (int i = 0; i < numPlayers - 1; i++) {
                int centerX = players.get(i).getX() + playerSize / 2;
                int centerY = players.get(i).getY() + playerSize / 2;
                int centerOtherX = players.get(i + 1).getX() + playerSize / 2;
                int centerOtherY = players.get(i + 1).getY() + playerSize / 2;

                // Adjust and Center Coordinates
                int offsetX = width / 2 - playerSize / 2 - players.get(playerNum).getX();
                int offsetY = height / 2 - playerSize / 2 - players.get(playerNum).getY();

                centerX += offsetX;
                centerY += offsetY;
                centerOtherX += offsetX;
                centerOtherY += offsetY;

                // Smooth Transition of Connection Cord Color and Thickness
                int forceX = Math.abs(forceX(i));
                
                int forceY = Math.abs(forceY(i));
                int netForce = (int) Math.sqrt(forceX * forceX + 8 * (forceY * forceY));
                float lineThickness = Math.max(15 - 1.333f * netForce, 5);
                int rColor = Math.min(0 + 42 * netForce, 255);
                int gColor = Math.max(255 - 42 * netForce, 0);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(rColor, gColor, 0));
                g2d.setStroke(new BasicStroke(lineThickness));
                g2d.drawLine(centerX, centerY, centerOtherX, centerOtherY);
            }
            for(int i = 0; i < numPlayers; ++i) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(colors[(players.get(i)).getColor()]);
                int stroke = 5;
                g2d.setStroke(new BasicStroke(stroke));

                int imageX = players.get(i).getX()-players.get(playerNum).getX()+width/2-playerSize/2;
                int imageY = players.get(i).getY()-players.get(playerNum).getY()+height/2-playerSize/2;
                g.drawImage(lebron, imageX, imageY, playerSize, playerSize, null);
                g2d.drawRect(imageX+stroke-(stroke/2), imageY+stroke-(stroke/2), playerSize-stroke, playerSize-stroke);

                obstacles.set(i, new Obstacle(players.get(i).getX(), players.get(i).getY(), players.get(i).getX()+ playerSize, players.get(i).getY() + playerSize, 0, 0));
            }
            for (int i = numPlayers; i < obstacles.size(); i++) {
                Obstacle obstacle = obstacles.get(i) ;
                if(obstacle.level == 0 || obstacle.level == level+1){
                    Graphics2D g2d = (Graphics2D)g;
                    if(obstacle.type == 0) g2d.setColor(new Color(0,0,100));
                    else if(obstacle.type == 1) g2d.setColor(new Color(0,100,0));
                    else if(obstacle.type == 2) g2d.setColor(new Color(100,0,0));
                    int[] coords=obstacle.getCoords();
                    g2d.fillRect(coords[0]-players.get(playerNum).getX()+width/2-playerSize/2, coords[1]-players.get(playerNum).getY()+height/2-playerSize/2, coords[2]-coords[0], coords[3]-coords[1]);
                }
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


                allowMove = new boolean[]{false, true, true, true};
                // Player-Obstacle Collisions
                boolean onObstacle = false;
                for (int i = 0 ; i < obstacles.size(); i++) {
                    if(i != playerNum && (obstacles.get(i).level == 0 || obstacles.get(i).level == level+1)) {
                        int[] coords = obstacles.get(i).getCoords();
                        // Left Border
                        if (players.get(playerNum).getX() - speed >= coords[0] && players.get(playerNum).getX() - speed <= coords[2]
                                && (players.get(playerNum).getY() < coords[3] && players.get(playerNum).getY() + playerSize > coords[1])) {
                            if (directions[1] && allowMove[1] && players.get(playerNum).getX() != coords[2])
                                (players.get(playerNum)).setX(coords[2]);
                            allowMove[1] = false;
                        }
                        // Right Border
                        if (players.get(playerNum).getX() + playerSize + speed >= coords[0] && players.get(playerNum).getX() + playerSize + speed <= coords[2]
                                && (players.get(playerNum).getY() < coords[3] && players.get(playerNum).getY() + playerSize > coords[1])) {
                            if (directions[3] && allowMove[3] && players.get(playerNum).getX() + playerSize != coords[0])
                                (players.get(playerNum)).setX(coords[0]-playerSize);
                            allowMove[3] = false;
                        }
                        // Top Border
                        if (players.get(playerNum).getY() - velocity >= coords[1] && players.get(playerNum).getY() - velocity <= coords[3]
                                && players.get(playerNum).getX() + playerSize > coords[0] && players.get(playerNum).getX() < coords[2]) {
                            if(players.get(playerNum).getY() != coords[3])
                                (players.get(playerNum)).setY(coords[3]);
                            velocity = 0;
                        }
                        // Bottom Border
                        if (players.get(playerNum).getY() + playerSize - velocity >= coords[1] && players.get(playerNum).getY() + playerSize - velocity <= coords[3]
                                && players.get(playerNum).getX() + playerSize > coords[0] && players.get(playerNum).getX() < coords[2]) {
                            players.get(playerNum).setY(coords[1] - playerSize);
                            onObstacle = true;
                            allowMove[0] = true;
                            if(obstacles.get(i).type == 1){
                                client.send("complete " + playerNum);
                                completed(playerNum);
                            }
                            if(obstacles.get(i).type == 2){
                                client.send("died");
                                died();
                            }
                        }
                    }
                }

                if(!onObstacle) {
                    if (velocity > -20) velocity += acceleration+forceY(playerNum);
                    allowMove[0] = false;
                } else {
                    velocity = 0;
                    allowMove[0] = true;
                }

                if (directions[0] && allowMove[0])
                    velocity = 10; //10
                if (directions[1] && allowMove[1])
                    (players.get(playerNum)).setX((players.get(playerNum)).getX() - speed - forceX(playerNum));
                else if (allowMove[1] && forceX(playerNum) > 0 && !onObstacle)
                    (players.get(playerNum)).setX((players.get(playerNum)).getX() - forceX(playerNum));
                if (directions[3] && allowMove[3])
                    (players.get(playerNum)).setX((players.get(playerNum)).getX() + speed - forceX(playerNum));
                else if (allowMove[3] && forceX(playerNum) < 0 && !onObstacle)
                    (players.get(playerNum)).setX((players.get(playerNum)).getX() - forceX(playerNum));
                players.get(playerNum).setY(players.get(playerNum).getY() - (int) velocity);


                if(players.get(playerNum).getY() > 3000){
                    client.send("died");
                    died();
                }

                boolean allDone = true;
                for(int i  = 0; i < numPlayers; i++)
                    if(!finished[i]) allDone = false;
                if(allDone) {
                    died();
                    level++;
                }

                repaint();
                client.send("pos " + (players.get(playerNum)).getX() + " " + (players.get(playerNum)).getY() + " " + playerNum);

            }
            try { Thread.sleep(1); }
            catch (Exception e) { }
        }
    }

    private int forceX(int player){
        if(numPlayers == 1) return 0;
        if(player==0){
            double force=k*(players.get(player).getX()-players.get(player+1).getX());
            return (int)force;
        }
        if(player==numPlayers-1){
            double force=k*(players.get(player).getX()-players.get(player-1).getX());
            return (int)force;
        }
        double force=k*(2*players.get(player).getX()-players.get(player-1).getX()-players.get(player+1).getX());
        return (int)force;
    }

    private int forceY(int player){
        if(numPlayers == 1) return 0;
        if(player==0){
            double force=k*(players.get(player).getY()-players.get(player+1).getY())/4;
            return (int)force;
        }
        if(player==numPlayers-1){
            double force=k*(players.get(player).getY()-players.get(player-1).getY())/4;
            return (int)force;
        }
        double force=k*(2*players.get(player).getY()-players.get(player-1).getY()-players.get(player+1).getY())/6;
        return (int)force;
    }

    public void updatePosition(int x, int y, int player) {
        players.get(player).setX(x);
        players.get(player).setY(y);
    }

    public void updateColor(int c, int player) {
        players.get(player).setColor(c);
    }

    public void readyGame(int numPlayers, int playerNum) {
        ready = true;
        this.numPlayers = numPlayers;
        this.playerNum = playerNum;
        finished = new boolean[numPlayers];

        for(int i = 0; i < numPlayers; ++i) {
            int[] playerPosition = new int[]{i*(playerSize+5) - 1400, 200};
            players.add(new Player(playerPosition, i + 1, 0));
            obstacles.add(i, new Obstacle(players.get(i).getX(), players.get(i).getY(), players.get(i).getX()+ playerSize, players.get(i).getY() + playerSize, 0, 0));
        }
        //System.out.println("ready");
    }

    public void died(){
        players.get(playerNum).setX(playerNum*(playerSize+5) - 1400);
        players.get(playerNum).setY(200);
        velocity = 0;
        for(int i = 0; i < numPlayers; i++)
            finished[i] = false;
    }

    public void completed(int pNum){
        finished[pNum] = true;
    }

    public void start() {
        start = true;
    }

    public void keyPressed(KeyEvent e) {
//         System.out.println(e.getKeyCode());
        //movement
        if (e.getKeyCode() == 87 || e.getKeyCode() == 38) // W -> Up
            directions[0] = true;
        if (e.getKeyCode() == 65 || e.getKeyCode() == 37) // A -> Left
            directions[1] = true;
        if (e.getKeyCode() == 68 || e.getKeyCode() == 39) // D -> Right
            directions[3] = true;

        //color select
        if (!start && ready) {
            if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
                if (color == 9)
                    color = 0;
                else
                    color++;
                repaint();
                client.send("color " + color + " " + playerNum);
                players.get(playerNum).setColor(color);
            }

            if (e.getKeyCode() == 37 || e.getKeyCode() == 65) {
                if (color == 0)
                    color = 9;
                else
                    color--;
                repaint();
                client.send("color " + color + " " + playerNum);
                players.get(playerNum).setColor(color);
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
        if (e.getKeyCode() == 87 || e.getKeyCode() == 38) // W -> Up
            directions[0] = false;
        if (e.getKeyCode() == 65 || e.getKeyCode() == 37) // A -> Left
            directions[1] = false;
        if (e.getKeyCode() == 68 || e.getKeyCode() == 39) // D -> Right
            directions[3] = false;
    }

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
