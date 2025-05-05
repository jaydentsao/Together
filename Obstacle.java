public class Obstacle {
    private int[] coords;
    public int type;
    public int level;
    public Obstacle(int x1, int y1, int x2, int y2, int type, int level){
        coords=new int[]{x1,y1,x2,y2};
        this.type=type;
        this.level = level;
    }

    public int[] getCoords(){
        return coords;
    }
}
