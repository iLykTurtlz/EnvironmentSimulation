// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import java.util.ArrayList;

import javax.lang.model.util.ElementScanner14;

import com.jogamp.opengl.GL2;

import applications.simpleworld.Weather.Condition;
import applications.simpleworld.Weather.Time;
import objects.UniqueDynamicObject;
import utils.DisplayToolbox;
import utils.PoolPredator;
import utils.PoolPrey;
import worlds.World;

public abstract class Agent extends UniqueDynamicObject{

    public static final double P_DOUSE = 0.01;                              // Probability of an agent who is ON_FIRE of being extinguished by rain or snow

    
    protected int age;
    protected int orientation;                                              // (0,1,2,3) = (N,E,S,W)
    protected int remainingBurnTime;
    protected int hunger;                                                   // This value increases to motivate the agent to look for food, but it rarely results in the death of the agent.  They usually die from old age or from being eaten.
    protected int appetiteThreshold;
    protected int fatigue;                                                  // This motivates the agent to rest
    


    protected int defaultBaseSpeed;                                         
    protected int baseSpeed;                
    protected int speed;                                                    // between 0 and 100, varies relative to base speed (varying the speed without this point of reference would lead to )
    protected double probablityChangeDirection;                             //probability of random movement in the case where there are no threats or food nearby, otherwise the agents move straight ahead.


    protected boolean directions[];                                         // true if the direction is accessible, false otherwise : indices (0,1,2,3) = (N,E,S,W)
    protected int accessible;                                               // number of accessible directions


    protected float scalingFactor;                                          // the original agents in this program had a body height of 4.  We have kept this the same.
    protected float[] headColor;                                            // to distinguish different types of agents, their heads and bodies are diffent colors.
    protected float[] bodyColor;  
                                   


	
    public Agent ( int __x , int __y, WorldOfTrees __world, float[] headColor, float[] bodyColor )
	{
		super(__x,__y,__world);
        age = 0;
        orientation = (int)(4*Math.random());      //random orientation by default
        state = State.ALIVE;
        remainingBurnTime = 80;
        hunger = 0;
        fatigue = 0;
        

        probablityChangeDirection = 0.25;

        directions = new boolean[4];    // indices (0,1,2,3) = (N,E,S,W)
        for (int i=0; i<directions.length; i++) {
            directions[i] = true;
        }
 
        this.headColor = headColor;
        this.bodyColor = bodyColor;
        scalingFactor = 4.f;
	}


	
	public void step() {

        if ( world.getIteration() % (100 - speed) == 0 )   {

            updateAge();
            updateHunger();

            //if the agent is touched by fire or lava (and not already on fire) it catches fire.
            if ( state != State.ON_FIRE && (world.getLandscape().getVolcano().isLava(x,y) || world.getForest().getCellState(x,y) == 2) ) { 
                bodyColor[0] = 1.f;
                bodyColor[1] = 0.5f;
                bodyColor[2] = 0;
                state = State.ON_FIRE;
                speed = baseSpeed + 20;
            } 


            if ( state == State.ON_FIRE )   {
                //if the agent is on fire, trees with same same position as well as adjacent UniqueDynamicObjects will catch fire too.
                spreadFire();   

                //if it is raining or snowing, there is a chance the agent will be doused
                if (world.getLandscape().getWeather().getCondition() == Condition.RAINY || world.getLandscape().getWeather().getCondition() == Condition.SNOWING )  {
                    if (Math.random() < P_DOUSE)    {
                        state = State.ALIVE;
                    }
                }

                //if time runs out, the agent dies
                if (remainingBurnTime > 0)  {
                    remainingBurnTime--;
                } else {
                    state = State.DEAD;
                }

            }
            
            
            

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
                hLeft  = this.world.getCellHeight(left, this.y),
                hThis  = this.world.getCellHeight(this.x,this.y);

            // Block off water and cliffs      
            if ( (hAbove < WorldOfTrees.WATER_LEVEL) /*|| (Math.abs(hAbove - hThis)) > 0.01*/ )    {
                directions[0] = false;
                accessible--;
            }
            if ( (hRight < WorldOfTrees.WATER_LEVEL) /*|| (Math.abs(hRight - hThis) > 0.01)*/ )   {
                directions[1] = false;
                accessible--;
            }
            if ( (hBelow < WorldOfTrees.WATER_LEVEL) /*|| (Math.abs(hBelow - hThis) > 0.01)*/ )    {
                directions[2] = false;
                accessible--;
            }
            if ( (hLeft < WorldOfTrees.WATER_LEVEL) /*|| (Math.abs(hLeft - hThis) > 0.01)*/ )      {
                directions[3] = false;
                accessible--;
            }

            
        }


    }
	


