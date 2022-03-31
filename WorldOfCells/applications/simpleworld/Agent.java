// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import javax.lang.model.util.ElementScanner14;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

public abstract class Agent extends UniqueDynamicObject{


    public static final int MAX_LIFESPAN = 1000;
    public static final float INITIAL_HUNGER = 100.f;
    protected int baseSpeed;
    protected int speed;                    // between 0 and 100
    protected int age;
    protected enum State {ALIVE, DEAD, ONFIRE};
    protected State state;
    protected int hunger;



    protected boolean directions[];          // true if the direction is accessible, false otherwise : indices (0,1,2,3) = (N,E,S,W)
    protected int accessible;                // number of accessible directions
    protected float[] headColor;             // to distinguish different types of agents
	
    public Agent ( int __x , int __y, WorldOfTrees __world, float[] headColor )
	{
		super(__x,__y,__world);
        directions = new boolean[4];    // above, right, below, left; in that order
        for (int i=0; i<directions.length; i++) {
            directions[i] = true;
        }

        this.age = 0;
        this.state = State.ALIVE;
        this.hunger = 0;
        this.headColor = headColor;
	}


	
	public void step() {

        if ( world.getIteration() % (100 - speed) == 0 )   {

            this.updateAge();
            this.updateHunger();
            if (this.age >= Agent.MAX_LIFESPAN)  {
                this.state = State.DEAD;
            }

            this.accessible = 4;

            /* Indices of squares in four directions relative to the agent's orientation */
            int right = (this.x + 1 + this.world.getWidth()) % this.world.getWidth();
            int left = (this.x - 1 + this.world.getWidth()) % this.world.getWidth();
            int above = (this.y + 1 + this.world.getHeight()) % this.world.getHeight();
            int below = (this.y - 1 + this.world.getHeight()) % this.world.getHeight();

            
            /* Block off directions that are impassable or dangerous */
            /* And determine the number of remaining accessible directions */
            double hAbove = this.world.getCellHeight(this.x, above), 
                hRight = this.world.getCellHeight(right, this.y),
                hBelow = this.world.getCellHeight(this.x, below), 
                hLeft  = this.world.getCellHeight(left, this.y),
                hThis  = this.world.getCellHeight(this.x,this.y);

            /* Block off water and cliffs */       
            if ( (hAbove < 0) || (Math.abs(hAbove - hThis)) > 0.1)    {
                directions[0] = false;
                accessible--;
            }
            if ( (hRight < 0) || (Math.abs(hRight - hThis) > 0.1) )   {
                directions[1] = false;
                accessible--;
            }
            if ( (hBelow < 0) || (Math.abs(hBelow - hThis) > 0.1) )    {
                directions[2] = false;
                accessible--;
            }
            if ( (hLeft < 0) || (Math.abs(hLeft - hThis) > 0.1) )      {
                directions[3] = false;
                accessible--;
            }
        }


    }
	

            
/*          //OLD MOVEMENT - Agents move randomly, including on water
            this.world.getCellHeight(this.x + 1, this.y);
			if ( dice < 0.25 )
				this.x = ( this.x + 1 ) % this.world.getWidth() ;
			else
				if ( dice < 0.5 )
					this.x = ( this.x - 1 +  this.world.getWidth() ) % this.world.getWidth() ;
				else
					if ( dice < 0.75 )
						this.y = ( this.y + 1 ) % this.world.getHeight() ;
					else
						this.y = ( this.y - 1 +  this.world.getHeight() ) % this.world.getHeight() ;
		}
	}
*/    


    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)
    {

        // display a monolith
        
        //gl.glColor3f(0.f+(float)(0.5*Math.random()),0.f+(float)(0.5*Math.random()),0.f+(float)(0.5*Math.random()));
        
    	int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

    	float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

    	float zoff = myWorld.getLandscape().getZOffset();

        gl.glColor3f(1.f,1.f,1.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight  + zoff+ 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff);

        gl.glColor3f(1.f,1.f,1.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff);

        gl.glColor3f(1.f,1.f,1.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + zoff);

        gl.glColor3f(1.f,1.f,1.f);
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
        this.age = 0;
        this.state = State.ALIVE;
    }



    /* GETTERS AND SETTERS */

    public void updateAge()   {
        this.age++;
    }

    public void updateHunger()  {
        this.hunger++;
    }

    public void updateSpeed(double currentHeight, double nextHeight)    {
        /* adjusts speed based on topography : uphill -> slower, downhill ->faster */
        double diff = nextHeight - currentHeight;
        if (diff > 0.03)   
            speed = baseSpeed + 10;
        else if (diff > 0.02)   
            speed = baseSpeed + 5;
        else if (diff > 0.01) 
            speed = baseSpeed + 2;
        else if (diff > 0.005) 
            speed = baseSpeed + 1;
        else if (diff > -0.005)   
            speed = baseSpeed;
        else if (diff > -0.01)   
            speed = baseSpeed - 1;
        else if (diff > -0.02)  
            speed = baseSpeed - 2;
        else if (diff > -0.03)   
            speed = baseSpeed -5;
        else 
            speed = baseSpeed -10;
    }

    public int updatePosition(int move)    {
        /* Updates the agent's position based on an int value:
            -1 -> random displacement
            -2 -> no change
            (0,1,2,3) -> (N,E,S,W)
            Returns the final value of move, which is needed to set predator orientation.
        */

        //Random movements
        double dice = Math.random();
        if (move == -1) {           
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
            if (move == -1)  {      //In rare cases, double comparison fails and move is still -1.  When that happens, the move should be the LAST accessible direction in directions.
                for (int i = directions.length - 1; i > -1; i--)    {
                    if (directions[i])  {
                        move = i;
                        break;
                    }
                }
            }
        }

        //Having determined the random direction (if applicable) we now set the agent's position
        switch (move)   {   
            case -2:
                break; 
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
                System.out.println("Erreur de déplacement : updatePosition(),move = " + move);
        }
        return move;
    }

    public int getAge()    {
        return this.age;
    }

    public State getState()  {
        return this.state;
    }

 
}
