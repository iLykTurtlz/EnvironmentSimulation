package applications.simpleworld;

import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;
import applications.simpleworld.WorldOfTrees;
import applications.simpleworld.Weather.*;

public class LavaCA extends CellularAutomataInteger {

	private WorldOfTrees world;
	private static final int ROCK_TIME = 100; //basically 100 world iterations
	private static final double SPREAD_CHANCE = 1d;

	public LavaCA ( WorldOfTrees __world, int __dx , int __dy )
	{
		super(__dx,__dy,true );
		this.world = __world;
	}

    //lava is set manually by the volcano
	public void init()
	{
    	this.swapBuffer();
	}

    /**
      * The Lava Cellular Automata is basically a cell that is affected by gravity and more precisely by height difference,
      * the lava will flow until reaching a stable height (neighborrs at same level). Up to a certain point the
      * lava itself will turn into stone (lava lifespan). The lava will directly turn into stone if it reaches water.
      * The lava lifespan is ROCK_TIME, it decreases with time and with world iterations % 10.
      *
      * The Lava is guaranteed to flow on the cell at the lowest height among its neighbors and has a probability (SPREAD_CHANCE)
      * to flow onto other neighbors that are at a lower height.
      *
      * States : 1 - lava (the states' choice is arbitrary)
      *          2 - stone
      *          between 3 and ROCK_TIME - lava at 2 nd state (increments until reaching ROCK_TIME)
      */
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
	    				// get neighbors height
	    				float left = (float) world.getCellHeight((i+_dx-1)%(_dx), j);
	    				float right = (float) world.getCellHeight((i+_dx+1)%(_dx), j);
	    				float up = (float) world.getCellHeight(i , (j+_dy+1)%(_dy));
	    				float down = (float) world.getCellHeight(i , (j+_dy-1)%(_dy));

                        // get the state of the neighbors
                        int lstate = this.getCellState( (i+_dx-1)%(_dx) , j );
                        int rstate = this.getCellState( (i+_dx+1)%(_dx) , j );
                        int ustate = this.getCellState( i , (j+_dy+1)%(_dy) );
                        int dstate = this.getCellState( i , (j+_dy-1)%(_dy) );

                        //look for the minimum height among neighbors
                        float min = left; //track variable to find the minimum
                        int mini = 0; //index for the minimum height
                        int count = 0; //count the number of neighbors who are at a lower height
                        float[][] dir = new float[4][3]; //save information for each cell : x, y, height

                        if (left < height) {
                            count++;
                            dir[0][0] = (i+_dx-1)%(_dx);
                            dir[0][1] = j;
                            dir[0][2] = left;
                        }

                        if (right < height) {
                            count++;
                            dir[1][0] = (i+_dx+1)%(_dx);
                            dir[1][1] = j;
                            dir[1][2] = right;
                            if (right < min) {
                                min = right;
                                mini = 1;
                            }
                        }

                        if (up < height) {
                            count++;
                            dir[2][0] = i;
                            dir[2][1] = (j+_dy+1)%(_dy);
                            dir[2][2] = up;
                            if (up < min) {
                                min = up;
                                mini = 2;
                            }
                        }

                        if (down < height) {
                            count++;
                            dir[3][0] = i;
                            dir[3][1] = (j+_dy-1)%(_dy);
                            dir[3][2] = down;
                            if (down < min) {
                                min = down;
                                mini = 3;
                            }
                        }

                        int state = this.getCellState((int) dir[mini][0], (int) dir[mini][1]);
                        if ((state == 0 || state == 2) && count > 0) {
                            world.getForest().swapBuffer(); // buffer has been changed by the previous call we need to come back to it
                            flowLava((((int) dir[mini][0]) + _dx) %_dx, (((int) dir[mini][1]) + _dy) % _dy, dir[mini][2]);
                            double r = Math.random();
                            int max_it = 3;
                            while (r < SPREAD_CHANCE && max_it > 0) {
                                int ind = (int)(Math.random()*count);
                                flowLava((((int) dir[mini][0]) + _dx) %_dx, (((int) dir[mini][1]) + _dy) % _dy, dir[ind][2]);
                                r = Math.random();
                                max_it--;
                            }
                            world.getForest().swapBuffer(); // swap back
                        }

                        this.setCellState(i, j, 3); //set current cell to 2 nd lava state
	    			}
	    			else
	    			{
	        				if ( this.getCellState(i, j) >= 3 && this.getCellState(i, j) < ROCK_TIME) // if is lava at 2 nd state
	        				{
                                if (world.getLandscape().getWeather().getCondition() == Condition.RAINY) {
                                    if (Math.random() < .3d) {
                                        this.setCellState(i, j, 2); //there is less than half a chance the lava becomes stone because of the rain
                                    }
                                } else
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
                        color[0] = 0.3f * (height+.1f) + 0.2f;
                        color[1] = 0.3f * (height+.1f) + 0.2f;
                        color[2] = 0.3f * (height+.1f) + 0.2f;
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

    /*
     * If the time condition is favorable (not rainy) and according to the height of the cell where the lava would flow on (which defines the state of the cell)
     * the lava will flow on it or not as lava or stone.
     */
    private void flowLava(int x, int y, float height) {
        if (world.getLandscape().getWeather().getCondition() == Condition.RAINY) {
            if (Math.random() < .3d) {
                this.setCellState(x, y, 2); //there is less than half a chance the lava becomes stone because of the rain
                return;
            }
        }
        if (height >= WorldOfTrees.WATER_LEVEL) {
            this.setCellState(x, y, 1); //set lava
            if (world.getCellValue(x, y) == 1) { // if lava is flowing on tree then it burns
                world.setCellValue(x, y, 2);
            }
        } else {
            this.setCellState(x, y, 2); //set rock
        }
    }
}
