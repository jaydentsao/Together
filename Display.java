import javax.swing.*;
import java.io.FileNotFoundException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.*;


public class Display extends JComponent implements KeyListener, MouseListener {
    private final Image lebronEdit;
    private final Image[] lebronSelectImages;
    private final Image[] levelImages;
    private final Image[] lebronImages;
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
    boolean videoDone;
    int numPlayers;
    int playerNum;
    int lebronNum;

    JFrame frame;

    long timeBefore;

    boolean[] finished;
    int level;

    private String name;
    JFXPanel fxPanel;
    boolean selectRepaint;


    public Display(String ip) {
        lebronImages = new Image[6];
        for(int i = 0; i < 6; ++i) {
            String lebronName = "Lebrons/lebron" + i + ".png";
            URL url1 = getClass().getResource(lebronName);
            if (url1 == null) {
                throw new RuntimeException("Unable to load:  " + lebronName);
            }
            lebronImages[i] = new ImageIcon(url1).getImage();
        }

        lebronSelectImages = new Image[6];
        for(int i = 0; i < 6; ++i) {
            String lebronSelectName = "LebronSelect/Lebrons" + i + ".png";
            URL url3 = getClass().getResource(lebronSelectName);
            if (url3 == null) {
                throw new RuntimeException("Unable to load:  " + lebronSelectName);
            }
            lebronSelectImages[i] = new ImageIcon(url3).getImage();
        }
        levelImages = new Image[5];
        for(int i = 0; i < 5; ++i) {
            String levelName = "Levels/level" + i + ".png";
            URL url4 = getClass().getResource(levelName);
            if (url4 == null) {
                throw new RuntimeException("Unable to load:  " + levelName);
            }
            levelImages[i] = new ImageIcon(url4).getImage();
        }

        String editName = "HDLebrons/lebron0.png";
        URL url5 = getClass().getResource(editName);
        if (url5 == null)
            throw new RuntimeException("Unable to load:  " + editName);
        lebronEdit = new ImageIcon(url5).getImage();

        obstacles = new ArrayList<>();

        obstacles.add(new Obstacle(-1463, -463, -1413, 637, 0, 0));
        obstacles.add(new Obstacle(-1413, 487, -913, 637, 0, 0));
        obstacles.add(new Obstacle(987, 487, 1087, 637, 0, 0));
        obstacles.add(new Obstacle(1087, 487, 1387, 637, 1, 0));
        obstacles.add(new Obstacle(1387, 487, 1487, 637, 0, 0));
        obstacles.add(new Obstacle(1487, -463, 1537, 637, 0, 0));

        obstacles.add(new Obstacle(-777, 401, -577, 451, 0, 1));
        obstacles.add(new Obstacle(-449, 345, -249, 395, 0, 1));
        obstacles.add(new Obstacle(-143, 297, 57, 347, 0, 1));
        obstacles.add(new Obstacle(231, 453, 431, 503, 0, 1));
        obstacles.add(new Obstacle(577, 405, 777, 455, 0, 1));

        obstacles.add(new Obstacle(-688, 487, -538, 537, 0, 2));
        obstacles.add(new Obstacle(-538, 487, -488, 537, 2, 2));
        obstacles.add(new Obstacle(-488, 487, -288, 537, 0, 2));
        obstacles.add(new Obstacle(-288, 487, -238, 537, 2, 2));
        obstacles.add(new Obstacle(-238, 487, -88, 537, 0, 2));
        obstacles.add(new Obstacle(133, 487, 283, 537, 0, 2));
        obstacles.add(new Obstacle(283, 487, 333, 537, 2, 2));
        obstacles.add(new Obstacle(333, 487, 533, 537, 0, 2));
        obstacles.add(new Obstacle(533, 487, 583, 537, 2, 2));
        obstacles.add(new Obstacle(583, 487, 733, 537, 0, 2));
        
        obstacles.add(new Obstacle(-854, 400, -754, 450, 0, 3));
        obstacles.add(new Obstacle(-754, 400, -704, 450, 2, 3));
        obstacles.add(new Obstacle(-704, 400, -604, 450, 0, 3));
        obstacles.add(new Obstacle(-564, 316, -464, 366, 0, 3));
        obstacles.add(new Obstacle(-464, 316, -414, 366, 2, 3));
        obstacles.add(new Obstacle(-414, 316, -314, 366, 0, 3));
        obstacles.add(new Obstacle(-133, 276, -33, 326, 0, 3));
        obstacles.add(new Obstacle(-33, 276, 17, 326, 2, 3));
        obstacles.add(new Obstacle(17, 276, 117, 326, 0, 3));
        obstacles.add(new Obstacle(217, 174, 317, 224, 0, 3));
        obstacles.add(new Obstacle(317, 174, 367, 224, 2, 3));
        obstacles.add(new Obstacle(367, 174, 467, 224, 0, 3));
        obstacles.add(new Obstacle(564, 487, 664, 537, 0, 3));
        obstacles.add(new Obstacle(664, 487, 714, 537, 2, 3));
        obstacles.add(new Obstacle(714, 487, 814, 537, 0, 3));
        
        obstacles.add(new Obstacle(-713, 487, -563, 537, 0, 4));
        obstacles.add(new Obstacle(-663, 467, -613, 487, 2, 4));
        obstacles.add(new Obstacle(-493, 431, -343, 481, 0, 4));
        obstacles.add(new Obstacle(-443, 411, -393, 431, 2, 4));
        obstacles.add(new Obstacle(-247, 431, -97, 481, 0, 4));
        obstacles.add(new Obstacle(-197, 411, -147, 431, 2, 4));
        obstacles.add(new Obstacle(-37, 347, 113, 397, 0, 4));
        obstacles.add(new Obstacle(13, 327, 63, 347, 2, 4));
        obstacles.add(new Obstacle(166, 263, 316, 313, 0, 4));
        obstacles.add(new Obstacle(216, 243, 266, 263, 2, 4));
        obstacles.add(new Obstacle(414, 487, 554, 537, 0, 4));
        obstacles.add(new Obstacle(454, 467, 514, 487, 2, 4));
        obstacles.add(new Obstacle(690, 487, 840, 537, 0, 4));
        obstacles.add(new Obstacle(740, 467, 790, 487, 2, 4));
        
        obstacles.add(new Obstacle(-860, 319, -660, 369, 0, 5));
        obstacles.add(new Obstacle(-612, 112, -412, 162, 0, 5));
        obstacles.add(new Obstacle(-357, -113, -157, -63, 0, 5));
        obstacles.add(new Obstacle(-126, -365, 74, -315, 0, 5));
        obstacles.add(new Obstacle(223, 487, 373, 537, 0, 5));
        obstacles.add(new Obstacle(373, 487, 423, 537, 2, 5));
        obstacles.add(new Obstacle(423, 487, 623, 537, 0, 5));
        obstacles.add(new Obstacle(623, 487, 673, 537, 2, 5));
        obstacles.add(new Obstacle(673, 487, 823, 537, 0, 5));

        frame = new JFrame(); // create window
        frame.setTitle("LeGether"); // set title of window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closing window will exit program
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().add(this); // add drawing region to window
        frame.pack(); // adjust window size to fit drawing region
        frame.setVisible(true); // show window

        Dimension size = frame.getSize();
        width = size.width;
        height = size.height;

        playerSize = 75;

        name = "";

        directions = new boolean[4];
        allowMove = new boolean[] { true, true, true, true };

        start = false;
        ready = false;

        timeBefore = System.currentTimeMillis();

        acceleration = -0.4;
        velocity = 0.0;
        speed = 8;

        lebronNum = 0;
        numPlayers = 1;
        players = new ArrayList<Player>();

        level = 0;

        // Keyboard
        setFocusable(true); // indicates that Display can process key presses
        addKeyListener(this); // will notify Display when a key is pressed

        // Mouse
        addMouseListener(this); // will notify Display when the mouse is pressed
        client = new Client(ip, this);

        selectRepaint = true;
        //JFX
        fxPanel = new JFXPanel();
        frame.add(fxPanel);
        playVideo();
 
        repaint();
        run();
    }

