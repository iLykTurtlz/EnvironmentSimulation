// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

public abstract class Agent extends UniqueDynamicObject{


    public static final int MAX_LIFESPAN = 100;
    public static final float INITIAL_ENERGY = 100.f;
    protected float energy;
    protected int age;
    protected enum State {ALIVE, DEAD};
    protected State state;



    protected boolean orientation[];         // (0,1,2,3) = (nord,est,sud,ouest)
    protected boolean directions[];          // idem
    protected int accessible;
    protected float[] headColor;
	
    public Agent ( int __x , int __y, World __world, float[] headColor )
	{
		super(__x,__y,__world);
        directions = new boolean[4];    // above, right, below, left; in that order
        for (int i=0; i<directions.length; i++) {
            directions[i] = true;
        }

        this.age = 0;
        this.state = State.ALIVE;
        this.energy = INITIAL_ENERGY;
        this.orientation = new boolean[4];  
        int index = (int)(Math.random()*4); //random orientation by default
        orientation[index] = true;

        this.headColor = headColor;
	}

	public Agent ( int __x , int __y, World __world, boolean orientation[], float[] headColor )
	{
		super(__x,__y,__world);
        directions = new boolean[4];    // above, right, below, left; in that order
        for (int i=0; i<directions.length; i++) {
            directions[i] = true;
        }
        this.orientation = orientation;
        this.headColor = headColor;
	}

	
	public void step() {

        if ( world.getIteration() % 20 == 0 )   {

            this.updateAge();
            if (this.age >= Agent.MAX_LIFESPAN)  {
                this.state = State.DEAD;
            }

            /* Indices of squares in four directions relative to the agent's orientation */
            int right = (this.x + 1 + this.world.getWidth()) % this.world.getWidth();
            int left = (this.x - 1 + this.world.getWidth()) % this.world.getWidth();
            int above = (this.y + 1 + this.world.getHeight()) % this.world.getHeight();
            int below = (this.y - 1 + this.world.getHeight()) % this.world.getHeight();

            this.accessible = 4;
            
            /* Block off directions that are impassable or dangerous */
            /* And determine the number of remaining accessible directions */
            double hAbove = this.world.getCellHeight(this.x, above), 
                hRight = this.world.getCellHeight(right, this.y),
                hBelow = this.world.getCellHeight(this.x, below), 
                hLeft  = this.world.getCellHeight(left, this.y),
                hThis  = this.world.getCellHeight(this.x,this.y);

            /* Block off water and cliffs */       
            if ( (hAbove < 0) || (Math.abs(hAbove - hThis)) > 0.5)    {
                directions[0] = false;
                accessible--;
            }
            if ( (hRight < 0) || (Math.abs(hRight - hThis) > 0.5) )   {
                directions[1] = false;
                accessible--;
            }
            if ( (hBelow < 0) || (Math.abs(hBelow - hThis) > 0.5) )    {
                directions[2] = false;
                accessible--;
            }
            if ( (hLeft < 0) || (Math.abs(hLeft - hThis) > 0.5) )      {
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

        gl.glColor3f(1.f,1.f,1.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight);

        gl.glColor3f(1.f,1.f,1.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight);
        
        gl.glColor3f(1.f,1.f,1.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight);

        gl.glColor3f(1.f,1.f,1.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight);

        gl.glColor3f(headColor[0],headColor[1],headColor[2]);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 5.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 5.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 5.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 5.f);
    }




    /* GETTERS AND SETTERS */

    public int getAge()    {
        return this.age;
    }
    
    public float getEnergy()    {
        return this.energy;
    }

    public void updateAge()   {
        this.age++;
    }

    public void updateEnergy()  {
        this.energy -= 1.;
    }
 
}
