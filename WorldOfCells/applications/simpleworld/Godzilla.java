package applications.simpleworld;
import utils.PredatorVision;
import worlds.World;
import com.jogamp.opengl.GL2;

public class Godzilla extends Agent {
    private static Godzilla instance = null;
    private float scalingFactor;
    private PredatorVision breath;

    private Godzilla(int __x , int __y, WorldOfTrees __world, float[] headColor, float[] bodyColor) {
        super(__x,__y,__world,headColor,bodyColor);
        this.breath = new PredatorVision(x, y, 20, orientation, world);
        this.scalingFactor = 2.f;
        this.bodyColor = new float[]{0.133f,0.545f,0.133f};
    }

    public static Godzilla getInstance(int x, int y, WorldOfTrees world)    {
        if (instance == null)   {
            instance = new Godzilla(x,y,world,new float[]{0.133f,0.545f,0.133f}, new float[]{0.133f,0.545f,0.133f});
        }
        return instance;
    }

    public void atomicBreath()  {
        
    }

    public void crush() {

    }

    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)
    {
      
    	int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

    	float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

    	float zoff = myWorld.getLandscape().getZOffset();

        gl.glColor3f( bodyColor[0],bodyColor[1],bodyColor[2]);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff + 4.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight  + zoff+ 4.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff);

        gl.glColor3f( bodyColor[0],bodyColor[1],bodyColor[2]);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff + 4.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff + 4.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff);

        gl.glColor3f( bodyColor[0],bodyColor[1],bodyColor[2]);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff + 4.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff + 4.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff);

        gl.glColor3f( bodyColor[0],bodyColor[1],bodyColor[2]);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff + 4.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff + 4.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff);

        gl.glColor3f(headColor[0],headColor[1],headColor[2]);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff + 5.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX-lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff + 5.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY+lenY*scalingFactor, height*normalizeHeight + zoff + 5.f*scalingFactor);
        gl.glVertex3f( offset+x2*stepX+lenX*scalingFactor, offset+y2*stepY-lenY*scalingFactor, height*normalizeHeight + zoff + 5.f*scalingFactor);
    }



    public float calculateSlopeRadius(float h, float scale, float minRadius, boolean increasing)    {
        // Calculates the radii needed to draw a gentle slope, whose values we will consider on the interval 0 <= h <= 1.
        // Lower scale makes the slope less steep but also lowers the values overall; minRadius is a constant added to the output to prevent the slope from decreasing below a certain value (hence it should NOT be negative).
        // If increasing is true, the slope is increasing; otherwise it is decreasing.
        if (increasing)
            return scale * 1.f / (h + 1.f);
        else
            return scale * 1.f / -(h - 2.f);
    }

    public float calculateSphereRadius(float h) {
        // takes in a float from the interval [0,1] and returns the radius of a slice of a sphere of radius 1/2
        return (float)(Math.sqrt(0.25 - (h-0.5)*(h-0.5)));
    }
    
}

    