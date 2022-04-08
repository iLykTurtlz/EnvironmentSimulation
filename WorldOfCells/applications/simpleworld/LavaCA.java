package applications.simpleworld;

import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;
import applications.simpleworld.WorldOfTrees;

public class LavaCA extends CellularAutomataInteger {

	private WorldOfTrees world;
	private static final int ROCK_TIME = 30;
	private static final double SPREAD_CHANCE = 0.3d;

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
                        int x, y;
                        float min = left;
                        int mini = 0;
                        int state = lstate;
                        int count = 0;
                        float[][] dir = new float[4][3];

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

                        /*if (min > right) {
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
                        }*/
                        state = this.getCellState((int) dir[mini][0], (int) dir[mini][1]);
                        if ((state == 0 || state == 2) && count > 0) {
                            world.getForest().swapBuffer(); // buffer has been changed by the previous call we need to come back to it
                            flowLava((int) dir[mini][0], (int) dir[mini][1], dir[mini][2]);
                            double r = Math.random();
                            while (r < SPREAD_CHANCE) {
                                int ind = (int)(Math.random()*count);
                                flowLava((int) dir[ind][0], (int) dir[ind][1], dir[ind][2]);
                                r = Math.random();
                            }
                        }

                        /*if ((state == 0 || state == -1 || state == 2) && min < height) {
                            if (min >= WorldOfTrees.WATER_LEVEL) {
                                this.setCellState(x, y, 1); //set lava
                                if (world.getCellValue(x, y) == 1) { // if lava is flowing on tree then it burns
                                    world.getForest().swapBuffer(); // buffer has been changed by the previous call we need to come back to it
                                    world.setCellValue(x, y, 2);
                                }
                            } else
                                this.setCellState(x, y, 2); //set rock
                        }*/
                        this.setCellState(i, j, 3); //set current cell to 2 nd lava state
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

    private void flowLava(int x, int y, float height) {
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
