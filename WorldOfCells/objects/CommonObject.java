// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package objects;

import com.jogamp.opengl.GL2;

import worlds.World;

abstract public class CommonObject  // CommonObject are standard object with no particular properties (ie. no internal state)
{ 


    public static void displayObjectAt(World myWorld, GL2 gl, int cellState, float x, float y, double height, double heightBooster, float offset, float stepX, float stepY, float lenX, float lenY, float heightFactor, float smoothFactor[])
    {
    	System.out.println("CommonObject.displayObjectAt(...,x,y,...) called, but not implemented.");
    }

    public static void displayObjectAt(World myWorld, GL2 gl, float offset, float stepX, float stepY, float lenX, float lenY, float heightFactor )
    {
    	System.out.println("CommonObject.displayObjectAt(...) called, but not implemented.");
    }

}
