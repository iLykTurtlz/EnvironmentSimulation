package applications.simpleworld;
import worlds.World;

import com.jogamp.opengl.GL2;

import utils.DisplayToolbox;

public class Pineapple extends Plant {

    private int nbBands;
    private float baseHeight;
    private float[] fruitColor;
    private float scalingFactor;
    private int max_size;

    public Pineapple(int __x , int __y, WorldOfTrees __world)  {
        super(__x,__y,__world);
        growth_rate = 900;
        nbBands = 10;
        baseHeight = 4.0f;
        fruitColor = new float[]{0.f,1.f,0.f};
        scalingFactor = 0.2f;
        max_size = 10;
    }

    public void step()  {
        super.step();

        if ( world.getIteration() % (1000 - growth_rate) == 0) {


            if (size < max_size) {
                incrementFruitColor();
                incrementSize();
            }

            //if the Pineapple is touched by fire or lava (and not already on fire) it catches fire.
            if ( state != State.ON_FIRE && (world.getLandscape().getVolcano().isLava(x,y) || world.getForest().getCellState(x,y) == 2) ) { 
                fruitColor[0] = 1.f;
                fruitColor[1] = 0.5f;
                fruitColor[2] = 0;
                state = State.ON_FIRE;
            } 

            //if the Pineapple is on fire, trees with same same position as well as adjacent UniqueDynamicObjects will catch fire too.
            if ( state == State.ON_FIRE )   {
                spreadFire(); 
            }

            if (size < max_size) {
                incrementSize();
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


        if (state == State.ALIVE)   {
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
        else if (state == State.ON_FIRE || state == State.DEAD)  {

            //Set the color
            if (state == State.ON_FIRE) {
                gl.glColor3f(1.f,0.5f + (float)(Math.random()*0.2),0);
            } else {
                gl.glColor3f(0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()));
            }

            //Leaves
            for (int i=0; i<8; i++) {   
                DisplayToolbox.drawLeaves(5, 4.f - 0.4f*i, baseHeight/9*i, baseHeight/9*(i+3), gl, x2, y2, offset, stepX, stepY, lenX, lenY, altitude);
            }


            //Fruit
            DisplayToolbox.drawOctagon(gl, x2, y2, offset, stepX, stepY, lenX, lenY, calculateRadius(bandThicknessNorm), altitude, baseHeight);
            int i;
            float r1, r2;
            for (i=0; i<nbBands; i++)   {
                
                r1 = calculateRadius(  bandThicknessNorm * (i+1)  );
                r2 = calculateRadius(  bandThicknessNorm * (i+2)  );

                DisplayToolbox.drawOctagonalPrism(r1,r2, baseHeight + bandThickness * i, baseHeight + bandThickness * (i+1), altitude,x2,y2,myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
            }
            DisplayToolbox.drawOctagon(gl, x2, y2, offset, stepX, stepY, lenX, lenY, calculateRadius( bandThicknessNorm * (i+1) ), altitude, baseHeight + bandThickness * i);

            //Top leaves
            float topHeight = baseHeight + bandThickness * (i -1);  // ... * i; is the height of the top of the fruit.  Using (i-1) instead of i here is an artistic choice, to make the top leaves resemble those of a real pineapple more closely
            float maxTopWidth = size/15.f;
            float widthDecrement = maxTopWidth/12.f;
            float heightIncrement = size/30.f;

            for (int j=0; j<size/2; j++)  {
                DisplayToolbox.drawLeaves(5, maxTopWidth - widthDecrement*j, topHeight, topHeight + heightIncrement*j, gl, x2, y2, offset, stepX, stepY, lenX, lenY, altitude);
            }

        }
        

    }

    public void reduceSize()    {
        // This method is called when a prey eats the plant.
        this.size = 0;
        this.fruitColor[0] = 0;
    }

    public void incrementSize() {
        if (size < max_size)
            size++;
    }
    
    public int getMaxSize() {
        return max_size;
    }


    public void incrementFruitColor()  {
        // This function increments the red in the fruit color, which starts at (r,g,b) = (0,1,0), so that the fruit becomes more yellow as it grows and ripens.
        fruitColor[0] += 1.f/(float)max_size;       //we want the red value to max out when size == max_size
        if (fruitColor[0] > 1.f)    {               //floating addition can be imprecise, so we really want to make it bounded above by 1.f exact!
            fruitColor[0] = 1.f;
        }
    }

    public float calculateRadius(float hNorm)    {
        //returns the value of a horizontal semi-ellipse scaled to the size of the fruit
        float r = (float)( (size*scalingFactor) * (Math.sqrt(0.125 - ((hNorm-0.5)*(hNorm-0.5))/2))  );
        return r;
    }

}
