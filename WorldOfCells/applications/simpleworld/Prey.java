package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

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
    

    public void step() 
	{
        super.step();

		if ( world.getIteration() % speed == 0 )
		{
			double dice = Math.random();
            

            /* If no direction is accessible, the agent does not move. */
            if (accessible == 0)    {
                return;
            }


            int move=-1, j=0;

            double partition_size = ((double)1)/((double)accessible);

            for (int i=0; i<directions.length; i++)    {
                if ( directions[i] )    {
                    j++;
                    if ( dice < (j*partition_size) )  {    
                        move = i;
                        break;
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