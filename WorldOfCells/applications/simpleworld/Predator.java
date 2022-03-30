package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;
import utils.Pool;
import utils.PoolPredator;
import utils.PoolPrey;

import utils.PredatorVision;

public class Predator extends Agent {

    public static final double p_reproduce = 0.01;
    protected enum Sex {MALE, FEMALE};
    protected Sex sex;
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
        if (Math.random() < 0.5)    {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
    }

    
    private int findMate()  {
        PoolPredator predators = world.getPredators();
        Predator mate;
        boolean copulate;

        mate = vision.searchPredator(predators);
        int[] coord;
        while (mate != null && mate.getSex() == this.sex) {                                     //Find the nearest predator of the opposite sex
            coord = mate.getCoordinate(); 
            mate = vision.searchPredator(predators, coord);
        }
        coord = mate.getCoordinate();
        if (mate != null)   {                                                                   //in this case mate is of the opposite sex
            copulate = isHere((Agent)mate);
            if (copulate)  {
                this.reproduce(mate);
                return -2;                                                                      //In this case, they should probably hold still for a moment
            }
        }
        return moveToward(coord, 1.f);
    }

    private void reproduce(Predator mate)   {

    }
   

    private int eatAndHunt()    {            //returns the Predator's next move, based on the prey's location, -1 if no prey is seen.
        PoolPrey prey = world.getPrey();
        boolean dinnertime;

        //int[][] field = vision.getField();

        /* EAT */
        Prey dinner = vision.searchPrey(prey);
        if (dinner != null) { 
            dinnertime = isHere((Agent)dinner);
        /*
        Prey dinner;
        for (int i=0; i<prey.getSizeUsed(); i++)    {
            dinner = prey.get(i);
            int[] coord = dinner.getCoordinate();
            boolean dinnertime = isHere((Agent))
            
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

            */
            if (dinnertime)  {
                prey.remove(dinner);
                this.hunger = 0;
                return -1;                      //Having eaten, hunting is unnecessary
            }
        
        

            /* HUNT */
            /*
            Prey target = vision.searchPrey(prey);
            if (target == null) {
                return -1;
            }
            */
            int[] coord = dinner.getCoordinate();
            return moveToward(coord, 0.75f);
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

    
    private boolean isHere(Agent a) {
        /* Returns true if the Agent, Predator or Prey, is directly in front or on the same space.
           Otherwise returns false. */
        int[] coord = a.getCoordinate();
        int height = world.getHeight();
        int width = world.getWidth();
        if (x == coord[0] && y == coord[1])                                                 //Same space -> true
            return true;
        
        switch (orientation)    {                                                           //Space directly in front -> true
            case 0:
                if (coord[0] == x && coord[1] == ((y + 1 + height)%height))  {
                    return true;
                }
                break;
            case 1:
                if (coord[0] == ((x + 1 + width)%width) && coord[1] == y)  {
                    return true;
                }
                break;
            case 2:
                if (coord[0] == x && coord[1] == ((y - 1 + height)%height))  {
                    return true;
                }
                break;
            case 3:
                if (coord[0] == ((x - 1 + width)%width) && coord[1] == y)  {
                    return true;
                }
                break;
            default:
                System.out.println("Erreur : findMate");
        }
        return false;                                                                           //Otherwise false
    }
    


    private int moveToward(int[] coord, float p_rightOrLeft)    {
        /* This function takes in a target coordinate and a probability of moving right or left if the target is NOT directly ahead.
           Returns the int corresponding to the direction of movement or -1 for random displacement. */
        int height = world.getHeight();
        int width = world.getWidth();
        int right = (orientation + 1 + 4) % 4;
        int left = (orientation - 1 + 4) % 4;
        double dice = Math.random();
        
        switch (orientation)    {
            case 0:
                if (coord[0] == x && directions[orientation])  {                                    //directly in front -> move straight ahead, if possible
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {           
                    if ( coord[0] == ((x+i+width)%width) && directions[right])  {                   //to the right -> move right
                        if ( dice < p_rightOrLeft )                                                 //A little bit of randomness to avoid infinite looping interactions
                            return right;
                        else if (directions[orientation])
                            return orientation;
                    }
                    if ( coord[0] == ((x-i+width)%width) && directions[left])  {                    //to the left -> move left
                        if ( dice < p_rightOrLeft )
                            return left;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                }
                break;
            case 1:
                if (coord[1] == y && directions[orientation])  {                                   //idem for the other cases
                    return orientation;
                }
                for (int i=1; i<=rangeOfVision; i++)    {
                    if ( coord[1] == ((y+i+height)%height) && directions[left])  {   
                        if ( dice < p_rightOrLeft )
                            return left;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                    if ( coord[1] == ((y-i+height)%height) && directions[right] )  {
                        if ( dice < p_rightOrLeft )
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
                        if ( dice < p_rightOrLeft )
                            return left;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                    if ( coord[0] == ((x-i+width)%width) && directions[right])  {
                        if ( dice < p_rightOrLeft )
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
                        if ( dice < p_rightOrLeft )
                            return right;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                    if ( coord[1] == ((y-i+height)%height) && directions[left])  {
                        if ( dice < p_rightOrLeft )
                            return left;                                                   
                        else if (directions[orientation])
                            return orientation;
                    }
                }
                break;
            default:
                System.out.println("Erreur : orientation, moveToward");
        }
        return -1;
    }



    public Sex getSex() {
        return sex;
    }

}