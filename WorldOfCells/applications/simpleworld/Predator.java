package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;
import applications.simpleworld.*;
import applications.simpleworld.Weather.Time;
import worlds.World;
import utils.DisplayToolbox;
import utils.Pool;
import utils.PoolPredator;
import utils.PoolPrey;

import utils.PredatorVision;

public class Predator extends Agent {

    public static final int MAX_LIFESPAN = 1000;
    public static final double P_REPRODUCTION = 0.6;
    public static final int MAX_HUNGER = 10000;
    public static final int MAX_FATIGUE = 1000;
    
    protected enum Sex {MALE, FEMALE};
    protected Sex sex;
    private PredatorVision vision;
    private int rangeOfVision;
    protected Predator mate;         // food and mate serve as the Predator's memory.  Without storing the destination, the Predators would have looping behavior as they shift back and forth between two or more target destinations.
    protected Prey food;
    protected int appetiteThreshold;
    private int gestationPeriod;
    private int gestationStage;
    private boolean pregnant;
    private int offspringTraits[]; // (rangeOfVision, baseSpeed, appetiteThreshold, gestationPeriod)


    public Predator( int __x , int __y, WorldOfTrees __world ) {
        super(__x,__y,__world, new float[] {1.f, 0.f, 0.f}, new float[] {0.25f,0.25f,0.25f});   // new Agent with red head and gray body
        rangeOfVision = 6;
        
        defaultBaseSpeed = 70;
        baseSpeed = defaultBaseSpeed;
        speed = baseSpeed;

        vision = new PredatorVision(__x,__y,rangeOfVision,orientation,__world);
        appetiteThreshold = 100;
        if (Math.random() < 0.5)    {
            sex = Sex.MALE;
        } else {
            sex = Sex.FEMALE;
        }
        gestationPeriod = 10;
        gestationStage = 0;                             //for the males this value will always be 0, for the females it will increment to gestationPeriod during pregnancy
        pregnant = false;
        mate = null;
        food = null;
    }

