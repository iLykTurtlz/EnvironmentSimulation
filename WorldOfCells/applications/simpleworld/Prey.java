package applications.simpleworld;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import applications.simpleworld.Weather.Time;
import objects.UniqueDynamicObject;

import worlds.World;
import utils.DisplayToolbox;
import utils.PoolPredator;
import utils.PreyVision;

public class Prey extends Agent {

    public static final int MAX_LIFESPAN = 1000;
    public static final int MAX_HUNGER = 1000;
    public static final int MAX_FATIGUE = 1000;

    public static final double P_REPRODUCTION = 0.001;

    protected final int maxColorChangePeriod = 30;
    protected int colorCounter;

    private PreyVision vision;
    private int rangeOfVision;
    private Plant food;
    private int appetiteThreshold;

    

    public Prey( int __x , int __y, WorldOfTrees __world ) {
        super(__x,__y,__world, new float[] {0.f,0.75f,1.f}, new float[] {1.f,1.f,1.f});     // new Agent with azure head and white body

        defaultBaseSpeed = 55;
        baseSpeed = defaultBaseSpeed;
        speed = baseSpeed;

        rangeOfVision = 4;
        vision = new PreyVision(__x,__y,rangeOfVision,__world);
        probablityChangeDirection = 0.1;
        appetiteThreshold = 100;
        food = null;

    }


    public void step() 
	{
        super.step();

		if ( world.getIteration() % (100 - speed) == 0 )
		{
            double dice = Math.random();

             //If the Prey is too old or too hungry, it dies
             if (hunger >= MAX_HUNGER || age >= MAX_LIFESPAN)   {
                state = State.DEAD;
            }

            //If the Prey is dead, remove it
            if (state == State.DEAD)    {
                world.getPrey().remove(this);
            }


            //this variable stores a value that will ultimately determine the Prey's next move
            // (0,1,2,3) = (N,E,S,W)
            // -1 = not yet determined
            // -2 = stay in place
            int move = -1;

       

            //If no direction is accessible the Prey does not move.
            if (accessible == 0)    { 
                move = -2;
            }

            //If the Prey is on fire, it rampages, regardless of fatigue or the time of day
            if (move == -1 && state == State.ON_FIRE) {
                rampage();
                return; //nothing else to do in this case.
            }

            //If the Prey is too tired to move AND it is nighttime, it does not move.
            if (  move == -1 && (fatigue == MAX_FATIGUE && world.getLandscape().getWeather().getTime() == Time.NIGHT)  )    {
                move = -2;
            }

            //If the Prey can move freely, it must first flee from any Predator it may see.
            if ( move == -1)  {
                vision.setPosition(x, y);
                vision.updateField();
                move = flee();          
            }

            //If the Prey has already found food ( a side effect of calling findFood() ), it pursues food.
            if (move == -1 && food != null)  {
                move = pursueFood();     // returns (0,1,2,3) or -1 if the destination is out of range or inaccessible
            }

            //If the Prey is hungry, it looks for food then pursues it, otherwise it has a chance to reproduce.
            if (move == -1) {

                if (hunger > appetiteThreshold) {
                    findFood();
                    if (food != null)   {
                        move = pursueFood();
                    }

                } else {
                    if (dice < P_REPRODUCTION)  {
                        reproduce();
                    }
                }
            }

            //Update position based on the value of move.  That means explore if move is still -1.
            move = updatePosition(move);
            setOrientation(move);   //the Prey's orientation matches its direction of movement.
            
            //reset the four directions to true
            reinitializeDirections();
        }
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

      
        // if the Prey is on fire, its body is orange.
        if ( this.state == State.ON_FIRE )  {
            bodyColor[0] = 1.f;
            bodyColor[1] = 0.5f;
            bodyColor[2] = 0;
        }
        // if the Prey psychedelic, its body changes color every turn for a while.
        else if (this.state == State.PSYCHEDELIC)   {
        
            if (colorCounter < maxColorChangePeriod) {
                if ( world.getIteration() % (100 - speed) == 0 )    {
                    DisplayToolbox.incrementRainbow(bodyColor);
                    colorCounter++;
                }
            
            //Time is up    
            } else {
                state = State.ALIVE;
                colorCounter = 0;
                for (int i=0; i<bodyColor.length; i++)  {
                    bodyColor[i] = 1.f;
                }
            }
        }
        

        //Here we draw the body
        float bandThicknessNorm = 1.f/10;
        float r1,r2;
        int i;
        gl.glColor3f(bodyColor[0], bodyColor[1], bodyColor[2]);
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

    private int flee()  {
        //local variables for use later in the method
        PoolPredator predators = world.getPredators();
        int height = world.getHeight();
        int width = world.getWidth();

        //search for the nearest threat
        Predator threat = vision.searchPredator(predators);

        if (threat == null) {   // no predator spotted in the field of vision -> return -1 to allow random displacement.
            return -1;
        }

        int[] coord = threat.getCoordinate();
        for (int i=0; i<rangeOfVision; i++) {                           //Tests of predator's location in counterclockwise sequence (to compensate for the prey's field of vision giving priority in CLOCKWISE direction)
            
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

    private void findFood() {
        // This function searches the field of vision for an edible Plant.
        ArrayList<Plant> plants = world.getPlants();
        food = vision.searchPlant(plants);      //find nearest plant, null if no prey in field of vision           
    }




    private void eat()  {
        //This function can be called from pursueFood() when the prey is within reach of its meal.
        //food must not be null.
        if (food == null)   {
            System.err.println("Erreur : Prey eat(), food est null");
            return;
        }

        food.reduceSize();
            hunger = 0;

            if (food.getClass() ==  Mushroom.class)  {
                this.state = State.PSYCHEDELIC;
                colorCounter = 0;
                bodyColor[0] = 1.f;
                bodyColor[1] = 0.f;
                bodyColor[2] = 0.f;
            }
    }


    private int pursueFood() {
        //This function should be called AFTER testing that the attribute food is not null.
        //The Prey eats food, if possible, otherwise moves toward it.
        if (food == null)    {
            System.err.println("Erreur : pursueFood(), food est null");
            return -1;
        }

        int[] coord = food.getCoordinate();

        //If food is within reach, the Prey eats.
        if ( isAdjacent(coord) )  {
            eat();
            return -2;
        }


        // Otherwise it moves toward the food
        int height = world.getHeight();
        int width = world.getWidth();


        //We want to return the direction encountered first as we traverse the x and y coordinates from the maximum distance the Plant could have been spotted (x +/- rangeOfVision, y +/- rangeOfVision) to the von Neumann neighborhood.
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
        return -1;
    }
    
    private void reproduce()    {
        world.addPrey(this.x, this.y);
    }

    public void reinitialize()  {
        super.reinitialize();
    }

    

}
