// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package objects;

import javax.swing.DefaultRowSorter;

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
                //gl.glColor3f(0,0.5f,0);                                                       //no shimmer
        		gl.glColor3f(0.f,0.5f-(float)(0.1*Math.random()),0.f);                          //shimmer  
                
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





//BEGIN NEW TREE CODE
/*

            //Draw the leaves
            for (int i=0; i<2; i++) {
                DisplayToolbox.drawLeaves2(3, 2.f - 0.4f*i, 1.f + 0.6f*i,  2.5f + 0.4f*i, gl, x, y, offset, stepX, stepY, lenX, lenY, altitude);
            }

            //Draw the trunk
            if ( cellState == 3)    {
                gl.glColor3f(0.2f,0.2f,0.2f);
            }
            else   {
                gl.glColor3f(0.514f,0.263f,0.2f);
            }
            DisplayToolbox.drawOctagonalPrism(0.5f, 0.5f, 0, 1.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
*/

 //END NEW TREE CODE








 //BEGIN COMPROMISE - OPTIMIZED NEW TREE CODE
/*
            //Draw the leaves
            DisplayToolbox.drawLeaves2(2, 2.f, 1.f,  5.f, gl, x, y, offset, stepX, stepY, lenX, lenY, altitude);
            
            //Draw the trunk
            if ( cellState == 3)
                gl.glColor3f(0.2f,0.2f,0.2f);
            else 
                gl.glColor3f(0.514f,0.263f,0.2f);
<<<<<<< HEAD
           // DisplayToolbox.drawSquarePrism(0.5f, 0.5f, 0, 1.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);

=======
            DisplayToolbox.drawSquarePrism(0.5f, 0.5f, 0, 1.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
*/

 //END COMPROMISE - OPTIMIZED NEW TREE CODE



 //BEGIN FOURTH OPTION - trees are prisms
 
            
            //Draw the leaves
            DisplayToolbox.drawSquarePrism(1.f, 0, 1.f, 5.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
            
            //Draw the trunk
            if ( cellState == 3)
                gl.glColor3f(0.2f,0.2f,0.2f);
            else 
                gl.glColor3f(0.514f,0.263f,0.2f);
            DisplayToolbox.drawSquarePrism(0.5f, 0.5f, 0, 1.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);


 //END FOURTH OPTION



















 
    		//float heightFactor, double heightBooster, float smoothFactor[]
    		
    		// maj: 2020-02-13
    		
 //BEGIN OLD TREE CODE
/*
    		gl.glVertex3f( offset+x*stepX, offset+y*stepY, altitude + 4.f);                 //Old tree display (inverted)
            gl.glVertex3f( offset+x*stepX-lenY/16.f, offset+y*stepY+lenY/2.f, altitude  );
            gl.glVertex3f( offset+x*stepX, offset+y*stepY, altitude + 4.f );
            gl.glVertex3f( offset+x*stepX+lenY/16.f, offset+y*stepY-lenY/2.f, altitude );

    		gl.glVertex3f( offset+x*stepX, offset+y*stepY, altitude + 4.f );
            gl.glVertex3f( offset+x*stepX-lenY/2.f, offset+y*stepY+lenY/16.f, altitude );
            gl.glVertex3f( offset+x*stepX, offset+y*stepY, altitude + 4.f );
            gl.glVertex3f( offset+x*stepX+lenY/2.f, offset+y*stepY-lenY/16.f, altitude );
*/
//END OLD TREE CODE
            
        }
    }

}
