package applications.simpleworld;
import utils.PredatorVision;
import worlds.World;

public class Godzilla extends Agent {
    private static Godzilla instance = null;
    private PredatorVision breath;

    private Godzilla(int __x , int __y, WorldOfTrees __world, float[] headColor, float[] bodyColor) {
        super(__x,__y,__world,headColor,bodyColor);
        this.breath = new PredatorVision(x, y, 20, orientation, world);
    }

    public static Godzilla getInstance(int x, int y, WorldOfTrees world)    {
        if (instance == null)   {
            instance = new Godzilla(x,y,world,new float[]{0.133f,0.545f,0.133f}, new float[]{0.133f,0.545f,0.133f});
        }
        return instance;
    }

    public void atomicBreath()  {
        
    }
}