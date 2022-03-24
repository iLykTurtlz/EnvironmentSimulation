package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

public class Predator extends Agent {

    public static final double p_reproduce = 0.01;

    public Predator( int __x , int __y, World __world ) {
        super(__x,__y,__world, new float[] {1.f, 0.f, 0.f});
    }

    public Predator( int __x, int __y, World __world, boolean[] orientation)  {
        super(__x,__y,__world, orientation, new float[] {1.f,0.f,0.f});
    }

    public void eatAndHunt()    {
        Prey target;
        switch (orientation)    {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                System.out.println("Erreur : orientation d'un prédateur.");
        }
        
    }

    public void step() 
	{
        super.step();
		if ( world.getIteration() % 20 == 0 )
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

            /* Devour prey */


        }
    }

}