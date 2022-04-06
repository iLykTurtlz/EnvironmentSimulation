package applications.simpleworld;

import javax.lang.model.util.ElementScanner14;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

public abstract class Plant extends UniqueDynamicObject {
    protected int size;
    protected int max_size;
    protected int growth_rate;      //0-999: 1000 will provoke a division by zero exception

    public Plant(int __x , int __y, WorldOfTrees __world)  {
        super(__x,__y,__world);
        this.size = 0;       
    }

    public void step()  {
        
        if ( world.getIteration() % (1000 - growth_rate) == 0 && size < max_size) {
            incrementSize();
        }
        
    }

   

    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ) {

        float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );
        float altitude = (float)height * normalizeHeight ;

        float x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	float y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();
        

        /*
        //stems
        gl.glColor3f(stemColor[0],stemColor[1],stemColor[2]);
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX-lenY/16.f, offset+y2*stepY+lenY/2.f, altitude + centerHeight );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX+lenY/16.f, offset+y2*stepY-lenY/2.f, altitude + centerHeight );
        
    	gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX-lenY/2.f, offset+y2*stepY+lenY/16.f, altitude + centerHeight );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX+lenY/2.f, offset+y2*stepY-lenY/16.f, altitude + centerHeight );
        

        //floral disc
        gl.glColor3f(1.f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX-lenX*centerRadius, offset+y2*stepY-lenY*centerRadius, altitude + centerHeight);
        gl.glVertex3f( offset+x2*stepX-lenX*centerRadius, offset+y2*stepY+lenY*centerRadius, altitude + centerHeight);
        gl.glVertex3f( offset+x2*stepX+lenX*centerRadius, offset+y2*stepY+lenY*centerRadius, altitude + centerHeight);
        gl.glVertex3f( offset+x2*stepX+lenX*centerRadius, offset+y2*stepY-lenY*centerRadius, altitude + centerHeight);

        grow(0, 0, centerHeight, 0.1f, new float[]{1.f,0.5f,0.f}, 0.1f, x2, y2, height, altitude, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight);

        */

        /*

        gl.glColor3f(1.f,0.f,0.f);

        
        //2,6,3,2
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        //7,3,6,7
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);

        //3,7,4,3
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        //8,4,7,8
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);

        //4,8,1,4
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);

        //5,1,8,5
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);

        //1,5,2,1
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);

        //6,2,5,6
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        */


        /*
        //1,2,3,4 = inner square
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);

        //5,6,7,8 = outer square
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        */






        //4 square petals

        /*
        gl.glColor3f(1.f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);

        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY*1.5f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY*1.5f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        */

        /*
        gl.glVertex3f( offset+x2*stepX-lenX*2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);

        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);
        */

        
    }

    public void reinitialize()  {
        return;
    }


    public int getSize()    {
        return size;
    }

    public void decrementSize() {
        if (size > 0)
            size--;
    }

    public void incrementSize() {
        if (size < max_size)
            size++;
    }
}
