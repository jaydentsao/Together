public class Player {
    private int[] pos;
    private int num;
    private int lebron;
    private String name;

    public Player(int[] pos, int num, int lebron, String name) {
        this.pos = pos;
        this.num = num;
        this.lebron = lebron;
        this.name = name;
    }

    public void setX(int x) {
        this.pos[0] = x;
    }

    public void setY(int y) {
        this.pos[1] = y;
    }

    public int getX() {
        return this.pos[0];
    }

    public int getY() {
        return this.pos[1];
    }

    public void setLebron(int c) {
        this.lebron = c;
    }

    public int getLebron() {
        return this.lebron;
    }

    public void setName(String n) {this.name = n;}

    public String getName() {return this.name;}
}