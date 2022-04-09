package applications.simpleworld;
import worlds.World;

import javax.lang.model.util.ElementScanner14;

import com.jogamp.opengl.GL2;

import utils.DisplayToolbox;

public class Mushroom extends Plant {
    private float[] stemColor;
    private float[] bandColor;
    private float centerHeight;
    private float scalingFactor;

    public Mushroom(int __x , int __y, WorldOfTrees __world)   {
        super(__x,__y,__world);
        stemColor = new float[] {1.f,0.f,1.f};
        bandColor = new float[] {1.f,0.f,0.f};
        centerHeight = 3.0f;

        max_size = 11;
        growth_rate = 300;
        scalingFactor = 1.f;
    }

    public void step()  {
        if ( world.getIteration() % (1000 - growth_rate) == 0 && size < max_size) {
            incrementSize();
        }
        
    }


   



    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ) {

        float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

    	float zoff = myWorld.getLandscape().getZOffset();
        float altitude = (float)height * normalizeHeight + zoff;

        int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();
        
        //stems
        gl.glColor3f(stemColor[0],stemColor[1],stemColor[2]);
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
        gl.glVertex3f( offset+x2*stepX-lenY/16.f, offset+y2*stepY+lenY/2.f, altitude );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
        gl.glVertex3f( offset+x2*stepX+lenY/16.f, offset+y2*stepY-lenY/2.f, altitude );
        
    	gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
        gl.glVertex3f( offset+x2*stepX-lenY/2.f, offset+y2*stepY+lenY/16.f, altitude );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
        gl.glVertex3f( offset+x2*stepX+lenY/2.f, offset+y2*stepY-lenY/16.f, altitude );
        
        float hNorm = 0.f;
        float bandThicknessNorm = 1.f/max_size;
        float r1,r2;
        for (int i=0; i<size; i++)  {
            DisplayToolbox.incrementRainbow(bandColor);
            gl.glColor3f(bandColor[0],bandColor[1],bandColor[2]);
            r1 = calculateRadius( hNorm + bandThicknessNorm * (i+1) );
            r2 = calculateRadius( hNorm + bandThicknessNorm * i );
            DisplayToolbox.drawOctagonalPrism(r1, r2, centerHeight - scalingFactor*bandThicknessNorm * (i+1), centerHeight - scalingFactor * bandThicknessNorm * i, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);

        }
    }

    public void reduceSize()    {
        // This method is called when a prey eats the plant.
        decrementSize();
    }

    
    public float calculateRadius(float h)  {
        //We will use the values on the interval [0,1] for a certain ellipse
        return (float)(scalingFactor*Math.sqrt(4-4*(h-1)*(h-1)));
    }
    


}
