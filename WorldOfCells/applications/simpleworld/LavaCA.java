package applications.simpleworld;

import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;
import worlds.World;

public class LavaCA extends CellularAutomataInteger {

	private World world;
	private static final int ROCK_TIME = 30;

	public LavaCA ( World __world, int __dx , int __dy )
	{
		super(__dx,__dy,true );
		this.world = __world;
	}

	public void init()
	{
		/*for ( int x = 0 ; x != _dx ; x++ )
    		for ( int y = 0 ; y != _dy ; y++ )
    		{
    			if ( _cellsHeightValuesCA.getCellState(x,y) >= WorldOfTrees.SNOW_LINE )
    			{
    				if ( Math.random() < 1 )
    					this.setCellState(x, y, 1); // lava
    				else
    					this.setCellState(x, y, 0); // empty
    			}
    			else
    			{
    				this.setCellState(x, y, -1); // water (ignore)
    			}
    		}*/
    	this.swapBuffer();

	}

	public void step()
	{
    	for ( int i = 0 ; i != _dx ; i++ )
    		for ( int j = 0 ; j != _dy ; j++ )
    		{
                float height = (float) world.getCellHeight(i, j);
    			if ( this.getCellState(i, j) >= 1 )
    			{
	    			if ( this.getCellState(i,j) == 1 ) // lava
	    			{
	    				// check if neighbors are at a lower height
	    				float left = (float) world.getCellHeight((i+_dx-1)%(_dx), j);
	    				float right = (float) world.getCellHeight((i+_dx+1)%(_dx), j);
	    				float up = (float) world.getCellHeight(i , (j+_dy+1)%(_dy));
	    				float down = (float) world.getCellHeight(i , (j+_dy-1)%(_dy));

	    						/*left < height ||
	    						right < height ||
	    						up < height ||
	    						down < height*/
                        // check if neighbors are lava and the lava is falling
                        int lstate = this.getCellState( (i+_dx-1)%(_dx) , j );
                        int rstate = this.getCellState( (i+_dx+1)%(_dx) , j );
                        int ustate = this.getCellState( i , (j+_dy+1)%(_dy) );
                        int dstate = this.getCellState( i , (j+_dy-1)%(_dy) );
                            //look for the minimum height
                            int x, y;
                            float min = left;
                            int state = lstate;
                            if (min > right) {
                                min = right;
                                state = rstate;
                            }
                            if (min > up) {
                                min = up;
                                state = ustate;
                            }
                            if (min > down) {
                                min = down;
                                state = dstate;
                            }

                            if (min == left) {
                                x = (i+_dx-1)%(_dx);
                                y = j;
                            } else if (min == right) {
                                x = (i+_dx+1)%(_dx);
                                y = j;
                            } else if (min == up) {
                                x = i;
                                y = (j+_dy+1)%(_dy);
                            } else {
                                x = i;
                                y = (j+_dy-1)%(_dy);
                            }

                            if ((state == 0 || state == -1 || state == 2) && min < height) {
                                if (min >= WorldOfTrees.WATER_LEVEL)
                                    this.setCellState(x, y, 1); //set lava
                                else
                                    this.setCellState(x, y, 2); //set rock
                            }
                            this.setCellState(i, j, 3); //set current cell to 2 nd lava state
                            /*if (this.getCellState( (i+_dx+1)%(_dx) , j ) == -1 && right < height)
                                this.setCellState( (i+_dx+1)%(_dx), j , 1);
                            if (this.getCellState( i , (j+_dy+1)%(_dy) ) == -1 && up < height)
                                this.setCellState( i , (j+_dy+1)%(_dy), 1);
                            if (this.getCellState( i , (j+_dy-1)%(_dy) ) == -1 && down < height)
                                this.setCellState( i , (j+_dy-1)%(_dy), 1);*/
                            //this.setCellState(i, j, this.getCellState(i, j)+1); // lava 2 nd state
	    			}
	    			else
	    			{
	        				if ( this.getCellState(i, j) >= 3 && this.getCellState(i, j) < ROCK_TIME) // if is lava at 2 nd state
	        				{
	        					this.setCellState(i,j, this.getCellState(i,j) + 1); // decrement lava life span
	        				}
	        				else
	        				{
	        					this.setCellState(i,j, 2 );
	        				}
	    			}

	    			float color[] = new float[3];
	    			if ( this.getCellState(i, j) == 1 || this.getCellState(i, j) >= 3 ) {
                        float r = (float) Math.random();
                        color[0] = (float) (0.2d * r) + 0.7f;
                        color[1] = (float) (0.3d * r) + 0.3f;
                        color[2] = 0.f;
                    } else if (this.getCellState(i, j) == 2) {
                        color[0] = 0.3f * height + 0.2f;
                        color[1] = 0.3f * height + 0.2f;
                        color[2] = 0.3f * height + 0.2f;
                    } else {
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
