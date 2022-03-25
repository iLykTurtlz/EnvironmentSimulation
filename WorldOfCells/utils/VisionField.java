package utils;
import worlds.*;


public abstract class VisionField    {
    int[][] field;
    int range;
    World world;
    int x;
    int y;

    public VisionField(int x, int y, int range, World world)  {
        this.x = x;
        this.y = y;
        this.range = range;
        this.world = world;
    }

    protected abstract void calculateField();

    public void updateField()    {
        calculateField();
    }

    /* GETTERS AND SETTERS */

    public void setPosition(int x, int y)   {
        this.x = x;
        this.y = y;
    }

    public void setRange(int r) {
        this.range = r;
    }

    public int getRange(int r) {
        return this.range;
    }

    public int[][] getField()   {
        return field;
    }

    //= new int [100][2];

}