package utils;

public abstract class VisionField    {
    int[][] field;
    int range;
    int orientation;
    int x;
    int y;

    public VisionField(int x, int y, int range, int orientation)  {
        this.x = x;
        this.y = y;
        this.range = r;
        this.orientation = o;
    }

    protected abstract void calculateField();

    /* GETTERS AND SETTERS */

    public void setRange(int r) {
        this.range = r;
    }

    public void setOrientation(int o)   {
        this.orientation = o;
    }

    public int getRange(int r) {
        return this.range;
    }

    public int getOrientation(int o)    {
        return this.orientation;
    }

    //= new int [100][2];

}