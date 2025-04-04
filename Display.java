import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;


public class Display extends JComponent implements KeyListener,  MouseListener {
    private Image lebron;
    private Image grid;
    private int width;
    private int height;
    int playerX;
    int playerY;
    boolean[] directions; //wasd
    boolean jumping;
    int jumpHeight;
    int currentJumpHeight;
    Client client;


    public Display(Client c)
    {
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



        //Window
        JFrame frame = new JFrame();  //create window
        frame.setTitle("Together");  //set title of window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //closing window will exit program
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().add(this);  //add drawing region to window
        frame.pack();  //adjust window size to fit drawing region
        frame.setVisible(true);  //show window

        Dimension size = frame.getSize();
        width = size.width;
        height = size.height;

        playerX = 0;
        playerY = 0;

        directions= new boolean[4];
        jumping = false;

        jumpHeight = 200;
        currentJumpHeight = 0;

        client = c;

        //Keyboard
        setFocusable(true);  //indicates that Display can process key presses
        addKeyListener(this);  //will notify Display when a key is pressed

        //Mouse
        addMouseListener(this);  //will notify Display when the mouse is pressed
        run();
    }

    public void paintComponent(Graphics g)
    {
//        int lebronWidth;
//        int lebronHeight;
//        if(lebron.getWidth(null) > lebron.getHeight(null)){
//            lebronWidth = (lebron.getHeight(null)/height)*lebron.getWidth(null);
//            lebronHeight = height;
//        } else {
//            lebronWidth = width;
//            lebronHeight = (int)((width/(double)lebron.getWidth(null))*(double)lebron.getHeight(null));
//            System.out.println(lebron.getWidth(null));
//        }
        //g.drawImage(lebron, 0, 0, lebronWidth, lebronHeight, null);

        g.drawImage(grid, 0, 0, grid.getWidth(null)*4, grid.getHeight(null)*4, null);
        g.drawImage(lebron, playerX, playerY, 50, 50, null);

        // red rectangle on lebron image's border
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(playerX, playerY, 50, 50);
    }

    public void run(){
        while(true){
            //if(directions[0]) mapOffsetY++;
            if(directions[0] && !jumping) jumping = true;
            if(directions[1]) playerX--;
            if(directions[2]) playerY++;
            if(directions[3]) playerX++;

            if(jumping){
                if(currentJumpHeight == jumpHeight) {
                    jumping = false;
                    currentJumpHeight = 0;
                }
                else {
                    currentJumpHeight++;
                    playerY--;
                }

            }
            repaint();
            try{Thread.sleep(1);}catch(Exception e){}
        }
    }

    public void keyPressed(KeyEvent e)
    {
        //System.out.println(e.getKeyCode());
        if(e.getKeyCode() == 87)
            directions[0] = true;
        if(e.getKeyCode() == 65)
            directions[1] = true;
        if(e.getKeyCode() == 83)
            directions[2] = true;
        if(e.getKeyCode() == 68)
            directions[3] = true;
        client.actionPerformed(String.valueOf(e.getKeyCode()));
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == 87)
            directions[0] = false;
        if(e.getKeyCode() == 65)
            directions[1] = false;
        if(e.getKeyCode() == 83)
            directions[2] = false;
        if(e.getKeyCode() == 68)
            directions[3] = false;
    }

    public void mouseClicked(MouseEvent e) {  }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void keyTyped(KeyEvent e) { }
}
