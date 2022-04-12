package applications.simpleworld;
import utils.DisplayToolbox;
import utils.PredatorVision;
import worlds.World;
import com.jogamp.opengl.GL2;

public class Godzilla extends Agent {
    private static Godzilla instance = null;
    private PredatorVision breath;

    private Godzilla(int __x , int __y, WorldOfTrees __world, float[] headColor, float[] bodyColor) {
        super(__x,__y,__world,headColor,bodyColor);
        this.breath = new PredatorVision(x, y, 20, orientation, world);
        this.scalingFactor = 2.f;
        this.bodyColor = new float[]{0.133f,0.545f,0.133f};
        probablityChangeDirection = 0.2;
    }

    public static Godzilla getInstance(int x, int y, WorldOfTrees world)    {
        if (instance == null)   {
            instance = new Godzilla(x,y,world,new float[]{0.133f,0.545f,0.133f}, new float[]{0.133f,0.545f,0.133f});
        }
        return instance;
    }

    public void step()  {
        if ( world.getIteration() % 800 == 0 )   {
            atomicBreath();
        }

        if ( world.getIteration() % 20 == 0 )   {
            this.accessible = 4;

            // Indices of squares in four directions relative to the agent's orientation 
            int right = (this.x + 1 + this.world.getWidth()) % this.world.getWidth();
            int left = (this.x - 1 + this.world.getWidth()) % this.world.getWidth();
            int above = (this.y + 1 + this.world.getHeight()) % this.world.getHeight();
            int below = (this.y - 1 + this.world.getHeight()) % this.world.getHeight();

            
            // Block off directions that are impassable or dangerous 
            // And determine the number of remaining accessible directions 
            double hAbove = this.world.getCellHeight(this.x, above), 
                   hRight = this.world.getCellHeight(right, this.y),
                   hBelow = this.world.getCellHeight(this.x, below), 
                   hLeft  = this.world.getCellHeight(left, this.y);
                   //hThis  = this.world.getCellHeight(this.x,this.y);

            // Block off water and cliffs      
            if (hAbove < WorldOfTrees.WATER_LEVEL)  {
                directions[0] = false;
                accessible--;
            }
            if (hRight < WorldOfTrees.WATER_LEVEL)  {
                directions[1] = false;
                accessible--;
            }
            if (hBelow < WorldOfTrees.WATER_LEVEL)  {
                directions[2] = false;
                accessible--;
            }
            if (hLeft < WorldOfTrees.WATER_LEVEL)   {
                directions[3] = false;
                accessible--;
            }


             //Update position based on the value of move.  That means explore if move is still -1.
             int move = updatePosition(-1);
             setOrientation(move);   //the Predator's orientation matches its direction of movement.
             
             reinitializeDirections();

        }


    }

    public void atomicBreath()  {
        //reset atomic breath based on Godzilla's position and orientation
        breath.setOrientation(orientation);
        breath.setPosition(x, y);
        breath.updateField();

        breath.irradiate(world);

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

        gl.glColor3f(0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()),0.f+(float)(0.2*Math.random()));

        //We draw a Queen chesspiece from bottom to top using a few mathematical functions
        //lastR and lastH are used to pass the most recent radius and height values from one part of the drawing to the next.
   
        
        //THE BASE
        
