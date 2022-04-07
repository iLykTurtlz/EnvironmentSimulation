package applications.simpleworld;

import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;
import worlds.World;

public class LavaCA extends CellularAutomataInteger {

	private CellularAutomataDouble _cellsHeightValuesCA;
	private World world;

	public LavaCA ( World __world, int __dx , int __dy, CellularAutomataDouble cellsHeightValuesCA )
	{
		super(__dx,__dy,true );
		_cellsHeightValuesCA = cellsHeightValuesCA;
		this.world = __world;
	}

	public void init()
	{
		for ( int x = 0 ; x != _dx ; x++ )
    		for ( int y = 0 ; y != _dy ; y++ )
    		{
    			if ( _cellsHeightValuesCA.getCellState(x,y) >= WorldOfTrees.SNOW_LINE )
    			{
    				if ( Math.random() < 0.5 )
    					this.setCellState(x, y, 1); // lava
    				else
    					this.setCellState(x, y, 0); // empty
    			}
    			else
    			{
    				this.setCellState(x, y, -1); // water (ignore)
    			}
    		}
    	this.swapBuffer();

	}

	public void step()
	{
    	for ( int i = 0 ; i != _dx ; i++ )
    		for ( int j = 0 ; j != _dy ; j++ )
    		{
    			if ( this.getCellState(i, j) == 1 || this.getCellState(i, j) == 2 || this.getCellState(i, j) == 3 )
    			{
                    float height = (float) world.getCellHeight(i, j);
	    			if ( this.getCellState(i,j) == 1 ) // lava
	    			{
	    				// check if neighbors are at a lower height
	    				float left = (float) world.getCellHeight(i+_dx-1%(_dx), j);
	    				float right = (float) world.getCellHeight(i+_dx+1%(_dx), j);
	    				float up = (float) world.getCellHeight(i , (j+_dy+1)%(_dy));
	    				float down = (float) world.getCellHeight(i , (j+_dy-1)%(_dy));

	    						/*left < height ||
	    						right < height ||
	    						up < height ||
	    						down < height*/
                        // check if neighbors are lava and the lava is falling
	    				if (
	    						(this.getCellState( (i+_dx-1)%(_dx) , j ) == 1 && left < height) ||
	    						(this.getCellState( (i+_dx+1)%(_dx) , j ) == 1 && right < height) ||
	    						(this.getCellState( i , (j+_dy+1)%(_dy) ) == 1 && up < height) ||
	    						(this.getCellState( i , (j+_dy-1)%(_dy) ) == 1 && down < height)
                            )
	    				{
                            this.setCellState(i, (j+_dy-1)%(_dy), 1); //set lava
	    				}
	    			}
	    			else
	    			{
	        				if ( this.getCellState( i , j ) == 1 ) // if is lava
	        				{
	        					this.setCellState(i,j,2); // rock
	        				}
	        				else
	        				{
	        					this.setCellState(i,j, this.getCellState(i,j) ); // copied unchanged
	        				}
	    			}

	    			float color[] = new float[3];
	    			switch ( this.getCellState(i, j) )
	    			{
	    				case 0:
	    					break;
	    				case 1: //lava
                            float r = (float) Math.random();
                            color[0] = (float) (0.8d * r);
                            color[1] = (float) (0.3d * r);
	    					color[2] = 0.f;
	    					break;
	    				case 2: // rock
	    					color[0] = 0.2f;
	    					color[1] = 0.2f;
	    					color[2] = 0.2f;
	    					break;
	    				default:
	    					color[0] = 0.5f;
	    					color[1] = 0.5f;
	    					color[2] = 0.5f;
	    					System.out.print("cannot interpret CA state: " + this.getCellState(i, j));
	    					System.out.println(" (at: " + i + "," + j + " -- height: " + this.world.getCellHeight(i,j) + " )");
	    			}
	    			this.world.cellsColorValues.setCellState(i, j, color);
    			}
    		}
    	this.swapBuffer();
	}


}
