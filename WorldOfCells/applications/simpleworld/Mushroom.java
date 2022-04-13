package applications.simpleworld;
import worlds.World;

import com.jogamp.opengl.GL2;

import utils.DisplayToolbox;

public class Mushroom extends Plant {
    private float[] stemColor;
    private float[] bandColor;                                      // The color of the next band to be drawn
    private float[] startingBandColor;                              // When the mushroom is NOT fully grown, the color of the innermost band (really a disc) is set at red.  The colors cycle only when the mushroom is fully grown.
    private float centerHeight;
    private float scalingFactor;
    private static final int MAX_SIZE = 11;                         // In order for the fully grown mushrooms' color changes to display properly, this value must be exactly 11 (hence final).  Otherwise we would need to create a new incrementColor function which cycles through (MAX_SIZE + 1) colors.

    public Mushroom(int __x , int __y, WorldOfTrees __world)   {
        super(__x,__y,__world);
        stemColor = new float[] {1.f,0.f,1.f};
        bandColor = new float[] {1.f,0.f,0.f};
        startingBandColor = new float[] {1.f,0.f,0.f};
        centerHeight = 3.0f;
        growth_rate = 900;
        scalingFactor = 1.f;
    }

    public void step()  {
        if ( world.getIteration() % (1000 - growth_rate) == 0) {
            //if the Mushroom is touched by fire or lava (and not already on fire) it catches fire.
            if ( state != State.ON_FIRE && (world.getLandscape().getVolcano().isLava(x,y) || world.getForest().getCellState(x,y) == 2) ) { 
                stemColor[0] = bandColor[0] = 1.f;
                stemColor[1] = bandColor[1] = 0.5f;
                stemColor[2] = bandColor[2] = 0;
                state = State.ON_FIRE;
            } 

            //if the mushroom is on fire, trees with same same position as well as adjacent UniqueDynamicObjects will catch fire too.
            if ( state == State.ON_FIRE )   {
                spreadFire(); 
                burnDown();
            }

            if (size < MAX_SIZE && state == State.ALIVE) {
                incrementSize();
            }
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

        if (state == State.ALIVE)  {

            //Draw the stems
            gl.glColor3f(stemColor[0],stemColor[1],stemColor[2]);
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
            gl.glVertex3f( offset+x2*stepX-lenY/16.f, offset+y2*stepY+lenY/2.f, altitude );
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
            gl.glVertex3f( offset+x2*stepX+lenY/16.f, offset+y2*stepY-lenY/2.f, altitude );
            
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
            gl.glVertex3f( offset+x2*stepX-lenY/2.f, offset+y2*stepY+lenY/16.f, altitude );
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
            gl.glVertex3f( offset+x2*stepX+lenY/2.f, offset+y2*stepY-lenY/16.f, altitude );

            //Draw the mushroom cap.  Colors cycle through the rainbow iff size = MAX_SIZE.
            float hNorm = 0.f;
            float bandThicknessNorm = 1.f/MAX_SIZE;
            float r1,r2;
            if (size < MAX_SIZE)    {
                bandColor[0] = startingBandColor[0];
                bandColor[1] = startingBandColor[1];
                bandColor[2] = startingBandColor[2];
            }
            for (int i=0; i<size; i++)  {
                DisplayToolbox.incrementRainbow(bandColor);
                gl.glColor3f(bandColor[0],bandColor[1],bandColor[2]);
                r1 = calculateRadius( hNorm + bandThicknessNorm * (i+1) );
                r2 = calculateRadius( hNorm + bandThicknessNorm * i );
                DisplayToolbox.drawOctagonalPrism(r1, r2, centerHeight - scalingFactor*bandThicknessNorm * (i+1), centerHeight - scalingFactor * bandThicknessNorm * i, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
            }
        }


        else if (state == State.ON_FIRE || state == State.DEAD) {

            //Set the color
            if (state == State.ON_FIRE) {
                gl.glColor3f(1.f,0.5f + (float)(Math.random()*0.2),0);
            } else {
                gl.glColor3f(0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()));
            }

            //Stems
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
            gl.glVertex3f( offset+x2*stepX-lenY/16.f, offset+y2*stepY+lenY/2.f, altitude );
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
            gl.glVertex3f( offset+x2*stepX+lenY/16.f, offset+y2*stepY-lenY/2.f, altitude );
            
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
            gl.glVertex3f( offset+x2*stepX-lenY/2.f, offset+y2*stepY+lenY/16.f, altitude );
            gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude + centerHeight );
            gl.glVertex3f( offset+x2*stepX+lenY/2.f, offset+y2*stepY-lenY/16.f, altitude );

            //Mushroom cap
            float hNorm = 0.f;
            float bandThicknessNorm = 1.f/MAX_SIZE;
            float r1,r2;
            for (int i=0; i<size; i++)  {
                r1 = calculateRadius( hNorm + bandThicknessNorm * (i+1) );
                r2 = calculateRadius( hNorm + bandThicknessNorm * i );
                DisplayToolbox.drawOctagonalPrism(r1, r2, centerHeight - scalingFactor*bandThicknessNorm * (i+1), centerHeight - scalingFactor * bandThicknessNorm * i, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
            }
        }
    }

    public void reduceSize()    {
        // This method is called when a prey eats the plant.
        decrementSize();
    }

    public void incrementSize() {
        if (size < MAX_SIZE)
            size++;
    }


    
    public float calculateRadius(float h)  {
        //We will use the values on the interval [0,1] for a certain ellipse
        return (float)(scalingFactor*Math.sqrt(4-4*(h-1)*(h-1)));
    }


}