    public void paintComponent(Graphics g) {
        if (start&&videoDone) {
            g.drawImage(levelImages[level], (int)(players.get(playerNum).getX()*-1-levelImages[level].getWidth(null) + width/2), (int)(players.get(playerNum).getY()*-1 - levelImages[level].getHeight(null) + height/2), (int)(levelImages[level].getWidth(null))*2, (int)(levelImages[level].getHeight(null))*2, null);
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
                int imageX = players.get(i).getX()-players.get(playerNum).getX()+width/2-playerSize/2;
                int imageY = players.get(i).getY()-players.get(playerNum).getY()+height/2-playerSize/2;
                g.drawImage(lebronImages[players.get(i).getLebron()], imageX, imageY, playerSize, playerSize, null);
                if(i == playerNum){
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setColor(Color.YELLOW);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRect(imageX, imageY, playerSize, playerSize);
                }
            }
            for(int i = 0; i < numPlayers; ++i) {
                int imageX = players.get(i).getX()-players.get(playerNum).getX()+width/2-playerSize/2;
                int imageY = players.get(i).getY()-players.get(playerNum).getY()+height/2-playerSize/2;
                Graphics2D g2d = (Graphics2D) g;
                Font font = new Font("Arial", Font.BOLD, 30);
                g2d.setFont(font);
                if(i == playerNum) g2d.setColor(Color.YELLOW);
                else g2d.setColor(new Color(120, 50, 180));
                g2d.drawString(players.get(i).getName(), (int)((double)(playerSize / 2) - (double) (g2d.getFontMetrics(font).stringWidth(players.get(i).getName()) / 2)) + imageX, imageY - 10);
                obstacles.set(i, new Obstacle(players.get(i).getX(), players.get(i).getY(), players.get(i).getX()+ playerSize, players.get(i).getY() + playerSize, 0, 0));
            }
            for (int i = numPlayers; i < obstacles.size(); i++) {
                Obstacle obstacle = obstacles.get(i) ;
                if(obstacle.level == 0 || obstacle.level == level+1){
                    Graphics2D g2d = (Graphics2D)g;
                    if(obstacle.type == 0) g2d.setColor(new Color(0,0,100));
                    else if(obstacle.type == 1) g2d.setColor(new Color(0,200,0));
                    else if(obstacle.type == 2) g2d.setColor(new Color(200,0,0));
                    int[] coords=obstacle.getCoords();
                    g2d.fillRect(coords[0]-players.get(playerNum).getX()+width/2-playerSize/2, coords[1]-players.get(playerNum).getY()+height/2-playerSize/2, coords[2]-coords[0], coords[3]-coords[1]);
                }
            }
        } else{
            frame.getContentPane().setBackground(new Color(130, 0, 200));
            double colorWidth = (double) width * (double) 0.6F;
            double colorHeight = colorWidth / (double) lebronSelectImages[lebronNum].getWidth(null) * (double) lebronSelectImages[lebronNum].getHeight(null);
            if(videoDone) g.drawImage(lebronSelectImages[lebronNum], (int) ((double) (width / 2) - colorWidth / (double) 2.0F), (int) ((double) (height / 2.75) - colorHeight / (double) 2.0F), (int) colorWidth, (int) colorHeight, null);
            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Arial", Font.BOLD, width/25);
            g2d.setFont(font);
            if(videoDone) g2d.drawString("Enter Name: " + name, (int) ((double) (width / 2) - (double) (g2d.getFontMetrics(font).stringWidth("Enter Name " + name) / 2)), (int) (height * 0.80));
            else g2d.drawString("Enter Name: " + name, (int) ((double) (width / 2) - (double) (g2d.getFontMetrics(font).stringWidth("Enter Name " + name) / 2)), 99999999);
            g2d.setColor(Color.YELLOW);
            if(videoDone)g2d.drawString("Press Enter to Start", (int) ((double) (width / 2) - (double) (g2d.getFontMetrics(font).stringWidth("Press Enter to Start") / 2)), (int) (height * 0.90));
            else g2d.drawString("Press Enter to Start", (int) ((double) (width / 2) - (double) (g2d.getFontMetrics(font).stringWidth("Press Enter to Start") / 2)), 999999999);
            if(selectRepaint) {
                repaint();
                selectRepaint = false;
            }
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
                            if(obstacles.get(i).type == 2){
                                client.send("died");
                                died();
                            }
                        }
                        // Right Border
                        if (players.get(playerNum).getX() + playerSize + speed >= coords[0] && players.get(playerNum).getX() + playerSize + speed <= coords[2]
                                && (players.get(playerNum).getY() < coords[3] && players.get(playerNum).getY() + playerSize > coords[1])) {
                            if (directions[3] && allowMove[3] && players.get(playerNum).getX() + playerSize != coords[0])
                                (players.get(playerNum)).setX(coords[0]-playerSize);
                            allowMove[3] = false;
                            if(obstacles.get(i).type == 2){
                                client.send("died");
                                died();
                            }
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


                if(players.get(playerNum).getY() > 2000){
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

    public void updateLebron(int c, int player) {
        players.get(player).setLebron(c);
    }

    public void updateName(String s, int player) {
        players.get(player).setName(s);
    }

    public void readyGame(int numPlayers, int playerNum) {
        ready = true;
        this.numPlayers = numPlayers;
        this.playerNum = playerNum;
        finished = new boolean[numPlayers];
        for(int i = 0; i < numPlayers; ++i) {
            int[] playerPosition = new int[]{i*(playerSize+5) - 1400, 250};
            players.add(new Player(playerPosition, i + 1, 0, ""));
            obstacles.add(i, new Obstacle(players.get(i).getX(), players.get(i).getY(), players.get(i).getX()+ playerSize, players.get(i).getY() + playerSize, 0, 0));
        }
    }

    public void died(){
        players.get(playerNum).setX(playerNum*(playerSize+5) - 1400);
        players.get(playerNum).setY(300);
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
    
    public void reset(){
        start = false;
        videoDone = false;
        level = 0;
        died();
        playVideo();
    }
    
    public void playVideo(){
        Platform.runLater(() -> {
                File file = new File("misc/lebronKawaii.mp4"); 
                Media media = new Media(file.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                StackPane root = new StackPane();
                root.getChildren().add(mediaView);
                mediaView.fitWidthProperty().bind(root.widthProperty());
                mediaView.fitHeightProperty().bind(root.heightProperty());
                mediaView.setPreserveRatio(true);
                Scene scene = new Scene(root, javafx.scene.paint.Color.rgb(0, 0, 0));
                fxPanel.setScene(scene);
                mediaPlayer.play();
                mediaPlayer.setOnEndOfMedia(() ->{
                    Platform.runLater(() -> fxPanel.setScene(null));
                    videoDone=true;
                    frame.getContentPane().setBackground(new Color(130, 0, 200));
                    selectRepaint = true;
                    repaint();
                });
        });
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        //movement
        if (e.getKeyCode() == 87 || e.getKeyCode() == 38) // W -> Up
            directions[0] = true;
        if (e.getKeyCode() == 65 || e.getKeyCode() == 37) // A -> Left
            directions[1] = true;
        if (e.getKeyCode() == 68 || e.getKeyCode() == 39) // D -> Right
            directions[3] = true;

        //lebron select
        if (!start && ready && videoDone) {
            if (e.getKeyCode() == 39) { // || e.getKeyCode() == 68
                if (lebronNum == 5)
                    lebronNum = 0;
                else
                    lebronNum++;
                repaint();
                client.send("lebron " + lebronNum + " " + playerNum);
                players.get(playerNum).setLebron(lebronNum);
            }

            if (e.getKeyCode() == 37) {  //|| e.getKeyCode() == 65
                if (lebronNum == 0)
                    lebronNum = 5;
                else
                    lebronNum--;
                repaint();
                client.send("lebron " + lebronNum + " " + playerNum);
                players.get(playerNum).setLebron(lebronNum);
            }
        }

        //game start
        if (ready && e.getKeyCode() == 10 && videoDone) {
            HashSet<Integer> lebronSet = new HashSet<Integer>();
            for(int i = 0; i < numPlayers; ++i)
                lebronSet.add((players.get(i)).getLebron());
            if (lebronSet.size() == numPlayers) {
                client.send("start");
                start();
            }
        }

        if(!start && videoDone){
            if(e.getKeyCode() == 8 && !name.isEmpty()) name = name.substring(0, name.length()-1);
            if(e.getKeyCode() > 40 && e.getKeyCode() < 91 || e.getKeyCode() == 32)
                name += e.getKeyChar();
            repaint();
            client.send("name " + name + " " + playerNum);
            players.get(playerNum).setName(name);
        }
        
        if (e.getKeyCode() == 27 && videoDone){ //reset
            client.send("reset");
            reset();
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


    public void keyTyped(KeyEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}
