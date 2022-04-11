package applications.simpleworld;
import utils.DisplayToolbox;
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

        float altitude = height*normalizeHeight + zoff;

        gl.glColor3f( bodyColor[0],bodyColor[1],bodyColor[2]);

        //We draw a Queen chesspiece from bottom to top using just two mathematical functions
        //lastR and lastH are used to pass the most recent radius and height values from one part of the drawing to the next.
   
        
        /*
        //THE BASE
        float lastR = 2.f;
        float lastH = 1.f;
        DisplayToolbox.drawOctagonalPrism(lastR, lastR, 0, lastH , altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);

        int i;
        float h1Norm ,h2Norm ,r1 ,r2, scale = 0.5f;
        for (i=0; i<6; i++) {
            h1Norm = 1.f/6 * i;
            h2Norm = 1.f/6 * (i+1);
            r1 = scale * calculateSphereRadius( h1Norm );
            r2 = scale * calculateSphereRadius( h2Norm );
            DisplayToolbox.drawOctagonalPrism(r1 + lastR, r2 + lastR, scale*h1Norm + lastH, scale*h2Norm + lastH, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //THE SHAFT
        int j;
        lastR = r2 + lastR;
        lastH = scale*h2Norm + lastH;
        scale = 1.f;
        float additionalHeight = 5.f;
        float verticalScalingFactor = additionalHeight/10;

        for (j=0; j<10; j++)    {
            h1Norm = 1.f/10 * j;
            h2Norm = 1.f/10 * (j+1);
            r1 = scale * calculateSlopeRadius(h1Norm, scale, lastR - 0.5f, false);
            r2 = scale * calculateSlopeRadius(h2Norm, scale, lastR - 0.5f, false);
            DisplayToolbox.drawOctagonalPrism(r1, r2, scale*h1Norm + lastH + verticalScalingFactor*j, scale*h2Norm + lastH + verticalScalingFactor*(j+1), altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //THE TOP
        lastR = r2;
        lastH = scale*h2Norm + lastH + verticalScalingFactor*j;
        
        scale = 0.5f;
        for (i=0; i<6; i++) {
            h1Norm = 1.f/6 * i;
            h2Norm = 1.f/6 * (i+1);
            r1 = scale * calculateSphereRadius( h1Norm );
            r2 = scale * calculateSphereRadius( h2Norm );
            DisplayToolbox.drawOctagonalPrism(r1 + lastR, r2 + lastR, scale*h1Norm + lastH, scale*h2Norm + lastH, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        lastR = 
        lastH = 

*/




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
        // Lower scale makes the slope less but also lowers the values overall; minRadius is a constant added to the output to prevent the slope from decreasing below a certain value (hence it should NOT be negative).
        // If increasing is true, the slope is increasing and at h=0 its value is 0 and at h=1 its value is 0.5; otherwise it is decreasing and its value at h=1 is 0 and its value at h=0 = 0.5.
        if (increasing)
            return scale * ( 1.f / -(h - 2.f) - 0.5f ) + minRadius;
        else
            return scale * ( 1.f / (h + 1.f) - 0.5f ) + minRadius;
    }

    public float calculateSphereRadius(float h) {
        // takes in a float from the interval [0,1] and returns the radius of a slice of a sphere of radius 1/2
        return (float)(Math.sqrt(0.25 - (h-0.5)*(h-0.5)));
    }
    
}

    