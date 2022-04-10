package applications.simpleworld;

import javax.lang.model.util.ElementScanner14;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

public abstract class Plant extends UniqueDynamicObject {
    protected int size;
    protected int growth_rate;      //0-999: 1000 will provoke a division by zero exception
    protected int remainingBurnTime;

    public Plant(int __x , int __y, WorldOfTrees __world)  {
        super(__x,__y,__world);
        size = 0;
        state = State.ALIVE;
        remainingBurnTime = 10;
    }

    public void step() {
        burnDown();
    }

    public abstract void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight );

    public abstract void reduceSize();  //call this method when the plant gets eaten

    public void catchFire() {
        state = State.ON_FIRE;
    }

    public void burnDown()  {
        // This function will first test if the plant is on fire, so it can be called with every step()
        if (state == State.ON_FIRE)  {
            if (remainingBurnTime > 0)  {
                remainingBurnTime--;
            } else {
                state = State.DEAD;
            }
        }    
    }
 

    public void reinitialize()  {
        return;
    }


    public int getSize()    {
        return size;
    }


    public void decrementSize() {
        if (size > 0)
            size--;
    }

    public abstract void incrementSize();

    public void resetSize() {
        this.size = 0;
    }

}
