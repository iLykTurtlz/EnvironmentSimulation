package applications.simpleworld;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import applications.simpleworld.Weather.Time;
import objects.UniqueDynamicObject;

import worlds.World;
import utils.PoolPredator;
import utils.PreyVision;

public class Prey extends Agent {

    public static final int MAX_LIFESPAN = 1000;
    public static final float INITIAL_HUNGER = 100.f;
    public static final double P_REPRODUCTION = 0.001;

    protected final int maxColorChangePeriod = 30;
    protected int colorCounter;

    private PreyVision vision;
    private int rangeOfVision;

    

    public Prey( int __x , int __y, WorldOfTrees __world ) {
        super(__x,__y,__world, new float[] {0.f,0.75f,1.f}, new float[] {1.f,1.f,1.f});
        this.rangeOfVision = 5;
        this.defaultBaseSpeed = 55;
        this.baseSpeed = this.defaultBaseSpeed;
        this.speed = this.baseSpeed;
        this.vision = new PreyVision(__x,__y,rangeOfVision,__world);
        this.probablityChangeDirection = 0.2;
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

    private int graze() {
        ArrayList<Plant> plants = world.getPlants();
        //double dice = Math.random();

        Plant p = vision.searchPlant(plants);
        if (p == null)  {
            return -1;      //random displacement if no plant in view
        }

        int[] coord = p.getCoordinate();
        int height = world.getHeight();
        int width = world.getWidth();

        if (Math.abs( (coord[0]-x+world.getWidth())%world.getWidth() ) < 2 &&  Math.abs( (coord[1]-y+world.getHeight())%world.getHeight() ) < 2)    {
            //TO DO : reduce plant size and prey hunger
            p.decrementSize();
            hunger-=10;

            this.state = State.PSYCHEDELIC;
            colorCounter = 0;
            bodyColor[0] = 1.f;
            bodyColor[1] = 0.f;
            bodyColor[2] = 0.f;

            return -2;      //if the prey is eating, it stays until it is no longer hungry or the plant has been completely eaten.
        }

        for (int i=2; i<rangeOfVision; i++) {   //the case i < 2 has already been addressed
            if (coord[1] == (y+i+height)%height && directions[0])
                return 0;
            if (coord[0] == (x+i+width)%width && directions[1])
                return 1;
            if (coord[1] == (y-i+height)%height && directions[2])
                return 2;
            if (coord[0] == (x-i+width)%width && directions[3])
                return 3;
        }
        return -1;
    }
    
    private void reproduce()    {
        world.addPrey(this.x, this.y);
    }

    public void step() 
	{
        super.step();

        double dice = Math.random();

		if ( world.getIteration() % (100 - speed) == 0 )
		{
            
            if (accessible == 0 || world.getLandscape().getWeather().getTime() == Time.NIGHT )    {           //If no direction is accessible, or if it is nighttime the agent does not move.
                return;
            }


            vision.setPosition(x, y);
            vision.updateField();
            

            int move = flee();                  //Look for predators and flee from the nearest one

            if (move == -1 && hunger > 20)  {   //If there is no threat in view, the prey can look for food.
                move = graze();
            } else {
                if (dice < P_REPRODUCTION)
                    reproduce();
            }

            double currentHeight = world.getCellHeight(x,y);
            updatePosition(move);
            double nextHeight = world.getCellHeight(x,y);
            updateSpeed(currentHeight, nextHeight);

            /* Reset the four directions to true*/
            for (int i=0; i<directions.length; i++)    {
                directions[i] = true;
            }
        }
    }


    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)
    {
        
        //gl.glColor3f(0.f+(float)(0.5*Math.random()),0.f+(float)(0.5*Math.random()),0.f+(float)(0.5*Math.random()));
        
    	int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

    	float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

    	float zoff = myWorld.getLandscape().getZOffset();


        if (this.state == State.PSYCHEDELIC && colorCounter < maxColorChangePeriod) {
            if (world.getIteration() % 20 == 0) {
                Mushroom.incrementColor(bodyColor);
                colorCounter++;
            }
        }
        else if (this.state == State.PSYCHEDELIC)   {
            state = State.ALIVE;
            colorCounter = 0;
            for (int i=0; i<bodyColor.length; i++)  {
                bodyColor[i] = 1.f;
            }
        }

        gl.glColor3f(bodyColor[0],bodyColor[1],bodyColor[2]);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight  + zoff+ 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff);

        gl.glColor3f(bodyColor[0],bodyColor[1],bodyColor[2]);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff);

        gl.glColor3f(bodyColor[0],bodyColor[1],bodyColor[2]);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff);

        gl.glColor3f(bodyColor[0],bodyColor[1],bodyColor[2]);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff);

        gl.glColor3f(headColor[0],headColor[1],headColor[2]);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff + 5.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 5.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 5.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff + 5.f);
    }

    public void reinitialize()  {
        super.reinitialize();
    }

    

}
