package applications.simpleworld;
import worlds.World;

import com.jogamp.opengl.GL2;

import utils.DisplayToolbox;

public class Pineapple extends Plant {

    private int nbBands;
    private float baseHeight;
    private float[] fruitColor;
    private float scalingFactor;

    public Pineapple(int __x , int __y, WorldOfTrees __world)  {
        super(__x,__y,__world);
        this.growth_rate = 900;
        this.nbBands = 10;
        this.baseHeight = 4.0f;
        this.fruitColor = new float[]{0.f,1.f,0.f};
        this.scalingFactor = 0.2f;
        this.max_size = 10;
    }

    public void step()  {
        super.step();
        if ( world.getIteration() % (1000 - growth_rate) == 0 && size < max_size) {
            incrementFruitColor();
        }
        
    }

    public void incrementFruitColor()  {
        // This function increments the red in the fruit color, which starts at (r,g,b) = (0,1,0), so that the fruit becomes more yellow as it grows and ripens.
        fruitColor[0] += 1.f/(float)max_size;       //we want the red value to max out when size == max_size
        if (fruitColor[0] > 1.f)    {               //floating addition can be imprecise, so we really want to make it bounded above by 1.f exact!
            fruitColor[0] = 1.f;
        }
    }



    public void drawLeaves(float width, float h1, float h2, GL2 gl, int x2, int y2, float offset, float stepX, float stepY, float lenX, float lenY, float altitude) {
        //this is a similar design to the trees, except their height (h2 - h1), width and placement above the ground (h1) are specified by parameters, and going around the unit circle also makes it bushier.
        for (int i=0; i<10; i++)    {
            float x3 = (float)Math.cos(Math.PI/5*i);
            float y3 = (float)Math.sin(Math.PI/5*i);
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + h1);
            gl.glVertex3f( offset+x2*stepX-lenY*width*x3, offset+y2*stepY+lenY*width*y3, altitude + h2);
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + h1);
            gl.glVertex3f( offset+x2*stepX+lenY*width*x3, offset+y2*stepY-lenY*width*y3, altitude + h2);
        }
    }

   

    public void drawBottomOrTop(GL2 gl, int x2, int y2, float offset, float stepX, float stepY, float lenX, float lenY, float radius, float altitude, float h)  {
        // Draws an octagon for the bottom or top of the fruit.  Because we're using opengl : GL_QUADS, a polygon can't have more than four sides.
        // For this reason, the function draws two trapezoids with a rectangle in the middle.
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
    
        //gl.glVertex3f( offset+x2*stepX-lenX*radius / 3, offset+y2*stepY-lenY*radius, altitude + h );
    }


    public void drawFruitLayer(float radius1, float radius2, float h1, float h2, float altitude, int x2, int y2, World myWorld, GL2 gl, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)  {
        // Draws an octagonal prism of height h2-h1 with top and bottom open.  The bottom octagon's in circle radius (apothem) is radius1 whereas the top otagon's in circle radius is radius2.
        // The 16 vertices of this shape are selected by a sequence of 8 quadruplets defined as a function of i, as shown.
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
                }
            }
        }
    }
    

    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ) {

        //calculate altitude, the height of the ground on which we will draw the plant.
        float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );
        float zoff = myWorld.getLandscape().getZOffset();
        float altitude = (float)height * normalizeHeight +zoff;

        //bandThickness is the height delta of each fruit layer, bandThicknessNorm is used to get radius values from the ellipse function, calculateRadius()
        float bandThickness = size * scalingFactor / nbBands;
        float bandThicknessNorm = 1.f / (nbBands+2);              //we cut the top and bottom off the ellipse, so that it will look more like a pineapple, hence +2.


        //this code allows the fruit to stay in place while the user moves to explore the world.
        int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();


        
        //leaves under the fruit
        gl.glColor3f(0,0.6f,0);     //green
        for (int i=0; i<8; i++) {   //we deliberately let h2 pass the base height for artistic reasons
            DisplayToolbox.drawLeaves(5, 4.f - 0.4f*i, baseHeight/9*i, baseHeight/9*(i+3), gl, x2, y2, offset, stepX, stepY, lenX, lenY, altitude);
        }

        //here we draw the fruit
        gl.glColor3f(fruitColor[0],fruitColor[1],fruitColor[2]);
        DisplayToolbox.drawOctagon(gl, x2, y2, offset, stepX, stepY, lenX, lenY, calculateRadius(bandThicknessNorm), altitude, baseHeight);
        int i;
        float r1, r2;
        for (i=0; i<nbBands; i++)   {
            
            r1 = calculateRadius(  bandThicknessNorm * (i+1)  );
            r2 = calculateRadius(  bandThicknessNorm * (i+2)  );

            DisplayToolbox.drawOctagonalPrism(r1,r2, baseHeight + bandThickness * i, baseHeight + bandThickness * (i+1), altitude,x2,y2,myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }
        DisplayToolbox.drawOctagon(gl, x2, y2, offset, stepX, stepY, lenX, lenY, calculateRadius( bandThicknessNorm * (i+1) ), altitude, baseHeight + bandThickness * i);

        //finally the number and size of leaves on top of the fruit depend on its growth state (the superclass attribute, size)
        float topHeight = baseHeight + bandThickness * (i -1);  // ... * i; is the height of the top of the fruit.  Using (i-1) instead of i here is an artistic choice, to make the top leaves resemble those of a real pineapple more closely
        float maxTopWidth = size/15.f;
        float widthDecrement = maxTopWidth/12.f;
        float heightIncrement = size/30.f;

        gl.glColor3f(0,0.6f,0);         //green

        for (int j=0; j<size/2; j++)  {
            DisplayToolbox.drawLeaves(5, maxTopWidth - widthDecrement*j, topHeight, topHeight + heightIncrement*j, gl, x2, y2, offset, stepX, stepY, lenX, lenY, altitude);
        }
        
    }

    public float calculateRadius(float hNorm)    {
        //returns the value of a horizontal semi-ellipse scaled to the size of the fruit
        float r = (float)( (size*scalingFactor) * (Math.sqrt(0.125 - ((hNorm-0.5)*(hNorm-0.5))/2))  );
        return r;
    }
}
