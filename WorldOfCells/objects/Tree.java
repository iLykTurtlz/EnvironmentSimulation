// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package objects;

import com.jogamp.opengl.GL2;

import utils.DisplayToolbox;
import worlds.World;

public class Tree extends CommonObject {

    public static void displayObjectAt(World myWorld, GL2 gl, int cellState, float x, float y, double height, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight )
    {
        //float smoothFactorAvg = ( smoothFactor[0] + smoothFactor[1] + smoothFactor[2] + smoothFactor[3] ) / 4.f;
        
        switch ( cellState )
        {
        	case 1:
        		//gl.glColor3f(0.f,0.6f-(float)(0.2*Math.random()),0.f);    //we decided to get rid of the shimmering
                gl.glColor3f(0.f,0.4f,0.f);
        		break;
        	case 2:
        		gl.glColor3f(1.f-(float)(0.2*Math.random()),(float)(0.2*Math.random()),0.f);       //flaming trees can still shimmer, but we adjusted it for artistic reasons
        		break;
        	case 3:
        		//gl.glColor3f(0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()));  //dead trees don't shimmer
                gl.glColor3f(0.1f,0.1f,0.1f);
        		break;
        }
        
        if ( cellState > 0 )
        {
    		float altitude = (float)height * normalizeHeight + myWorld.getLandscape().getZOffset();
    		
    		//float heightFactor, double heightBooster, float smoothFactor[]
    		
    		// maj: 2020-02-13
    		/*
    		gl.glVertex3f( offset+x*stepX, offset+y*stepY, altitude + 4.f);                 //Old tree display
            gl.glVertex3f( offset+x*stepX-lenY/16.f, offset+y*stepY+lenY/2.f, altitude  );
            gl.glVertex3f( offset+x*stepX, offset+y*stepY, altitude + 4.f );
            gl.glVertex3f( offset+x*stepX+lenY/16.f, offset+y*stepY-lenY/2.f, altitude );

    		gl.glVertex3f( offset+x*stepX, offset+y*stepY, altitude + 4.f );
            gl.glVertex3f( offset+x*stepX-lenY/2.f, offset+y*stepY+lenY/16.f, altitude );
            gl.glVertex3f( offset+x*stepX, offset+y*stepY, altitude + 4.f );
            gl.glVertex3f( offset+x*stepX+lenY/2.f, offset+y*stepY-lenY/16.f, altitude );
            */
            
            //DisplayToolbox.drawLeaves2(4, 2.f, 0,  4.f, gl, x, y, offset, stepX, stepY, lenX, lenY, altitude);
            
        }
    }

}