    public void rampage()    {
        speed = baseSpeed + 20;
        int move = updatePosition(-1);
        setOrientation(move);
        reinitializeDirections();
    }


    /* GETTERS AND SETTERS */

    public void updateAge()   {
        this.age++;
    }

    public void updateHunger()  {
        this.hunger++;
    }

    public void reinitialize()  {
        this.age = 0;
        this.state = State.ALIVE;
        this.orientation = (int)(4*Math.random());
        this.hunger = 0;
        this.fatigue = 0;
        this.scalingFactor = 4.f;
    }





    public int updatePosition(int move)    {
        /* Updates the agent's position based on an int value:
            -1 -> 
                if (dice > probabilityChangeDirection and directions[orientation]) then move straight ahead
                otherwise random displacement
            -2 -> no change
            (0,1,2,3) -> (N,E,S,W)
            Returns the final value of move, which is needed to set predator orientation.
        */

        //Random movements
        double dice = Math.random();
        if (move == -1) {   

            if ( directions[orientation] && dice > probablityChangeDirection ) {        //straight ahead
                       
                    move = orientation;

            }                                 
            else {


                int j=0;                                                                //random displacement
                double partition_size = 1/((double)accessible);

                for (int i=0; i<directions.length; i++)    {
                    if ( directions[i] )    {
                        j++;
                        if ( dice < (j*partition_size) )  {    
                            move = i;
                            break;
                        }
                    }
                }
                if (move == -1)  {      //In rare cases, double comparison fails and move is still -1.  When that happens, the move should be the LAST accessible direction in directions.
                    for (int i = directions.length - 1; i > -1; i--)    {
                        if (directions[i])  {
                            move = i;
                            break;
                        }
                    }
                }
            }

        }


        
        if ( dice < 0.6 && !directions[orientation] && directions[(orientation + 2)%4]) {        //when an Agent hits an obstacle, they have a tendency to get stuck.  To avoid this nuisance, we send them in the opposite direction, if possible.
            if (directions[(orientation + 2)%4])    {
                move = (orientation + 2)%4;
            }   
        }
        


        //Having determined the direction we now set the agent's position
        switch (move)   {   
            case -2:
                break; 
            case 0:
                this.y = (this.y + 1 + this.world.getHeight()) % this.world.getHeight();
                break;
            case 1:
                this.x = (this.x + 1 + this.world.getWidth()) % this.world.getWidth();
                break;
            case 2:
                this.y = (this.y - 1 + this.world.getHeight()) % this.world.getHeight();
                break;
            case 3:
                this.x = (this.x - 1 + this.world.getWidth()) % this.world.getWidth();
                break;
            default:
                System.out.println("Erreur de déplacement : updatePosition(),move = " + move);
        }
        return move;
    }

    public void catchFire() {
        state = State.ON_FIRE;
    }

    public void burnDown()  {
        // This function will first test if the Agent is on fire, so it can be called with every step()
        if (state == State.ON_FIRE)  {
            if (remainingBurnTime > 0)  {
                remainingBurnTime--;
            } else {
                state = State.DEAD;
            }
        }    
    }

    public void reinitializeDirections()    {
        //This function reinitializes the four directions to true, making them all accessible 
        for (int i=0; i<directions.length; i++)    {
            directions[i] = true;
        }
    }

    public float calculateRadius(float h)    {
        // takes in a float from the interval [0,1] and returns the value of sin(sqrt(3*pi*x))/10, multiplied by a scaling factor, which is used to draw the agent's body.
        return (float)(scalingFactor * Math.sin(Math.sqrt(3*Math.PI*h))*0.3);
    }

    public float calculateSphereRadius(float h) {
        // takes in a float from the interval [0,1] and returns the radius of a slice of a sphere of radius 1/2
        return (float)(Math.sqrt(0.25 - (h-0.5)*(h-0.5)));
    }

    public int getAge()    {
        return this.age;
    }

    public State getState()  {
        return this.state;
    }

    public void setOrientation(int move) {
        // sets orientation based on direction of movement:
        // -2 -> no change
        if (move != -2)
            this.orientation = move;
    }
    

 
}
