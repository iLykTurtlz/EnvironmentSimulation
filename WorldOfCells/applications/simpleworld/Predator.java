package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;
import applications.simpleworld.*;
import applications.simpleworld.Weather.Time;
import worlds.World;
import utils.Pool;
import utils.PoolPredator;
import utils.PoolPrey;

import utils.PredatorVision;

public class Predator extends Agent {

    public static final int MAX_LIFESPAN = 1000;
    public static final float INITIAL_HUNGER = 0.f;
    public static final double p_reproduce = 0.01;
    public static final float MAX_HUNGER = 100.f;
    protected enum Sex {MALE, FEMALE};
    protected Sex sex;
    private PredatorVision vision;
    private int rangeOfVision;
    protected int bloodlustThreshold;
    private int gestationPeriod;
    private int gestationStage;
    private boolean pregnant;
    private int offspringCharacters[]; // (rangeOfVision, baseSpeed, bloodlustThreshold, gestationPeriod)


    public Predator( int __x , int __y, WorldOfTrees __world ) {
        super(__x,__y,__world, new float[] {1.f, 0.f, 0.f}, new float[] {1.f,1.f,1.f});
        this.rangeOfVision = 10;
        this.defaultBaseSpeed = 60;
        this.baseSpeed = this.defaultBaseSpeed;
        this.speed = this.baseSpeed;
        this.vision = new PredatorVision(__x,__y,rangeOfVision,orientation,__world);
        this.bloodlustThreshold = 15;
        if (Math.random() < 0.5)    {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
        this.gestationPeriod = 100;
        this.gestationStage = 0;
        this.probablityChangeDirection = 0.1;
        this.pregnant = false;
    }

    public Predator( int __x , int __y, WorldOfTrees __world, int[] offspringCharacters) {
        super(__x,__y,__world, new float[] {1.f, 0.f, 0.f}, new float[] {1.f,1.f,1.f});
        this.orientation = (int)(4*Math.random());      //random orientation by default
        this.rangeOfVision = offspringCharacters[0];
        this.speed = offspringCharacters[1];
        this.vision = new PredatorVision(__x,__y,rangeOfVision,orientation,__world);
        this.bloodlustThreshold = offspringCharacters[2];
        if (Math.random() < 0.5)    {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
        this.gestationPeriod = offspringCharacters[3];
        this.gestationStage = 0;
    }

    
    


    public void step() 
	{
        super.step();
		if ( world.getIteration() % (100-speed) == 0 )
		{
               
            if (hunger >= MAX_HUNGER)   {
                world.removePredator(this);
            }


            if (accessible == 0 || world.getLandscape().getWeather().getTime() == Time.NIGHT )    {   //If no direction is accessible, or if it is nighttime, the agent does not move.
                return;
            }

            vision.setOrientation(orientation);
            vision.setPosition(x, y);
            vision.updateField();

            int move = -1;      //default value
            if (hunger > bloodlustThreshold) {
                move = eatAndHunt();    //move in the direction of the prey (0,1,2,3) = (N,E,S,W), -1 if no prey in view
            }

            if (sex == Sex.FEMALE && pregnant)  {
                move = gestate();
            } 
            else if (move == -1 && age > 100) {
                move = findMate();
            }
            
            double currentHeight = world.getCellHeight(x,y);
            move = updatePosition(move);
            setOrientation(move);
            double nextHeight = world.getCellHeight(x,y);
            updateSpeed(currentHeight, nextHeight);

            
            for (int i=0; i<directions.length; i++)    {        //reinitialize the four directions to true, making them all accessible 
                directions[i] = true;
            }

        }
    }



    private int findMate()  {
        /* Finds the nearest opposite-sex predator, reproduces if possible, otherwise moves in the direction of the prospective mate. */
        PoolPredator predators = world.getPredators();
        Predator mate;
        boolean copulate;

        mate = vision.searchPredator(predators);
        int[] coord;

        while (mate != null && mate.sex == this.sex) {                                     //Find the nearest predator of the opposite sex
            coord = mate.getCoordinate(); 
            mate = vision.searchPredator(predators, coord);
        }
        
        
        if (mate != null)   {                                                                //in this case mate is of the opposite sex
            coord = mate.getCoordinate();      
            copulate = isHere((Agent)mate);
            if (copulate)  {
                this.reproduce(mate);
                return -2;                                                                      //In this case, they should probably hold still for a moment
            }
            return moveToward(coord, 1.f);
        }
        return -1;
    }

    private void reproduce(Predator mate)   {
        double dice = Math.random();
        Predator m,f;
        if (dice > p_reproduce) {
            return;
        }
        if (this.getSex() == Sex.FEMALE)    {
            m = mate;
            f = this;
        } else {
            m = this;
            f = mate;
        }
        f.pregnant = true;
        f.offspringCharacters = new int[4];
        //  TO DO recombine characteristics of both parents
        f.setOffspringCharacters((m.getRangeOfVision() + f.getRangeOfVision())/2, (m.getSpeed() + f.getSpeed())/2, (m.getBloodlustThreshold() + f.getBloodlustThreshold())/2, (m.getGestationPeriod() + f.getGestationPeriod())/2);
        //  TO DO mutation


    }

    private int gestate()  {
        this.gestationStage++;
        if (gestationStage == gestationPeriod)  {
            world.addPredator(this.x, this.y, offspringCharacters);      //TO DO : add arguments to combine traits from both parents.
            this.gestationStage = 0;
            this.pregnant = false;
            return -2;
        }
        return -1;
    }
   

    private int eatAndHunt()    {                   
        /* Finds nearest prey, eats it if possible, 
           otherwise returns the Predator's next move, based on the prey's location, -1 if no prey is seen. */
        PoolPrey prey = world.getPrey();
        boolean dinnertime;

        Prey dinner = vision.searchPrey(prey);      //find nearest prey, null if no prey in field of vision
        if (dinner != null) {
            dinnertime = isHere((Agent)dinner);     //if prey accessible -> eat
            if (dinnertime)  {
                prey.remove(dinner);
                this.hunger = 0;
                return -1;                          //Having eaten, hunting is unnecessary
            }
            int[] coord = dinner.getCoordinate();   //if prey not accessible -> hunt
            this.baseSpeed = defaultBaseSpeed + 10;
            return moveToward(coord, 0.75f);
        }
        this.baseSpeed = this.defaultBaseSpeed;
        return -1;                         
    }


    
    
    private boolean isHere(Agent a) {
        /* Returns true if the Agent, Predator or Prey (requires cast), is directly in front or on the same space.
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

    public void reinitialize()  {
        int[] coord = world.getRandomLandCoordinate();      //random or default values
        this.x = coord[0];
        this.y = coord[1];
        this.orientation = (int)(4*Math.random());           
        this.rangeOfVision = 10;  
        this.baseSpeed = 80;                         
        this.speed = baseSpeed;
        this.vision.setOrientation(this.orientation);
        this.vision.setPosition(this.x, this.y);
        this.vision.updateField();
        this.bloodlustThreshold = 15;
        if (Math.random() < 0.5)    {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
        this.gestationPeriod = 5;
        this.gestationStage = 0;
    }

    public void reinitialize(int x, int y, int[] offspringCharacters) {      
        this.x = x;
        this.y = y;
        this.orientation = (int)(4*Math.random());           
        this.rangeOfVision = offspringCharacters[0];                           
        this.baseSpeed = offspringCharacters[1];
        this.speed = baseSpeed;
        this.vision.setOrientation(this.orientation);
        this.vision.setPosition(this.x, this.y);
        this.vision.updateField();
        this.bloodlustThreshold = offspringCharacters[2];
        if (Math.random() < 0.5)    {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
        this.gestationPeriod = offspringCharacters[3];
        this.gestationStage = 0;
    }

    /* GETTERS AND SETTERS */

    public Sex getSex() {
        return sex;
    }

    public int getRangeOfVision()   {
        return rangeOfVision;
    }

    public int getBaseSpeed()   {
        return baseSpeed;
    }

    public int getSpeed()   {
        return speed;
    }

    public int getBloodlustThreshold()  {
        return bloodlustThreshold;
    }

    public int getGestationPeriod() {
        return gestationPeriod;
    }

    public void setOrientation(int move) {
        /* sets predator orientation based on direction of movement:
            -2 -> no change
        */
        if (move != -2)
            this.orientation = move;
    }

    public void setRangeOfVision(int rangeOfVision)   {
        this.rangeOfVision = rangeOfVision;
    }

    public void setSpeed(int speed)   {
        this.speed = speed;
    }

    public void setBloodlustThreshold(int bloodlustThreshold)  {
        this.bloodlustThreshold = bloodlustThreshold;
    }

    public void setGestationPeriod(int gestationPeriod) {
        this.gestationPeriod = gestationPeriod;
    }

    public void setOffspringCharacters(int rangeOfVision, int speed, int bloodlustThreshold, int gestationPeriod)    {
        offspringCharacters[0] = rangeOfVision;
        offspringCharacters[1] = speed;
        offspringCharacters[2] = bloodlustThreshold;
        offspringCharacters[3] = gestationPeriod;
    }

}
