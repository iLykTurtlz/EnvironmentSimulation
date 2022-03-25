package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;
import utils.PoolPrey;
import utils.PredatorVision;

public class Predator extends Agent {

    public static final double p_reproduce = 0.01;

    protected int orientation;                          // (0,1,2,3) = (nord,est,sud,ouest)
    private PredatorVision vision;
    private int rangeOfVision;

    public Predator( int __x , int __y, World __world ) {
        super(__x,__y,__world, new float[] {1.f, 0.f, 0.f});
        this.orientation = (int)(4*Math.random());      //random orientation by default
        this.rangeOfVision = 10;
        this.speed = 10;
        this.vision = new PredatorVision(__x,__y,rangeOfVision,orientation,__world);
    }

    public Predator( int __x, int __y, World __world, boolean[] orientation)  {
        super(__x,__y,__world, orientation, new float[] {1.f,0.f,0.f});
    }

    public int eatAndHunt()    {            //returns the Predator's next move, based on the prey's location, -1 if no prey is seen.
        PoolPrey prey = world.getPrey();

        vision.setOrientation(orientation);
        vision.setPosition(x, y);
        vision.updateField();
        int[][] field = vision.getField();

        /* EAT */
        Prey dinner;
        for (int i=0; i<prey.getSizeUsed(); i++)    {
            dinner = prey.get(i);
            int[] coord = dinner.getCoordinate();
            if (coord[0] == field[0][0] && coord[1] == field[0][1]) {
                prey.remove(dinner);    //TODO : reduce hunger - make a flag that handles this
                System.out.println("Munch");
            }
            switch (orientation)    {
                case 0:
                    if (coord[0] == field[0][0] && coord[1] == ( field[0][1] + 1))  {
                        prey.remove(dinner);    //TODO : reduce hunger
                        System.out.println("Munch");
                    }
                    break;
                case 1:
                    if (coord[0] == (field[0][0] + 1) && coord[1] == field[0][1])  {
                        prey.remove(dinner);    //TODO : reduce hunger
                        System.out.println("Munch");
                    }
                    break;
                case 2:
                    if (coord[0] == field[0][0] && coord[1] == ( field[0][1] - 1))  {
                        prey.remove(dinner);    //TODO : reduce hunger
                        System.out.println("Munch");
                    }
                    break;
                case 3:
                    if (coord[0] == (field[0][0] - 1) && coord[1] == field[0][1])  {
                        prey.remove(dinner);    //TODO : reduce hunger
                        System.out.println("Munch");
                    }
                    break;
                default:
                    System.out.println("Erreur : eatAndHunt, EAT");
            }
        }

        /* HUNT */
        Prey target = vision.search(prey);
        if (target == null) {
            return -1;
        }
        int[] coord = target.getCoordinate();
        int height = world.getHeight();
        int width = world.getWidth();
        int right = (orientation + 1 + 4) % 4;
        int left = (orientation - 1 + 4) % 4;

        switch (orientation)    {
            case 0:
                if (coord[0] == x && directions[0])  {
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {
                    if ( coord[0] == ((x+i+width)%width) )  {   
                        return right;
                    }
                    if ( coord[0] == ((x-i+width)%width) )  {
                        return left;
                    }
                }
                break;
            case 1:
                if (coord[1] == y && directions[1])  {
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {
                    if ( coord[1] == ((y+i+height)%height) )  {   
                        return left;
                    }
                    if ( coord[1] == ((y-i+height)%height) )  {
                        return right;
                    }
                }
                break;
            case 2:
                if (coord[0] == x && directions[2])  {
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {
                    if ( coord[0] == ((x+i+width)%width) )  {   
                        return left;
                    }
                    if ( coord[0] == ((x-i+width)%width) )  {
                        return right;
                    }
                }
                break;
            case 3:
                if (coord[1] == y && directions[3])  {
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {
                    if ( coord[1] == ((y+i+height)%height) )  {   
                        return right;
                    }
                    if ( coord[1] == ((y-i+height)%height) )  {
                        return left;
                    }
                }
                break;

            default:
                System.out.println("Erreur : orientation, eatAndHunt()");
        }
        return -1;
        
    }


    public void step() 
	{
        super.step();
		if ( world.getIteration() % 20 == 0 )
		{
            
            double dice = Math.random();


            if (accessible == 0)    {   //If no direction is accessible, the agent does not move.
                return;
            }

            int move = eatAndHunt();    //move in the direction of the prey (0,1,2,3) = (N,E,S,W)

            if (move == -1) {           //Random movements if no prey seen
                int j=0;

                double partition_size = 1/((double)accessible);

                for (int i=0; i<directions.length; i++)    {
                    if ( directions[i] )    {
                        j++;
                        if ( dice < (j*partition_size) )  {    
                            move = i;
                            orientation = i;
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

            /* Devour prey */


        }
    }

}