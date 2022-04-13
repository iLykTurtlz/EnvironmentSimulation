// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject.State;
import objects.*;
import worlds.World;
import graphics.*;
import applications.simpleworld.Weather.*;

public class WorldOfTrees extends World {

    protected ForestCA forest;
    protected Landscape landscape;
    private static final int NB_CLOUDS = 2;
    private static final int NB_RAIN = 100;
    private static final int NB_SNOW = 100;
    public static final float WATER_LEVEL = -0.05f;
	public static final float TREE_LINE = 0.2f;
	public static final float SNOW_LINE = 0.3f;


    public void init ( int __dxCA, int __dyCA, double[][] landscape )
    {
    	super.init(__dxCA, __dyCA, landscape);
    	
    	// add colors

    	for ( int x = 0 ; x < __dxCA ; x++ )
    		for ( int y = 0 ; y < __dyCA ; y++ )
    		{
	        	float color[] = new float[3];

	        	float height = (float) this.getCellHeight(x, y);
		
				
				if (height > SNOW_LINE)	
				{
                    // snowy mountains
                    float c = height / (float)this.getMaxEverHeight();
		        	color[0] = c + (1-c)/2f;
					color[1] = c + (1-c)/2f;
					color[2] = c + (1-c)/2f;
				}

		        else if ( height >= 0 )
		        {

					// green mountains
		        	/**/
		        	color[0] = height / ( (float)this.getMaxEverHeight() );
					color[1] = 0.4f + 0.2f * height / ( (float)this.getMaxEverHeight() );
					color[2] = height / ( (float)this.getMaxEverHeight() );
					/**/
		        }
		        else if (height >= WATER_LEVEL) {
                    // sand
                    color[0] = 0.9f + (float)Math.random()%0.05f;
                    color[1] = 0.8f + (float)Math.random()%0.05f;
                    color[2] = 0.6f + (float)Math.random()%0.05f;
                }
		        else
		        {
		        	// water
					color[0] = -height;
					color[1] = -height;
					color[2] = 0.8f;
		        }
		        this.cellsColorValues.setCellState(x, y, color);
    		}
    	// add some objects
    	/*for ( int i = 0 ; i < 11 ; i++ )
    	{
    		if ( i%10 == 0 )
    			uniqueObjects.add(new Monolith(110,110+i,this));
    		else
    			uniqueObjects.add(new BridgeBlock(110,110+i,this));
    	}*/

        //add clouds
        for (int i = 0; i < NB_CLOUDS; i++)
            uniqueObjects.add(new Cloud((int) (Math.random() * landscape.length), (int) (Math.random() * landscape[0].length), this));

        //add rain
        for (int i = 0; i < NB_RAIN; i++)
            raindrops.add(new Rain((int) (Math.random() * landscape.length), (int) (Math.random() * landscape[0].length), this));

        //add snow
        for (int i = 0; i < NB_SNOW; i++)
            snow.add(new Snow((int) (Math.random() * landscape.length), (int) (Math.random() * landscape[0].length), this));
		/*
		for (int i=0; i<40; i++)	{
			int posx = (int)(Math.random()*__dxCA);
			int posy = (int)(Math.random()*__dyCA);

			while (this.getCellHeight(posx,posy) < 0)	{	//si l'emplacement est sur l'eau il faudra en trouver un autre.  Est-ce qu'il y a un moyen moins couteux?  Avec des ArrayList?
				posx = (int)(Math.random()*__dxCA);
				posy = (int)(Math.random()*__dyCA);
			}

			uniqueDynamicObjects.add(new Prey(posx,posy,this));
		}
		for (int i=0; i<40; i++)	{
			int posx = (int)(Math.random()*__dxCA);
			int posy = (int)(Math.random()*__dyCA);

			while (this.getCellHeight(posx,posy) < 0)	{	//si l'emplacement est sur l'eau il faudra en trouver un autre.  Est-ce qu'il y a un moyen moins couteux?  Avec des ArrayList?
				posx = (int)(Math.random()*__dxCA);
				posy = (int)(Math.random()*__dyCA);
			}

			uniqueDynamicObjects.add(new Predator(posx,posy,this));
		}

		*/
    	//uniqueDynamicObjects.add(new Agent(64,64,this));
    	
    }

    
    protected void initCellularAutomata(int __dxCA, int __dyCA, double[][] landscape)
    {
    	forest = new ForestCA(this,__dxCA,__dyCA,cellsHeightValuesCA);
    	forest.init();

    }
    
    protected void stepCellularAutomata()
    {
    	if ( iteration%10 == 0 ) {
    		forest.step();
        }
    }
    
    protected void stepAgents()
    {
		/*
    	for ( int i = 0 ; i < this.uniqueDynamicObjects.size() ; i++ )
    	{ 
			Agent a = this.uniqueDynamicObjects.get(i);
    		a.step();
			if (a.getState() == State.DEAD)	{
				this.removeAgent(a);
			}

    	}
		*/

		for ( int i = 0 ; i < this.predators.getSizeUsed() ; i++ )
		{
			Predator p = this.predators.get(i);
			p.step();
			if (p.getState() == State.DEAD)	{
				this.predators.remove(p);
			}
		}

		for ( int i = 0 ; i < this.prey.getSizeUsed() ; i++ )
		{
			Prey p = this.prey.get(i);
			p.step();
			if (p.getState() == State.DEAD)	{
				this.prey.remove(p);
			}
		}

		for ( int i = 0 ; i < this.plants.size() ; i++ )
		{
			Plant p = this.plants.get(i);
			p.step();
		}

		if (godzilla != null)
			godzilla.step();
    }

