package objects;

import com.jogamp.opengl.GL2;

import utils.DisplayToolbox;
import worlds.World;

public class Grass extends CommonObject {

    public static void displayObjectAt(World myWorld, GL2 gl, int cellState, float x, float y, double height, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight )
    {
        //float smoothFactorAvg = ( smoothFactor[0] + smoothFactor[1] + smoothFactor[2] + smoothFactor[3] ) / 4.f;
        
        switch ( cellState )
        {
        	case 1:
                //gl.glColor3f(0,0.5f,0);                                                       //no shimmer
        		gl.glColor3f(0.f,0.5f-(float)(0.1*Math.random()),0.f);                          //shimmer  
                
        		break;
        	case 2:
        		gl.glColor3f(1.f-(float)(0.2*Math.random()),(float)(0.2*Math.random()),0.f);       //flaming grass can still shimmer, but we adjusted it for artistic reasons
        		break;
        	case 3:
        		//gl.glColor3f(0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()));  //dead trees don't shimmer
                gl.glColor3f(0.1f,0.1f,0.1f);
        		break;
        }
        
        if ( cellState > 0 )
        {
    		float altitude = (float)height * normalizeHeight + myWorld.getLandscape().getZOffset();

            DisplayToolbox.drawLeaves(3,1.5f,0,0.5f,gl,(int)x,(int)y,offset,stepX,stepY,lenX,lenY,altitude);

        }
    }

}
