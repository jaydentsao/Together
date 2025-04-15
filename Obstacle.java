public class Obstacle {
    private int[] coords;
    public boolean safe;
    public Obstacle(int x1, int y1, int x2, int y2, boolean safe){
        coords=new int[]{x1,y1,x2,y2};
        this.safe=safe;
    }

    public int[] getCoords(){
        return coords;
    }
}