    public int getCellValue(int x, int y) // used by the visualization code to call specific object display.
    {
    	return forest.getCellState(x%dxCA,y%dyCA);
    }

    public Landscape getLandscape() {
        return landscape;
    }

    public ForestCA getForest() {
        return forest;
    }

    public void setCellValue(int x, int y, int state)
    {
    	forest.setCellState( x%dxCA, y%dyCA, state);
    }
    
	public void displayObjectAt(World _myWorld, GL2 gl, int cellState, int x,
			int y, double height, float offset,
			float stepX, float stepY, float lenX, float lenY,
			float normalizeHeight) 
	{
		switch ( cellState )
		{
		case 1: // trees: green, fire, burnt
		case 2:
		case 3:
            if (getLandscape().getWeather().getCondition() == Condition.SNOWY)
                gl.glColor3f(1f,1f,1f);
            else
                gl.glColor3f(
            Tree.displayObjectAt(_myWorld,gl,cellState, x, y, height, offset, stepX, stepY, lenX, lenY, normalizeHeight);
		default:
			// nothing to display at this location.
		}
	}

	public void displayUniqueObjects(World _myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset,
			float stepX, float stepY, float lenX, float lenY, float normalizeHeight)
	{
		super.displayUniqueObjects(_myWorld, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight);
		for ( int i = 0 ; i < plants.size(); i++ )
			plants.get(i).displayUniqueObject(_myWorld,gl,offsetCA_x,offsetCA_y,offset,stepX,stepY,lenX,lenY,normalizeHeight);
		for ( int i = 0 ; i < predators.getSizeUsed(); i++ )
			predators.get(i).displayUniqueObject(_myWorld,gl,offsetCA_x,offsetCA_y,offset,stepX,stepY,lenX,lenY,normalizeHeight);	
		for ( int i = 0 ; i < prey.getSizeUsed(); i++ )
			prey.get(i).displayUniqueObject(_myWorld,gl,offsetCA_x,offsetCA_y,offset,stepX,stepY,lenX,lenY,normalizeHeight);
			
		if (godzilla != null)	{
			godzilla.displayUniqueObject(_myWorld, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight);
		}
	}

		//change
	public void setLandscape(Landscape l) {
        this.landscape = l;
    }

	public void addPredator(int x, int y)	{
		predators.add(x,y,this);
	}

	public void addPredator(int x, int y, int[] offspringCharacters)	{
		predators.add(x,y,this,offspringCharacters);
	}



	public void addPrey(int x, int y)	{
		prey.add(x,y,this);
	}

	public void addPrey(int posx, int posy, Prey prey)	{
		prey.setPosition(posx, posy);
		uniqueDynamicObjects.add(prey);
	}

	public void removePredator(int index) {
		predators.remove(index);
	}

	public void removePredator(Predator p) {
		predators.remove(p);
	}

	public void removePrey(int index) {
		prey.remove(index);
	}
	
	public void removePrey(Prey p) {
		prey.remove(p);
	}

	public void removeAgent(Agent a)	{
		uniqueDynamicObjects.remove(a);
	}

	public void removePlant(Plant p)	{
		plants.remove(p);
	}

	public void spawnGodzilla()	{
		//Spawns Godzilla on land
		int[] coord = getRandomLandCoordinate();
		godzilla = Godzilla.getInstance(coord[0],coord[1],this);
	}

	//public void displayObject(World _myWorld, GL2 gl, float offset,float stepX, float stepY, float lenX, float lenY, float heightFactor, double heightBooster) { ... } 
    

	public int[] getRandomLandCoordinate()	{
		// Returns a coordinate on land.
		int[] coord = new int[2];
		coord[0] = (int)(Math.random()*dxCA);
		coord[1] = (int)(Math.random()*dyCA);
		double height = cellsHeightValuesCA.getCellState(coord[0]%dxCA,coord[1]%dyCA);
		while (height < WATER_LEVEL || height > SNOW_LINE)	{
			coord[0] = (int)(Math.random()*dxCA);
			coord[1] = (int)(Math.random()*dyCA);
			height = cellsHeightValuesCA.getCellState(coord[0]%dxCA,coord[1]%dyCA);
		}
		return coord;
	}

	public int[] getRandomSeaCoordinate()	{
		// Returns a coordinate at sea
		int[] coord = new int[2];
		coord[0] = (int)(Math.random()*dxCA);
		coord[1] = (int)(Math.random()*dyCA);
		double height = cellsHeightValuesCA.getCellState(coord[0]%dxCA,coord[1]%dyCA);
		while (height > WATER_LEVEL)	{
			coord[0] = (int)(Math.random()*dxCA);
			coord[1] = (int)(Math.random()*dyCA);
			height = cellsHeightValuesCA.getCellState(coord[0]%dxCA,coord[1]%dyCA);
		}
		return coord;

	}
   
}
