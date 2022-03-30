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
    protected int bloodlustThreshold;

    public Predator( int __x , int __y, World __world ) {
        super(__x,__y,__world, new float[] {1.f, 0.f, 0.f});
        this.orientation = (int)(4*Math.random());      //random orientation by default
        this.rangeOfVision = 10;
        this.speed = 80;
        this.vision = new PredatorVision(__x,__y,rangeOfVision,orientation,__world);
        this.bloodlustThreshold = 15;
    }

    public Predator( int __x, int __y, World __world, boolean[] orientation)  {
        super(__x,__y,__world, orientation, new float[] {1.f,0.f,0.f});
    }


    /*

    private int findMate()  {
        Pool predators = world.getPredators();

    }
    */

    private int eatAndHunt()    {            //returns the Predator's next move, based on the prey's location, -1 if no prey is seen.
        PoolPrey prey = world.getPrey();
        double dice = Math.random();
        boolean dinnertime = false;

        int[][] field = vision.getField();

        /* EAT */
        Prey dinner;
        for (int i=0; i<prey.getSizeUsed(); i++)    {
            dinner = prey.get(i);
            int[] coord = dinner.getCoordinate();
            if (coord[0] == field[0][0] && coord[1] == field[0][1]) {                           //Same space -> dinnertime.
                dinnertime = true;    
            }
            switch (orientation)    {                                                           //Space directly in front -> dinnertime.
                case 0:
                    if (coord[0] == field[0][0] && coord[1] == ( field[0][1] + 1))  {
                        dinnertime = true;
                    }
                    break;
                case 1:
                    if (coord[0] == (field[0][0] + 1) && coord[1] == field[0][1])  {
                        dinnertime = true;
                    }
                    break;
                case 2:
                    if (coord[0] == field[0][0] && coord[1] == ( field[0][1] - 1))  {
                        dinnertime = true;
                    }
                    break;
                case 3:
                    if (coord[0] == (field[0][0] - 1) && coord[1] == field[0][1])  {
                        dinnertime = true;
                    }
                    break;
                default:
                    System.out.println("Erreur : eatAndHunt, EAT");
            }
            if (dinnertime)  {
                prey.remove(dinner);
                this.hunger = 0;
                break;
            }
        }
        

        /* HUNT */
        Prey target = vision.searchPrey(prey);
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
                if (coord[0] == x && directions[orientation])  {                                    //Prey directly in front -> move straight ahead, if possible
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {           
                    if ( coord[0] == ((x+i+width)%width) && directions[right])  {                    //Prey to the right -> move right
                        if ( dice < 0.75 )                                                            //A little bit of randomness to avoid infinite looping interactions between predators and prey
                            return right;
                        else if (directions[orientation])
                            return orientation;
                    }
                    if ( coord[0] == ((x-i+width)%width) && directions[left])  {                    //Prey to the left -> move left
                        if ( dice < 0.75 )
                            return left;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                }
                break;
            case 1:
                if (coord[1] == y && directions[orientation])  {                                  //idem for the other cases
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {
                    if ( coord[1] == ((y+i+height)%height) && directions[left])  {   
                        if ( dice < 0.75 )
                            return left;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                    if ( coord[1] == ((y-i+height)%height) && directions[right] )  {
                        if ( dice < 0.75 )
                            return right;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                }
                break;
            case 2:
                if (coord[0] == x && directions[orientation])  {
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {
                    if ( coord[0] == ((x+i+width)%width) && directions[left] )  {   
                        if ( dice < 0.75 )
                            return left;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                    if ( coord[0] == ((x-i+width)%width) && directions[right])  {
                        if ( dice < 0.75 )
                            return right;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                }
                break;
            case 3:
                if (coord[1] == y && directions[orientation])  {
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {
                    if ( coord[1] == ((y+i+height)%height) && directions[right])  {   
                        if ( dice < 0.75 )
                            return right;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                    if ( coord[1] == ((y-i+height)%height) && directions[left])  {
                        if ( dice < 0.75 )
                            return left;                                                   
                        else if (directions[orientation])
                            return orientation;
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
		if ( world.getIteration() % (100-speed) == 0 )
		{
            
            double dice = Math.random();


            if (accessible == 0)    {   //If no direction is accessible, the agent does not move.
                return;
            }

            vision.setOrientation(orientation);
            vision.setPosition(x, y);
            vision.updateField();

            int move;
            if (hunger > bloodlustThreshold) {
                move = eatAndHunt();    //move in the direction of the prey (0,1,2,3) = (N,E,S,W)
            } else {
                move = -1;
            }
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


            
            switch (move)   {                                                                   //move to the new position
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

            
            for (int i=0; i<directions.length; i++)    {        //reinitialize the four directions to true, making them all accessible 
                directions[i] = true;
            }



        }
    }

}