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
}