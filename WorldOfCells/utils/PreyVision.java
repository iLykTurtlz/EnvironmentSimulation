package utils;
import java.util.ArrayList;

import applications.simpleworld.Plant;
import applications.simpleworld.Predator;
import worlds.*;

public class PreyVision extends VisionField {


    public PreyVision(int x, int y, int range, World world) {
        super(x, y, range, world);
        calculateField();
    }

    protected void calculateField() {
        /* Creates an n x 2 array where n is the array of the square vision field extending 'range' squares out from the prey in four directions.
           This array serves as a list in order of priority, so that nearer predators will have priority over more distant predators. */
        int size = 4*range*(range+1)+1;                 //number of coordiantes = (2*range+1)^2 
        int direction = 0;                              //priority will follow a spiral, nearest to farthest, (0,1,2,3) = (N,E,S,W)
        int length = 1;                                 //length before changing direction
        int width = world.getWidth();
        int height = world.getHeight();
        boolean incrementLength = true;                 //actually this will switch to false after the first inner loop.  We want to increment once for every two executions of the inner loop.
        
        int xi = x;
        int yi = y;

        field = new int[size][2];
        field[0][0] = xi;
        field[0][1] = yi;

        int i = 1;
        while (i < size)  {
            for (int j=0; j<length; j++)    {
                switch (direction)  {
                    case 0:
                        yi = (yi + 1 + height) % height;
                        break;
                    case 1:
                        xi = (xi + 1 + width) % width;
                        break;
                    case 2:
                        yi = (yi - 1 + height) % height;
                        break;
                    case 3:
                        xi = (xi - 1 + width) % width;
                        break;
                    default:
                        System.out.println("Erreur : direction du parcours spiral");
                }
                if (i < size)   {   //without this, the algo oversteps the array size by 1!
                    field[i][0] = xi;
                    field[i][1] = yi;
                }
                i++;
            }
            incrementLength = !incrementLength;
            if (incrementLength)    {
                length++;
            }
            direction = (direction + 1 + 4) % 4;
        }
    }

    public Predator searchPredator(PoolPredator predators)  {
        /* Finds the nearest predator in the field and returns it.
           Returns null if no predator is found */
        Predator p;
        for (int i=1; i<field.length; i++)  {   //if the predator is at i=0, it's on the same space, which means random movement is as good as fleeing.
            for (int j=0; j<predators.getSizeUsed(); j++)    {
                p = predators.get(j);
                int[] coord = p.getCoordinate();
                if (coord[0] == field[i][0] && coord[1] == field[i][1]) {
                    return p;
                }
            }
        }
        return null;
    }

    public Plant searchFood(ArrayList<Plant> plants)   {
        /* Finds the nearest plant in the field and returns it.
           Returns null if no plant is found. */
        Plant p;
        for (int i=0; i<field.length; i++)  {
            for (int j=0; j<plants.size(); j++)  {
                p = plants.get(j);
                int[] coord = p.getCoordinate();
                if (coord[0] == field[i][0] && coord[1] == field[i][1]) {
                    return p;
                }
            }
        }
        return null;
    }


}