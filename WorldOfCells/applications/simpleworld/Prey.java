package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;
import utils.PoolPredator;
import utils.PreyVision;

public class Prey extends Agent {

    public static final double p_reproduce = 0.05;

    private PreyVision vision;
    private int rangeOfVision;
    

    public Prey( int __x , int __y, World __world ) {
        super(__x,__y,__world, new float[] {0.f,0.f,1.f});
        this.rangeOfVision = 5;
        this.speed = 20;
        this.vision = new PreyVision(__x,__y,rangeOfVision,__world);
    }

    public Prey( int __x , int __y, World __world, boolean[] orientation ) {
        super(__x,__y,__world, orientation, new float[] {0.f,0.f,1.f});
    }

    private int flee()  {
        //local variables for use later in the method
        PoolPredator predators = world.getPredators();
        int height = world.getHeight();
        int width = world.getWidth();

        //update vision and search for the nearest threat
        vision.setPosition(x, y);
        vision.updateField();
        Predator threat = vision.searchPredator(predators);

        if (threat == null) {   // no predator spotted in the field of vision -> return -1 to allow random displacement.
            return -1;
        }

        int[] coord = threat.getCoordinate();
        for (int i=0; i<rangeOfVision; i++) {                           //Tests of predator's location in counterclockwise sequence (to compensate for the prey's field of vision being a priority queue - implemented as a static array - in CLOCKWISE direction)
            
            if ( ((x-i+width)%width) == coord[0] && directions[1] )     //predator in direction 3 -> move in direction 1 (if possible)
                return 1;
            
            if ( ((y-i+height)%height) == coord[1] && directions[0] )   //predator in direction 2 -> direction 0
                return 0;
            
            if ( ((x+i+width)%width) == coord[0] && directions[3] )     //predator in direction 1 -> direction 3
                return 3;
            
            if ( ((y+i+height)%height) == coord[1] && directions[2] )   //predator in direction 0 -> direction 2
                return 2; 
        }
        return -1;  //if the threat spotted earlier in this method has already left the prey's field of vision, return -1 to allow random displacement.
    }
    

    public void step() 
	{
        super.step();

		if ( world.getIteration() % speed == 0 )
		{
			double dice = Math.random();
            

        
            if (accessible == 0)    {           //If no direction is accessible, the agent does not move.
                return;
            }

            int move = flee();

            if (move == -1) {                   //Random displacement in this case
                int j=0;

                double partition_size = 1/((double)accessible);

                for (int i=0; i<directions.length; i++)    {
                    if ( directions[i] )    {
                        j++;
                        if ( dice < (j*partition_size) )  {    
                            move = i;
                            break;
                        }
                    }
                }
            }

            /* set the agent's new position */
            switch (move)   {
                case 0:
                    this.y = (this.y + 1 + this.world.getHeight()) % this.world.getHeight();
                    break;
                case 1:
                    this.x = (this.x + 1 + this.world.getWidth()) % this.world.getWidth();
                    break;
                case 2:
                    this.y = (this.y - 1 + this.world.getHeight()) % this.world.getHeight();
                    break;
                case 3:
                    this.x = (this.x - 1 + this.world.getWidth()) % this.world.getWidth();
                    break;
                default:
                    System.out.println("Erreur de déplacement : move = " + move);
            }

            /* Reinitialize the four directions to true*/
            for (int i=0; i<directions.length; i++)    {
                directions[i] = true;
            }
        }
    }

    public void reinitialize()  {
        super.reinitialize();
    }

    

}