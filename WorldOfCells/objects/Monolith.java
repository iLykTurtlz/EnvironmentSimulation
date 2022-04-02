// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package objects;

import com.jogamp.opengl.GL2;

import worlds.World;

public class Monolith extends UniqueObject{
	
	public Monolith ( float __x , float __y , World __world )
	{
		super(__x,__y,__world);
	}
	
    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight )
    {

        // display a monolith
        
        //gl.glColor3f(0.f+(float)(0.5*Math.random()),0.f+(float)(0.5*Math.random()),0.f+(float)(0.5*Math.random()));
        
    	float x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	float y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

    	float zoffset = myWorld.getLandscape().getZOffset();

        gl.glColor3f(0.5f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, 0.f + zoffset);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.f + zoffset);

        gl.glColor3f(0.6f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, 0.f + zoffset);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, 0.f + zoffset);
        
        gl.glColor3f(0.55f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.f + zoffset);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, 0.f + zoffset);

        gl.glColor3f(0.45f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, 0.f + zoffset);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, 0.f + zoffset);

        gl.glColor3f(0.5f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, 0.1f*normalizeHeight + zoffset);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoffset);
    }
}
