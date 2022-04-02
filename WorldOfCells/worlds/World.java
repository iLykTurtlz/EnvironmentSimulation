// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package worlds;

import java.util.ArrayList;
import com.jogamp.opengl.GL2;

import applications.simpleworld.Agent;
import applications.simpleworld.Plant;
import utils.PoolPredator;
import utils.PoolPrey;
import applications.simpleworld.Predator;
import applications.simpleworld.Prey;
import cellularautomata.*;
import graphics.*;


import objects.*;

public abstract class World {
	
	protected int iteration = 0;

	protected ArrayList<UniqueObject> uniqueObjects = new ArrayList<UniqueObject>();
	protected ArrayList<Agent> uniqueDynamicObjects = new ArrayList<Agent>();
	protected PoolPrey prey = new PoolPrey();
	protected PoolPredator predators = new PoolPredator();
	protected ArrayList<Plant> plants = new ArrayList<Plant>();

    
	protected int dxCA;
	protected int dyCA;

	protected int indexCA;

	//protected CellularAutomataInteger cellularAutomata; // TO BE DEFINED IN CHILDREN CLASSES
    
	protected CellularAutomataDouble cellsHeightValuesCA;
	protected CellularAutomataDouble cellsHeightAmplitudeCA;
	
	public CellularAutomataColor cellsColorValues;

	private double maxEverHeightValue = Double.NEGATIVE_INFINITY;
	private double minEverHeightValue = Double.POSITIVE_INFINITY;

    public World( )
    {
    	// ... cf. init() for initialization
    }
    
    public void init( int __dxCA, int __dyCA, double[][] landscape )
    {
    	dxCA = __dxCA;
    	dyCA = __dyCA;
    	
    	iteration = 0;

    	this.cellsHeightValuesCA = new CellularAutomataDouble (__dxCA,__dyCA,false);
    	this.cellsHeightAmplitudeCA = new CellularAutomataDouble (__dxCA,__dyCA,false);
    	
    	this.cellsColorValues = new CellularAutomataColor(__dxCA,__dyCA,false);
    	
    	// init altitude and color related information
    	for ( int x = 0 ; x != dxCA ; x++ )
    		for ( int y = 0 ; y != dyCA ; y++ )
    		{
    			// compute height values (and amplitude) from the landscape for this CA cell 
    			double minHeightValue = Math.min(Math.min(landscape[x][y],landscape[x+1][y]),Math.min(landscape[x][y+1],landscape[x+1][y+1]));
    			double maxHeightValue = Math.max(Math.max(landscape[x][y],landscape[x+1][y]),Math.max(landscape[x][y+1],landscape[x+1][y+1])); 
    			
    			if ( this.maxEverHeightValue < maxHeightValue )
    				this.maxEverHeightValue = maxHeightValue;
    			if ( this.minEverHeightValue > minHeightValue )
    				this.minEverHeightValue = minHeightValue;
    			
    			cellsHeightAmplitudeCA.setCellState(x,y,maxHeightValue-minHeightValue);
    			cellsHeightValuesCA.setCellState(x,y,(minHeightValue+maxHeightValue)/2.0);

    			/* TODO! Default coloring
    	    	// init color information
    	        if ( this.cellsHeightAmplitudeCA.getCellState(x,y) >= 0.0 )
    	        {
    				float color[] = { (float)height*4.0f, 1.0f-(float)height*0.3f, (float)height*2.0f };
    				this.cellsColorValues.setCellState(x,y,color);
    	        }
    	        else
    	        {
    	        	// water
    				float color[] = { (float)(-height), 1.0f-(float)(-height)*0.3f, (float)1.0f };
    				this.cellsColorValues.setCellState(x,y,color);
    	        }
    	        */
    		}
    	
    	initCellularAutomata(__dxCA,__dyCA,landscape);

    }
    
    
    public void step()
    {
    	stepCellularAutomata();
    	stepAgents();
    	iteration++;
    }
    
    public int getIteration()
    {
    	return this.iteration;
    }
    
    abstract protected void stepAgents();
    
    // ----

    protected abstract void initCellularAutomata(int __dxCA, int __dyCA, double[][] landscape);
    
    protected abstract void stepCellularAutomata();
    
    // ---
    
    abstract public int getCellValue(int x, int y); // used by the visualization code to call specific object display.

