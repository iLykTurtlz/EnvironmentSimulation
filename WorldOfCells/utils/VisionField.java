package utils;
import java.util.ArrayList;

import applications.simpleworld.Plant;
import applications.simpleworld.Predator;
import applications.simpleworld.Prey;
import applications.simpleworld.WorldOfTrees;
import worlds.*;


public abstract class VisionField    {
    int[][] field;
    int range;
    WorldOfTrees world;
    int x;
    int y;

    public VisionField(int x, int y, int range, WorldOfTrees world)  {
        this.x = x;
        this.y = y;
        this.range = range;
        this.world = world;
    }

    protected abstract void calculateField();

    public void updateField()    {
        calculateField();
    }

    public Predator searchPredator(PoolPredator predators)  {
        /* Finds the nearest predator in the field and returns it.
           Returns null if no predator is found */
        Predator p;
        for (int i=0; i<field.length; i++)  {   //if the predator is at i=0, it's on the same space.
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

    public Predator searchPredator(PoolPredator predators, int[] coordinate)   {
        /* Lighter version of searchPredator that resumes the search from a particular coordinate */
        Predator p;
        int i=0;
        while (  (i < field.length) && (coordinate[0] != field[i][0] || coordinate[1] != field[i][1])  )  {
            i++;
        }
        for (int j = i+1; j<field.length; j++)    {
            for (int k=0; k<predators.getSizeUsed(); k++)   {
                p = predators.get(j);
                int[] coord = p.getCoordinate();
                if (coord[0] == field[j][0] && coord[1] == field[j][1]) {
                    return p;
                }
            }
        }
        return null;
    }

    public Prey searchPrey(PoolPrey prey)    {  
        /* Finds the nearest prey in the field of vision and returns it.
           Returns null if no prey is found. */          
        Prey p;
        for (int i=0; i<field.length; i++)  {            
            for (int j=0; j<prey.getSizeUsed(); j++)    {
                p = prey.get(j);
                int[] coord = p.getCoordinate();
                if (coord[0] == field[i][0] && coord[1] == field[i][1]) {
                    return p;
                }
            }
        }
        return null;
    }

    public Plant searchPlant(ArrayList<Plant> plants)   {
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

    /* GETTERS AND SETTERS */

    public void setPosition(int x, int y)   {
        this.x = x;
        this.y = y;
    }

    public void setRange(int r) {
        this.range = r;
    }

    public int getRange(int r) {
        return this.range;
    }

    public int[][] getField()   {
        return field;
    }

}