        //Rounded base
        int i;
        float h1Norm ,h2Norm ,r1 ,r2;
        for (i=0; i<10; i++)    {
            h1Norm = 1.f/10 * i;
            h2Norm = 1.f/10 * (i+1);
            r1 = 4.f * calculateRadiusEllipse(h1Norm);
            r2 = 4.f * calculateRadiusEllipse(h2Norm);
            DisplayToolbox.drawOctagonalPrism(r1 + 5.f, r2 + 5.f, 0.5f*i, 0.5f*(i+1) , altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        float lastR = 6.f * calculateRadiusEllipse(1.f/10 * i) + 5.f;
        float lastH = 0.5f*i;


        //Round band
        float scale = 1.f;
        for (i=0; i<6; i++) {
            h1Norm = 1.f/6 * i;
            h2Norm = 1.f/6 * (i+1);
            r1 = scale * calculateSphereRadius( h1Norm );
            r2 = scale * calculateSphereRadius( h2Norm );
            DisplayToolbox.drawOctagonalPrism(r1 + lastR, r2 + lastR, scale*h1Norm + lastH, scale*h2Norm + lastH, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //THE SHAFT
        lastR = scale * calculateSphereRadius( 1.f/6 * i ) + lastR;
        lastH = scale*( 1.f/6 * i ) + lastH;
        scale = 5.f;
        float additionalHeight = 10.f;
        float verticalScalingFactor = additionalHeight/10;
        int j;
        for (j=0; j<10; j++)    {
            h1Norm = 1.f/10 * j;
            h2Norm = 1.f/10 * (j+1);
            r1 = scale * calculateSlopeRadius(h1Norm, 0.5f, false);
            r2 = scale * calculateSlopeRadius(h2Norm, 0.5f, false);
            DisplayToolbox.drawOctagonalPrism(r1, r2, scale*h1Norm + lastH + verticalScalingFactor*j, scale*h2Norm + lastH + verticalScalingFactor*(j+1), altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //THE TOP
        //Round band in the middle
        lastR = scale * calculateSlopeRadius( 1.f/10 * j, 0.5f, false );
        lastH = scale*( 1.f/10 * j ) + lastH + verticalScalingFactor*j;
        
        scale = 1.f;
        for (i=0; i<6; i++) {
            h1Norm = 1.f/6 * i;
            h2Norm = 1.f/6 * (i+1);
            r1 = scale * calculateSphereRadius( h1Norm );
            r2 = scale * calculateSphereRadius( h2Norm );
            DisplayToolbox.drawOctagonalPrism(r1 + lastR, r2 + lastR, scale*h1Norm + lastH, scale*h2Norm + lastH, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //The long, widening portion
        lastR = scale * calculateSphereRadius( 1.f/6 * i ) + lastR;
        lastH = scale*( 1.f/6 * i ) + lastH;
        scale = 5.f;
        additionalHeight = 2.f;
        verticalScalingFactor = additionalHeight/10;
        
        for (j=0; j<10; j++)    {
            h1Norm = 1.f/10 * j;
            h2Norm = 1.f/10 * (j+1);
            r1 = scale * calculateSlopeRadius(h1Norm, 0.5f, true);
            r2 = scale * calculateSlopeRadius(h2Norm, 0.5f, true);
            DisplayToolbox.drawOctagonalPrism(r1, r2, scale*h1Norm + lastH + verticalScalingFactor*j, scale*h2Norm + lastH + verticalScalingFactor*(j+1), altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //The round band at the edge of the head
        lastR = scale * calculateSlopeRadius(1.f/10 * j, 0.5f, true);
        lastH = scale*(1.f/10 * j) + lastH + verticalScalingFactor*j;
        scale = 1.f;
        for (i=0; i<6; i++) {
            h1Norm = 1.f/6 * i;
            h2Norm = 1.f/6 * (i+1);
            r1 = scale * calculateSphereRadius( h1Norm );
            r2 = scale * calculateSphereRadius( h2Norm );
            DisplayToolbox.drawOctagonalPrism(r1 + lastR, r2 + lastR, scale*h1Norm + lastH, scale*h2Norm + lastH, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //The rounded top of the head
        lastR = scale * calculateSphereRadius( 1.f/6 * i ) + lastR;
        lastH = scale*(1.f/6 * i) + lastH;
        scale = 1.f;
        additionalHeight = 1.f;
        for (j=0; j<5; j++) {
            h1Norm = 1.f/5 * j;
            h2Norm = 1.f/5 * (j+1);
            r1 = scale * lastR * calculateHumpRadius( h1Norm );
            r2 = scale * lastR * calculateHumpRadius( h2Norm );
            DisplayToolbox.drawOctagonalPrism(r1, r2, scale*h1Norm + lastH, scale*h2Norm + lastH, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }

        //The sphere on top
        //lastR is about 0
        lastH = scale*(1.f/5 * j) + lastH;
        lastH -= 1.f; //subtract a little bit so that the sphere will intersect the top of the dome
        scale = 2.f;
        for (i=0; i<6; i++) {
            h1Norm = 1.f/6 * i;
            h2Norm = 1.f/6 * (i+1);
            r1 = scale * calculateSphereRadius( h1Norm );
            r2 = scale * calculateSphereRadius( h2Norm );
            DisplayToolbox.drawOctagonalPrism(r1, r2, scale*h1Norm + lastH, scale*h2Norm + lastH, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        }
    }


    public float calculateSlopeRadius(float h, float minRadius, boolean increasing)    {
        // Calculates the radii needed to draw a gentle slope, whose values we will consider on the interval 0 <= h <= 1.
        // Lower scale makes the slope less but also lowers the values overall; minRadius is a constant added to the output to prevent the slope from decreasing below a certain value (hence it should NOT be negative).
        // If increasing is true, the slope is increasing and at h=0 its value is 0 and at h=1 its value is 0.5; otherwise it is decreasing and its value at h=1 is 0 and its value at h=0 = 0.5.
        if (increasing)
            return ( 1.f / -(h - 2.f) - 0.5f ) + minRadius;
        else
            return ( 1.f / (h + 1.f) - 0.5f ) + minRadius;
    }

    public float calculateSphereRadius(float h) {
        // takes in a float from the interval [0,1] and returns the radius of a slice of a sphere of radius 1/2
        return (float)(Math.sqrt(0.25 - (h-0.5)*(h-0.5)));
    }

    public float calculateHumpRadius(float h)  {
        // We will use 0 <= h <= 1 to calculate the hump on top of the Queen's head.
        return (1.f-h)*(1.f+h);
    }

    public float calculateRadiusEllipse(float hNorm)    {
        //returns the value of a horizontal semi-ellipse on the interval [0,1]
        return (float)(Math.sqrt(0.125 - ((hNorm-0.5)*(hNorm-0.5))/2));
    }
    
}

    