    abstract public void setCellValue(int x, int y, int state);
    
    // ---- 
    
    public double getCellHeight(int x, int y) // used by the visualization code to set correct height values
    {
    	return cellsHeightValuesCA.getCellState(x%dxCA,y%dyCA);
    }
    
    // ---- 
    
    public float[] getCellColorValue(int x, int y) // used to display cell color
    {
    	float[] cellColor = this.cellsColorValues.getCellState( x%this.dxCA , y%this.dyCA );

    	float[] color  = {cellColor[0],cellColor[1],cellColor[2],1.0f};
        
        return color;
    }

	abstract public void displayObjectAt(World _myWorld, GL2 gl, int cellState, int x,
			int y, double height, float offset,
			float stepX, float stepY, float lenX, float lenY,
			float normalizeHeight); 

	public void displayUniqueObjects(World _myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset,
			float stepX, float stepY, float lenX, float lenY, float normalizeHeight) 
	{
		
    	for ( int i = 0 ; i < uniqueObjects.size(); i++ ) {
            UniqueObject object = uniqueObjects.get(i);
            //if (object instanceof Cloud && _myWorld.getLandscape().VIEW_FROM_ABOVE) continue;
    		uniqueObjects.get(i).displayUniqueObject(_myWorld,gl,offsetCA_x,offsetCA_y,offset,stepX,stepY,lenX,lenY,normalizeHeight);
        }
    	for ( int i = 0 ; i < uniqueDynamicObjects.size(); i++ )
    		uniqueDynamicObjects.get(i).displayUniqueObject(_myWorld,gl,offsetCA_x,offsetCA_y,offset,stepX,stepY,lenX,lenY,normalizeHeight);
		

	}
    
	public int getWidth() { return dxCA; }
	public int getHeight() { return dyCA; }

	public double getMaxEverHeight() { return this.maxEverHeightValue; }
	public double getMinEverHeight() { return this.minEverHeightValue; }

	abstract public void setLandscape(Landscape l);
	abstract public Landscape getLandscape();

	public abstract void addPredator(int posx, int posy);

	public abstract void addPrey(int posx, int posy);




	/* GETTERS AND SETTERS*/
	public PoolPredator getPredators()	{
		return predators;
	}

	public PoolPrey getPrey()	{
		return prey;
	}

	public ArrayList<Plant> getPlants()	{
		return plants;
	}
	public void test()	{
		System.out.println("Bonjour");
	}
	

	public void displayHeightValues()	{
		/* This method is not to be implemented as part of the program.  
		   It is just a tool to study the variation of height values generated by Perlin noise. */
		double min=0.0, max=0.0;
		double minDiff=10.0, maxDiff=0.0;	//min and max difference of adjacent squares (at least those encountered consecutively in this program)
		double height1 = 0.0, height2;
		double totDiff=0.0;

		for (int i=0; i<this.dxCA; i++)	{
			for (int j=0; j<this.dyCA; j++)	{
				if (i!=0)	{
					height2 = this.getCellHeight(i,j);
					double absDiff = Math.abs(height1-height2);
					totDiff += absDiff;
					if (absDiff > maxDiff)	{
						maxDiff = absDiff;
					}
					if (absDiff < minDiff)	{
						minDiff = absDiff;
					}
				}
				height1 = this.getCellHeight(i,j);
				//System.out.println(height1);
				if (height1 > max)	{
					max = height1;
				}
				if (height1 < min)	{
					min = height1;
				}
			}
		}
		System.out.println("min = "+min+", max = "+max);
		System.out.println("minDiff = "+minDiff+", maxDiff = "+maxDiff);
		System.out.println("avgDiff = "+totDiff/((dxCA-1)*dyCA));
	}

	public int[] getRandomLandCoordinate()	{
		/* Returns a coordinate on land.  May have high temporal cost.*/
		int[] coord = new int[2];
		coord[0] = (int)(Math.random()*dxCA);
		coord[1] = (int)(Math.random()*dyCA);
		double height = cellsHeightValuesCA.getCellState(coord[0]%dxCA,coord[1]%dyCA);
		while (height < 0)	{
			coord[0] = (int)(Math.random()*dxCA);
			coord[1] = (int)(Math.random()*dyCA);
			height = cellsHeightValuesCA.getCellState(coord[0]%dxCA,coord[1]%dyCA);
		}
		return coord;
	}

}