    public Predator( int __x , int __y, WorldOfTrees __world, int[] offspringTraits) {
        super(__x,__y,__world, new float[] {1.f, 0.f, 0.f}, new float[] {1.f,1.f,1.f});
        this.orientation = (int)(4*Math.random());      //random orientation by default
        this.rangeOfVision = offspringTraits[0];
        this.speed = offspringTraits[1];
        this.vision = new PredatorVision(__x,__y,rangeOfVision,orientation,__world);
        this.appetiteThreshold = offspringTraits[2];
        if (Math.random() < 0.5)    {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
        this.gestationPeriod = offspringTraits[3];
        this.gestationStage = 0;
        mate = null;
        food = null;
    }

    
    


    public void step() 
	{
        super.step();
		if ( world.getIteration() % (100-speed) == 0 )
		{
            //If the Predator is no longer on fire
            if (state == State.ALIVE)    {
                resetColors();
            }

            //If the predator is too old or too hungry, it dies
            if (hunger >= MAX_HUNGER || age >= MAX_LIFESPAN)   {
                state = State.DEAD;
            }

            //If the predator is dead, remove it
            if (state == State.DEAD)    {
                world.getPredators().remove(this);
            }


            //this variable stores a value that will ultimately determine the Predator's next move
            // (0,1,2,3) = (N,E,S,W)
            // -1 = not yet determined
            // -2 = stay in place
            int move = -1;

            //If the Predator is female and pregnant, we need to call gestate() to increment gestationStage, and possibly not move
            if (sex == Sex.FEMALE && pregnant)  {
                move = gestate();   //returns -2 if the Predator is giving birth, -1 otherwise
            }

            //If no direction is accessible the agent does not move.
            if (accessible == 0)    { 
                move = -2;
            }

            //If the predator is on fire, it rampages, regardless of fatigue or the time of day
            if (move == -1 && state == State.ON_FIRE) {
                rampage();
                return; //nothing else to do in this case.
            }

            //If the predator is too tired to move AND it is nighttime, the predator does not move.
            if (  move == -1 && (fatigue == MAX_FATIGUE && world.getLandscape().getWeather().getTime() == Time.NIGHT)  )    {
                move = -2;
            }

            //If the predator has already found food ( a side effect of calling findFood() ), it hunts.
            if (move == -1 && food != null)  {
                move = hunt();     // returns (0,1,2,3) or -1 if the destination is out of range or inaccessible
            }

            //If the predator is pursuing a mate
            if (move == -1 && mate != null) {
                move = pursueMate();
            }

            //If move is still -1, the predator looks for something to do.
            if (move == -1)  {

                //First update the vision field, which will be used for the search
                vision.setOrientation(orientation);
                vision.setPosition(x, y);
                vision.updateField();

                //if the predator is hungry ENOUGH, it must look for food; otherwise it can look for a mate
                if (hunger >= appetiteThreshold) {

                    findFood();    
                    if (food != null)   {
                        move = hunt();  //returns (0,1,2,3) or -1 if no prey in view
                    } 


                } else {

                    findMate();
                    if (mate != null)   {
                        move = pursueMate(); //returns (0,1,2,3) or -1 if no mate in view
                    }
                    
                }
            }

        

            //Update position based on the value of move.  That means explore if move is still -1.
            move = updatePosition(move);
            setOrientation(move);   //the Predator's orientation matches its direction of movement.
            
            reinitializeDirections();
            
        }
    }


    private void findFood()   {
        // This function searches the field of vision for a Prey.
        PoolPrey prey = world.getPrey();
        food = vision.searchPrey(prey);      //find nearest prey, null if no prey in field of vision                
    }

    private void findMate() {
        // This function searches the field of vision for a Predator of the opposite sex.
        PoolPredator predators = world.getPredators();
        Predator prospectiveMate = vision.searchPredator(predators);
        if (prospectiveMate != null && prospectiveMate.sex != this.sex) {
            mate = prospectiveMate;
        }
    }

    private int pursueMate()    {
        // This function should be called AFTER verifying that mate is not null.
        // 
        if (mate == null)   {
            System.err.println("Erreur : pursueMate, mate est null.");
        }

        int[] coord = mate.getCoordinate();

        //If the mate is within reach, the Predator reproduces.
        if ( isAdjacent(coord) )  {
            reproduce();
            return -2;
        }

        //Otherwise the Predator pursues its prospective mate, picking up speed.
        speed = baseSpeed + 20;
        int height = world.getHeight();
        int width = world.getWidth();
    
        //We want to return the direction encountered first as we traverse the x and y coordinates from the maximum distance the prey could have been spotted (x +/- rangeOfVision, y +/- rangeOfVision) to the von Neumann neighborhood.
        //We first test the direction : if it is inaccessible, there is no reason to test the equality of the x- or y-coordinate.
        for (int i=rangeOfVision; i>0; i--)    {
            if ( directions[0] && coord[1] == (y+i+height)%height )   {
                return 0;
            }
            if ( directions[1] && coord[0] == (x+i+width)%width )  {
                return 1;
            }
            if ( directions[2] && coord[1] == (y-i+height)%height )  {
                return 2;
            }
            if ( directions[3] && coord[0] == (x-i+width)%width )    {
                return 3;
            }
        }

        //the prospective mate is either too far away or inaccessible due to an obstacle; it should therefore be abandoned.
        mate = null; 
        speed = baseSpeed;
        return -1;
    
    }



    private void reproduce()   {
        // The attribute mate must not be null.
        // This function begins the gestation period for the female Predator and sets the future offspring's traits.
        // TO DO : genetics
        if (mate == null)   {
            System.err.println("Erreur : Predator reproduce, mate est null");
            return;
        }
        double dice = Math.random();
        Predator m,f;
        if (dice > P_REPRODUCTION) {
            return;
        }
        if (this.sex == Sex.FEMALE)    {
            m = mate;
            f = this;
        } else {
            m = this;
            f = mate;
        }
        f.pregnant = true;
        f.offspringTraits = new int[4];
        //  TO DO recombine characteristics of both parents
        f.setOffspringTraits((m.getRangeOfVision() + f.getRangeOfVision())/2, (m.getSpeed() + f.getSpeed())/2, (m.getAppetiteThreshold() + f.getAppetiteThreshold())/2, (m.getGestationPeriod() + f.getGestationPeriod())/2);
        //  TO DO mutation


    }

    private int gestate()  {
        this.gestationStage++;
        if (gestationStage >= gestationPeriod)  {
            world.addPredator(this.x, this.y, offspringTraits);      //TO DO : add arguments to combine traits from both parents.
            gestationStage = 0;
            pregnant = false;
            speed = baseSpeed;
            System.out.println("A birth has occurred");
            return -2;
        }
        return -1;
    }
   


    private void eat()  {
        //The attribute food must not be null
        //This function kills the Predator's victim and resets hunger, speed and the Predator's memory : the attribute, food.
        if (food == null)   {
            System.err.println("Erreur : Predator eat(), food est null");
            return;
        }
        food.setState(State.DEAD);
        hunger = 0;
        speed = baseSpeed;
        food = null;
    }


    private int hunt() {
        //This function should be called AFTER testing that the attribute food is not null.
        //The Predator eats food, if possible, otherwise moves toward it.
        if (food == null)    {
            System.err.println("Erreur: moveTowardDestination, food est null");
            return -1;
        }

        int[] coord = food.getCoordinate();

        //If food is within reach, the predator eats.
        if ( isAdjacent(coord) )  {
            eat();
            return -2;
        }

        
        //Otherwise the predator pursues food.
        int height = world.getHeight();
        int width = world.getWidth();
    
        //We want to return the direction encountered first as we traverse the x and y coordinates from the maximum distance the prey could have been spotted (x +/- rangeOfVision, y +/- rangeOfVision) to the von Neumann neighborhood.
        //We first test the direction : if it is inaccessible, there is no reason to test the equality of the x- or y-coordinate.
        for (int i=rangeOfVision; i>0; i--)    {
            if ( directions[0] && coord[1] == (y+i+height)%height )   {
                return 0;
            }
            if ( directions[1] && coord[0] == (x+i+width)%width )  {
                return 1;
            }
            if ( directions[2] && coord[1] == (y-i+height)%height )  {
                return 2;
            }
            if ( directions[3] && coord[0] == (x-i+width)%width )    {
                return 3;
            }
        }

        //the food is either too far away or inaccessible due to an obstacle; it should therefore be abandoned.
        food = null; 
        speed = baseSpeed;
        return -1;
    }


    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)
    {
        
    	int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

    	float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

    	float zoff = myWorld.getLandscape().getZOffset();

        float altitude = height*normalizeHeight + zoff;

        if (state == State.ON_FIRE)  {
            bodyColor[0] = 1.f;
            bodyColor[1] = 0.5f;
            bodyColor[2] = 0;
        }

        //Here we draw the body
        float bandThicknessNorm = 1.f/10;
        float r1,r2;
        int i;
        gl.glColor3f(bodyColor[0],bodyColor[1],bodyColor[2]);
        for (i=0; i<10; i++)    {
            r1 = calculateRadius(  bandThicknessNorm * (i)  );
            r2 = calculateRadius(  bandThicknessNorm * (i+1)  );
            DisplayToolbox.drawOctagonalPrism(r1,r2, bandThicknessNorm * scalingFactor * i, bandThicknessNorm * scalingFactor * (i+1), altitude,x2,y2,myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //Now we draw the head
        float baseHeight = bandThicknessNorm * scalingFactor * i;   //This allows us to continue the drawing starting from the same altitude where we stopped drawing the body (from the bottom up)
        float headScalingFactor = 2.f;
        gl.glColor3f(headColor[0],headColor[1],headColor[2]);
        for (int j=0; j<10; j++)    {
            r1 = headScalingFactor * calculateSphereRadius( bandThicknessNorm * j );
            r2 = headScalingFactor * calculateSphereRadius( bandThicknessNorm * (j+1));
            DisplayToolbox.drawOctagonalPrism(r1,r2, baseHeight + bandThicknessNorm * headScalingFactor * j, baseHeight + bandThicknessNorm * headScalingFactor * (j+1), altitude,x2,y2,myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }
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
        this.appetiteThreshold = 15;
        if (Math.random() < 0.5)    {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
        this.gestationPeriod = 5;
        this.gestationStage = 0;
    }

    public void reinitialize(int x, int y, int[] offspringTraits) {      
        this.x = x;
        this.y = y;
        this.orientation = (int)(4*Math.random());           
        this.rangeOfVision = offspringTraits[0];                           
        this.baseSpeed = offspringTraits[1];
        this.speed = baseSpeed;
        this.vision.setOrientation(this.orientation);
        this.vision.setPosition(this.x, this.y);
        this.vision.updateField();
        this.appetiteThreshold = offspringTraits[2];
        if (Math.random() < 0.5)    {
            this.sex = Sex.MALE;
        } else {
            this.sex = Sex.FEMALE;
        }
        this.gestationPeriod = offspringTraits[3];
        this.gestationStage = 0;
    }



    /* GETTERS AND SETTERS */
    public void resetColors()   {
        bodyColor[0] = 0.25f;
        bodyColor[1] = 0.25f;
        bodyColor[2] = 0.25f;
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

    public int getAppetiteThreshold()  {
        return appetiteThreshold;
    }

    public int getGestationPeriod() {
        return gestationPeriod;
    }


    public void setRangeOfVision(int rangeOfVision)   {
        this.rangeOfVision = rangeOfVision;
    }

    public void setSpeed(int speed)   {
        this.speed = speed;
    }

    public void setAppetiteThreshold(int appetiteThreshold)  {
        this.appetiteThreshold = appetiteThreshold;
    }

    public void setGestationPeriod(int gestationPeriod) {
        this.gestationPeriod = gestationPeriod;
    }

    public void setOffspringTraits(int rangeOfVision, int speed, int appetiteThreshold, int gestationPeriod)    {
        offspringTraits[0] = rangeOfVision;
        offspringTraits[1] = speed;
        offspringTraits[2] = appetiteThreshold;
        offspringTraits[3] = gestationPeriod;
    }

}
