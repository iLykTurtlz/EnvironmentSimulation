package utils;

import com.jogamp.opengl.GL2;

import worlds.World;

/*  This class contains methods useful for at least TWO classes in our program.
 *
 * 
 * 
 * 
 * 
 */

public final class DisplayToolbox {

    //this class is not to be instantiated
    private DisplayToolbox()    {}

    public static void incrementRainbow(float color[])    {
        // Increments the color, a float array of size 3 for rgb, according to the spectrum
        // The three float values passed to the function should be permutations of the following multisets : {1.f,0,0}, {1.f,1.5f,0}, {1.f,1.f,0}
        for (int i=0; i<color.length; i++)  {
            if (color[i] == 0.f)  {
                if (color[(i-1+color.length)%color.length] == 1.f && color[(i+1+color.length)%color.length] == 0.f) {
                    color[i] = 0.5f;
                }
            }
            else if (color[i] == 1.f)    {
                if (color[(i-1+color.length)%color.length] == 0.f && color[(i+1+color.length)%color.length] == 1.f) {
                    color[i] = 0.5f;
                }
            }
            else {   
                if (color[(i-1+color.length)%color.length] == 0.f && color[(i+1+color.length)%color.length] == 1.f) {
                    color[i] = 0.f; 
                }
                else if (color[(i-1+color.length)%color.length] == 1.f && color[(i+1+color.length)%color.length] == 0.f)   {
                    color[i] = 1.f;
                } 
                return;     //this return is NOT optional : it affects the result.                
            }
        }
    }
    
    public static void drawLeaves(int nbLeaves, float width, float h1, float h2, GL2 gl, int x2, int y2, float offset, float stepX, float stepY, float lenX, float lenY, float altitude) {
        // This is a similar design to the trees, except their height (h2 - h1), width and placement above the ground (h1) are specified by parameters, and 
        // instead of 4 leaves 90 degrees apart, this function will display nbLeaves*2 leaves, evenly spaced around a central axis.
        // N.B. gl.glColor3f() should be called before this function to set the color.
        for (int i=0; i<nbLeaves; i++)    {
            float x3 = (float)Math.cos(Math.PI/nbLeaves*i);
            float y3 = (float)Math.sin(Math.PI/nbLeaves*i);
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + h1);
            gl.glVertex3f( offset+x2*stepX-lenY*width*x3, offset+y2*stepY+lenY*width*y3, altitude + h2);
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + h1);
            gl.glVertex3f( offset+x2*stepX+lenY*width*x3, offset+y2*stepY-lenY*width*y3, altitude + h2);
        }
    }

    public static void drawOctagon(GL2 gl, int x2, int y2, float offset, float stepX, float stepY, float lenX, float lenY, float radius, float altitude, float h)  {
        // Draws a horizontal octagon centered  at (x2,y2,altitude + h)  
        // Since we're using opengl : GL_QUADS, a polygon can't have more than four sides.
        // For this reason, the function draws two trapezoids with a rectangle in the middle.
        // N.B. gl.glColor3f() should be called before this function to set the color.
        gl.glVertex3f( offset+x2*stepX-lenX*radius / 3, offset+y2*stepY-lenY*radius, altitude + h );    //left trapezoid
        gl.glVertex3f( offset+x2*stepX-lenX*radius, offset+y2*stepY-lenY*radius / 3, altitude + h );
        gl.glVertex3f( offset+x2*stepX-lenX*radius, offset+y2*stepY+lenY*radius / 3, altitude + h );
        gl.glVertex3f( offset+x2*stepX-lenX*radius / 3, offset+y2*stepY+lenY*radius, altitude + h );

        gl.glVertex3f( offset+x2*stepX-lenX*radius / 3, offset+y2*stepY-lenY*radius, altitude + h );    //center rectangle
        gl.glVertex3f( offset+x2*stepX-lenX*radius / 3, offset+y2*stepY+lenY*radius, altitude + h );
        gl.glVertex3f( offset+x2*stepX+lenX*radius / 3, offset+y2*stepY+lenY*radius, altitude + h );
        gl.glVertex3f( offset+x2*stepX+lenX*radius / 3, offset+y2*stepY-lenY*radius, altitude + h );

        
        gl.glVertex3f( offset+x2*stepX+lenX*radius / 3, offset+y2*stepY+lenY*radius, altitude + h );    //right trapezoid
        gl.glVertex3f( offset+x2*stepX+lenX*radius, offset+y2*stepY+lenY*radius / 3, altitude + h );
        gl.glVertex3f( offset+x2*stepX+lenX*radius, offset+y2*stepY-lenY*radius / 3, altitude + h );
        gl.glVertex3f( offset+x2*stepX+lenX*radius / 3, offset+y2*stepY-lenY*radius, altitude + h );
    }

    public static void drawOctagonalPrism(float radius1, float radius2, float h1, float h2, float altitude, int x2, int y2, World myWorld, GL2 gl, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)  {
        // Draws an octagonal prism of height h2-h1 with top and bottom open.  The bottom octagon's in circle radius (apothem) is radius1 whereas the top otagon's in circle radius is radius2.
        // The 16 vertices of this shape are selected by a sequence of 8 quadruplets defined as a function of i, as shown.
        // N.B. gl.glColor3f() should be called before this function to set the color.
        int[] sequence = new int[4];

        for (int i=0; i<8; i++) {
            sequence[0] = i;
            sequence[1] = i+8;
            sequence[2] = (i+7)%8 + 8;
            sequence[3] = (i+7)%8;

            for (int j=0; j<sequence.length; j++)   {
                switch(sequence[j]) {       // 0-7: vertices of the bottom octagon, 8-15: vertices of the top octagon
                    case 0:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1 / 3, offset+y2*stepY-lenY*radius1, altitude + h1);
                        break;
                    case 1:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1, offset+y2*stepY-lenY*radius1 / 3, altitude + h1);
                        break;
                    case 2:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1, offset+y2*stepY+lenY*radius1 / 3, altitude + h1);
                        break;
                    case 3:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1 / 3, offset+y2*stepY+lenY*radius1, altitude + h1);
                        break;
                    case 4:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1 / 3, offset+y2*stepY+lenY*radius1, altitude + h1);
                        break;
                    case 5:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1, offset+y2*stepY+lenY*radius1 / 3, altitude + h1);
                        break;
                    case 6:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1, offset+y2*stepY-lenY*radius1 / 3, altitude + h1);
                        break;
                    case 7:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1 / 3, offset+y2*stepY-lenY*radius1, altitude + h1);
                        break;
                    case 8:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2 / 3, offset+y2*stepY-lenY*radius2, altitude + h2);
                        break;
                    case 9:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2, offset+y2*stepY-lenY*radius2 / 3, altitude + h2);
                        break;
                    case 10:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2, offset+y2*stepY+lenY*radius2 / 3, altitude + h2);
                        break;
                    case 11:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2 / 3, offset+y2*stepY+lenY*radius2, altitude + h2);
                        break;
                    case 12:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2 / 3, offset+y2*stepY+lenY*radius2, altitude + h2);
                        break;
                    case 13:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2, offset+y2*stepY+lenY*radius2 / 3, altitude + h2);
                        break;
                    case 14:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2, offset+y2*stepY-lenY*radius2 / 3, altitude + h2);
                        break;
                    case 15:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2 / 3, offset+y2*stepY-lenY*radius2, altitude + h2);
                        break;
                    default:
                        System.out.println("Erreur : DisplayToolbox.drawOctagonalPrism");
                }
            }
        }
    }



}
