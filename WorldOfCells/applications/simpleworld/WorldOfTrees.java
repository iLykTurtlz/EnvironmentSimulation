// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import com.jogamp.opengl.GL2;

import applications.simpleworld.Agent.State;
import objects.*;
import worlds.World;

public class WorldOfTrees extends World {

    protected ForestCA cellularAutomata;
    protected Landscape landscape;

    public void init ( int __dxCA, int __dyCA, double[][] landscape )
    {
    	super.init(__dxCA, __dyCA, landscape);
    	
    	// add colors
    	
    	for ( int x = 0 ; x < __dxCA ; x++ )
    		for ( int y = 0 ; y < __dyCA ; y++ )
    		{
	        	float color[] = new float[3];

	        	float height = (float) this.getCellHeight(x, y);
		    	
		        if ( height >= 0 )
		        {
		        	// snowy mountains
		        	/*
		        	color[0] = height / (float)this.getMaxEverHeight();
					color[1] = height / (float)this.getMaxEverHeight();
					color[2] = height / (float)this.getMaxEverHeight();
					/**/
		        	
					// green mountains
		        	/**/
		        	color[0] = height / ( (float)this.getMaxEverHeight() );
					color[1] = 0.9f + 0.1f * height / ( (float)this.getMaxEverHeight() );
					color[2] = height / ( (float)this.getMaxEverHeight() );
					/**/
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
    	for ( int i = 0 ; i < 11 ; i++ )
    	{
    		if ( i%10 == 0 )
    			uniqueObjects.add(new Monolith(110,110+i,this));
    		else
    			uniqueObjects.add(new BridgeBlock(110,110+i,this));
    	}
    	

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
    	cellularAutomata = new ForestCA(this,__dxCA,__dyCA,cellsHeightValuesCA);
    	cellularAutomata.init();
    }
    
    protected void stepCellularAutomata()
    {
    	if ( iteration%10 == 0 )
    		cellularAutomata.step();
    }
    
    protected void stepAgents()
    {
    	// nothing to do.
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

		for ( int i = 0 ; i < this.plants.getSizeUsed() ; i++ )
		{
			Plant p = this.plants.get(i);
			p.step();
		}
    }

    public int getCellValue(int x, int y) // used by the visualization code to call specific object display.
    {
    	return cellularAutomata.getCellState(x%dxCA,y%dyCA);
    }

    public void setCellValue(int x, int y, int state)
    {
    	cellularAutomata.setCellState( x%dxCA, y%dyCA, state);
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
			Tree.displayObjectAt(_myWorld,gl,cellState, x, y, height, offset, stepX, stepY, lenX, lenY, normalizeHeight);
		default:
			// nothing to display at this location.
		}
	}

	public void displayUniqueObjects(World _myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset,
			float stepX, float stepY, float lenX, float lenY, float normalizeHeight)
	{
		super.displayUniqueObjects(_myWorld, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight);
		for ( int i = 0 ; i < plants.getSizeUsed(); i++ )
			plants.get(i).displayUniqueObject(_myWorld,gl,offsetCA_x,offsetCA_y,offset,stepX,stepY,lenX,lenY,normalizeHeight);
		for ( int i = 0 ; i < predators.getSizeUsed(); i++ )
			predators.get(i).displayUniqueObject(_myWorld,gl,offsetCA_x,offsetCA_y,offset,stepX,stepY,lenX,lenY,normalizeHeight);	
		for ( int i = 0 ; i < prey.getSizeUsed(); i++ )
			prey.get(i).displayUniqueObject(_myWorld,gl,offsetCA_x,offsetCA_y,offset,stepX,stepY,lenX,lenY,normalizeHeight);
		
	}

		//change
	public void setLandscape(Landscape l) {
        this.landscape = l;
    }

    public Landscape getLandscape() { return landscape; }

	public void addPredator(int x, int y)	{
		//uniqueDynamicObjects.add(new Predator(x,y,this));
		predators.add(x,y,this);
	}

	public void addPredator(int posx, int posy, Predator pred)	{
		pred.setPosition(posx, posy);
		uniqueDynamicObjects.add(pred);
	}

	public void addPrey(int x, int y)	{
		prey.add(x,y,this);
	}

	public void addPrey(int posx, int posy, Prey prey)	{
		prey.setPosition(posx, posy);
		uniqueDynamicObjects.add(prey);
	}

	public void addPlant(int x, int y)	{
		plants.add(x,y,this);
	}

	public void removeAgent(Agent a)	{
		uniqueDynamicObjects.remove(a);
	}

	public void removePlant(Plant p)	{
		plants.remove(p);
	}

	//public void displayObject(World _myWorld, GL2 gl, float offset,float stepX, float stepY, float lenX, float lenY, float heightFactor, double heightBooster) { ... } 
    
   
}
