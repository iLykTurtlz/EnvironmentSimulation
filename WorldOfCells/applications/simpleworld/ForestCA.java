// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;
import applications.simpleworld.WorldOfTrees;

public class ForestCA extends CellularAutomataInteger {

    private static final float SPONTANEOUS_FIRE = 0.000001f;
    private static final float SPAWN_TREE = 0.4f;
    private static final float EXTENDED_RANGE = 0.4f;

	CellularAutomataDouble _cellsHeightValuesCA;
	
	WorldOfTrees world;
	
	public ForestCA ( WorldOfTrees __world, int __dx , int __dy, CellularAutomataDouble cellsHeightValuesCA )
	{
		super(__dx,__dy,true ); // buffering must be true.
		
		_cellsHeightValuesCA = cellsHeightValuesCA;
		
		this.world = __world;
	}
	
	public void init()
	{
		for ( int x = 0 ; x != _dx ; x++ )
    		for ( int y = 0 ; y != _dy ; y++ )
    		{
				if ( _cellsHeightValuesCA.getCellState(x,y) >= WorldOfTrees.TREE_LINE )	{
					this.setCellState(x, y, 0); // empty
				}
    			else if ( _cellsHeightValuesCA.getCellState(x,y) >= 0 )
    			{
    				if ( Math.random() < SPAWN_TREE ) // was: 0.71
    					this.setCellState(x, y, 1); // tree
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
	    			if ( this.getCellState(i,j) == 1 ) // tree?
	    			{
	    				// check if neighbors are burning
	    				if ( 
	    						this.getCellState( (i+_dx-1)%(_dx) , j ) == 2 ||
	    						this.getCellState( (i+_dx+1)%(_dx) , j ) == 2 ||
	    						this.getCellState( i , (j+_dy+1)%(_dy) ) == 2 ||
	    						this.getCellState( i , (j+_dy-1)%(_dy) ) == 2
	    					)
	    				{
	    					this.setCellState(i,j,2);
	    				}
	    				else
	    					if ( Math.random() < SPONTANEOUS_FIRE ) // spontaneously take fire ?
	    					{
	    						this.setCellState(i,j,2);
	    					}
	    					else if (Math.random() < EXTENDED_RANGE) {
	    						if (this.getCellState( (i+_dx-2)%(_dx) , j ) == 2 ||
	    						this.getCellState( (i+_dx+2)%(_dx) , j ) == 2 ||
	    						this.getCellState( i , (j+_dy+2)%(_dy) ) == 2 ||
	    						this.getCellState( i , (j+_dy-2)%(_dy) ) == 2) {
                            System.out.print("burning");
                                    this.setCellState(i, j, 2);
                                }
                            }
	    					else
	    					{
	    						this.setCellState(i,j,1); // copied unchanged
	    					}
	    			}
	    			else
	    			{
	        				if ( this.getCellState( i , j ) == 2 ) // burning?
	        				{
	        					this.setCellState(i,j,3); // burnt
	        				}
	        				else
	        				{
	        					this.setCellState(i,j, this.getCellState(i,j) ); // copied unchanged
	        				}
	    			}
	    			
	    			float color[] = new float[3];
	    			if (this.getCellState(i, j) == 1)
	    			{
                        color[0] = 0.f;
                        color[1] = 0.3f;
                        color[2] = 0.f;
                        this.world.cellsColorValues.setCellState(i, j, color);
                    }
                    else if (this.getCellState(i, j) == 2) { // burning tree
                        color[0] = 1.f;
                        color[1] = 0.f;
                        color[2] = 0.f;
                        this.world.cellsColorValues.setCellState(i, j, color);
                    }
                    else if (this.getCellState(i, j) == 3) { // burnt tree
                        if (!world.getLandscape().getVolcano().isStone(i, j)) {
                            color[0] = 0.f;
                            color[1] = 0.f;
                            color[2] = 0.f;
                            this.world.cellsColorValues.setCellState(i, j, color);
                        }
                    } else {
                        color[0] = 0.5f;
                        color[1] = 0.5f;
                        color[2] = 0.5f;
                        System.out.print("cannot interpret CA state: " + this.getCellState(i, j));
                        System.out.println(" (at: " + i + "," + j + " -- height: " + this.world.getCellHeight(i,j) + " )");
                        this.world.cellsColorValues.setCellState(i, j, color);
	    			}
    			}
    		}
    	this.swapBuffer();
	}
}
