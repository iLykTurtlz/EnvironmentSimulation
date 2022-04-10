// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package objects;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import applications.simpleworld.Plant;
import applications.simpleworld.Predator;
import applications.simpleworld.Prey;
import applications.simpleworld.WorldOfTrees;
import utils.PoolPredator;
import utils.PoolPrey;
import worlds.World;

abstract public class UniqueDynamicObject // UniqueObject are object defined with particular, unique, properties (ex.: particular location)
{
	protected int x,y;
	protected WorldOfTrees world;
	protected enum State {ALIVE, DEAD, ON_FIRE, IRRADIATED, PSYCHEDELIC};   
    protected State state;
	
	public UniqueDynamicObject(int __x, int __y, WorldOfTrees __world)
	{
		this.x = __x;
		this.y = __y;
		this.world = __world;
	}
	
	abstract public void step();

	public void setPosition(int x, int y)	{
		this.x = x;
		this.y = y;
	}
	
	public int[] getCoordinate()
	{
		int coordinate[] = new int[2];
		coordinate[0] = this.x;
		coordinate[1] = this.y;
		return coordinate;
	}
	
	abstract public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight );

	public boolean isAdjacent(UniqueDynamicObject udo)  {
        // Returns true if udo is adjacent to the one calling the function, otherwise false.
        int[] coord = udo.getCoordinate();
        int width = world.getWidth();
        int height = world.getHeight();

        return (  coord[0] == x && ( (coord[1] == y) || (coord[1] == ((y + 1 + height)%height)) || (coord[1] == ((y - 1 + height)%height)) )  )  ||
               (  coord[1] == y && ( ( (coord[0] == (x-1+width)%width))  ||  ( (coord[0] == (x+1+width)%width)) )  );  
    }


	public void spreadFire()	{
		// Sets fire to adjacent UniqueDynamicObjects if the current udo's state is State.ON_FIRE

		// if the agent who is on fire touches a tree, it burns
		if (world.getForest().getCellState(x,y) == 1)   {
			world.getForest().setCellState(x,y,2);
		}

		// adjacent UniqueDynamicObjects burn as well
		PoolPredator predators = world.getPredators();
		PoolPrey prey = world.getPrey();
		ArrayList<Plant> plants = world.getPlants();

		for (int i=0; i<predators.getSizeUsed(); i++)   {
			Predator p1 = predators.get(i);
			if (   isAdjacent( (UniqueDynamicObject)p1 )   ) {
				p1.catchFire();
			}
		}

		for (int i=0; i<prey.getSizeUsed(); i++)   {
			Prey p2 = prey.get(i);
			if (   isAdjacent( (UniqueDynamicObject)p2 )   ) {
				p2.catchFire();
			}
		}

		for (int i=0; i<plants.size(); i++) {
			Plant p3 = plants.get(i);
			if (   isAdjacent( (UniqueDynamicObject)p3 )   )    {
				p3.catchFire();
			}
		}
	}   


}
