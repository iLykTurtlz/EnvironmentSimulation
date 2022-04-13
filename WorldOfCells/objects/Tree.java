// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package objects;

import javax.swing.DefaultRowSorter;

import com.jogamp.opengl.GL2;

import applications.simpleworld.Weather.*;
import applications.simpleworld.ForestCA;

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
        	case ForestCA.BURNT_TREE:
        		gl.glColor3f(0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()));
        		break;
            default: //because the burning states are between 2 and 99
        		gl.glColor3f(1.f-(float)(0.2*Math.random()),(float)(0.2*Math.random()),0.f);       //flaming trees can still shimmer, but we adjusted it for artistic reasons
        		break;
        }

        if (myWorld.getLandscape().getWeather().getCondition() == Condition.SNOWING && cellState == 1) //if it's snowing and the tree is not burning nor burnt
            gl.glColor3f(1f, 1f, 1f);

        if ( cellState > 0 )
        {
    		float altitude = (float)height * normalizeHeight + myWorld.getLandscape().getZOffset();





//BEGIN NEW TREE CODE


            //Draw the leaves
            DisplayToolbox.drawLeaves2(4, 1.5f, 0.4f,  3.5f, gl, x, y, offset, stepX, stepY, lenX, lenY, altitude);

            DisplayToolbox.drawLeaves2(3, 1.f, 2.f, 4.f, gl, x, y, offset, stepX, stepY, lenX, lenY, altitude);
            //DisplayToolbox.drawLeaves2(nbLeaves, width, h1, h2, gl, x2, y2, offset, stepX, stepY, lenX, lenY, altitude);
            

            //Draw the trunk
            if ( cellState == ForestCA.BURNT_TREE )    { //if the tree is burnt
                gl.glColor3f(0.2f,0.2f,0.2f);
            }
            else   {
                gl.glColor3f(0.514f,0.263f,0.2f);
            }
            DisplayToolbox.drawX(0.5f, 0.5f, -5.f, 0.5f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
  
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
            DisplayToolbox.drawX(0.5f, 0.5f, 0, 1.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
=======
<<<<<<< HEAD
           // DisplayToolbox.drawSquarePrism(0.5f, 0.5f, 0, 1.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);

=======
            DisplayToolbox.drawSquarePrism(0.5f, 0.5f, 0, 1.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
>>>>>>> 0192fbf91548459f358000e3906c4321eb84eefc
*/

 //END COMPROMISE - OPTIMIZED NEW TREE CODE



 //BEGIN FOURTH OPTION - trees are prisms
 /*
            
            //Draw the leaves
            DisplayToolbox.drawSquarePrism(1.f, 0, 1.f, 5.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
            
            //Draw the trunk
            if ( cellState == 3)
                gl.glColor3f(0.2f,0.2f,0.2f);
            else 
                gl.glColor3f(0.514f,0.263f,0.2f);
            DisplayToolbox.drawX(0.5f, 0.5f, 0, 1.f, altitude, (int)x, (int)y, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
*/

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
