public class Player {
    private int[] pos;
    private int num;
    private int color;

    public Player(int[] pos, int num, int color) {
        this.pos = pos;
        this.num = num;
        this.color = color;
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

    public void setColor(int c) {
        this.color = c;
    }

    public int getColor() {
        return this.color;
    }
}