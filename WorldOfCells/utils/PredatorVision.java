package utils;
import java.util.ArrayList;

import applications.simpleworld.Agent;
import applications.simpleworld.Plant;
import applications.simpleworld.Predator;
import applications.simpleworld.Prey;
import applications.simpleworld.WorldOfTrees;
import objects.UniqueDynamicObject.State;
import worlds.World;


public class PredatorVision extends VisionField {

    protected int orientation;

    public PredatorVision(int x, int y, int range, int orientation, WorldOfTrees world) {
        super(x, y, range, world);
        this.orientation = orientation;
        calculateField();
    }

    protected void calculateField() {
    // Creates an n x 2 array, where n is the area of the triangular vision field, fanning out in front of the predator.
    // This array serves as a priority list, so that nearer prey will have priority over more distant prey.

        field = new int[(range+1)*(range+1)][2];    //(range+1)^2 coordinates == the area of the vision field.
        int k=0;                                    // index that will span the first dimension of the field array.
        int width = world.getWidth();
        int height = world.getHeight();
        switch (orientation)    {                   //the order of priority goes from nearest to farthest (index i), and from directly in front to off to the side (index j).
            case 0:
                for (int i=0; i<=range; i++)    {
                    for(int j=0; j<=i; j++) {
                        if (j > 0)  {
                            field[k][0] = (x + j + width) % width;      //i spaces in front and j spaces to the right
                            field[k++][1] = (y + i + height) % height;

                            field[k][0] = (x - j + width) % width;      //i spaces in front and j spaces to the left
                            field[k++][1] = (y + i + height) % height;

                        } else {
                            field[k][0] = x;                            //i spaces directly in front
                            field[k++][1] = (y + i + height) % height;

                        }
                    }
                }
                break;
            case 1:
                for (int i=0; i<=range; i++)    {
                    for(int j=0; j<=i; j++) {
                        if (j > 0)  {
                            field[k][0] = (x + i + width) % width;      //idem
                            field[k++][1] = (y + j + height) % height;

                            field[k][0] = (x + i + width) % width;
                            field[k++][1] = (y - i + height) % height;

                        } else {
                            field[k][0] = (x + i + width) % width;
                            field[k++][1] = y;

                        }
                    }
                }
                break;
            case 2:
                for (int i=0; i<=range; i++)    {
                    for(int j=0; j<=i; j++) {
                        if (j > 0)  {
                            field[k][0] = (x + j + width) % width;
                            field[k++][1] = (y - i + height) % height;

                            field[k][0] = (x - j + width) % width;
                            field[k++][1] = (y - i + height) % height;

                        } else {
                            field[k][0] = x;
                            field[k++][1] = (y - i + height) % height;

                        }
                    }
                }
                break;
            case 3:
                for (int i=0; i<=range; i++)    {
                    for(int j=0; j<=i; j++) {
                        if (j > 0)  {
                            field[k][0] = (x - i + width) % width;
                            field[k++][1] = (y + j + height) % height;

                            field[k][0] = (x - i + width) % width;
                            field[k++][1] = (y - i + height) % height;

                        } else {
                            field[k][0] = (x - i + width) % width;
                            field[k++][1] = y;

                        }
                    }
                }
                break;
            default :
                System.out.println("Erreur : calcul de champs de vision, predator orientation = "+orientation);
        }
    }

    public void irradiate(WorldOfTrees w)   {
        // Godzilla uses this method to irradiate all UniqueDynamicObjects and Trees in range
        ArrayList<Plant> plants = w.getPlants();
        PoolPredator predators = w.getPredators();
        PoolPrey prey = w.getPrey();
        Plant p1;
        Predator p2;
        Prey p3;
        int[] coord;

        for (int i=0; i<plants.size(); i++) {
            for (int j=0; j<field.length; j++)  {
                //Kill Trees
                if (w.getCellHeight(field[j][0], field[j][1]) > WorldOfTrees.WATER_LEVEL && w.getCellHeight(field[j][0], field[j][1]) < WorldOfTrees.TREE_LINE) {
                    w.getForest().setCellState(field[j][0], field[j][1],3);
                }

                //Kill Plants
                p1 = plants.get(i);
                coord = p1.getCoordinate();
                if (coord[0] == field[j][0] && coord[1] == field[j][1]) {
                    p1.setState(State.IRRADIATED);
                }
            }
        }

        for (int i=0; i<predators.getSizeUsed(); i++) {
            for (int j=0; j<field.length; j++)  {
                p2 = predators.get(i);
                coord = p2.getCoordinate();
                if (coord[0] == field[j][0] && coord[1] == field[j][1]) {
                    p2.setState(State.IRRADIATED);
                }
            }
        }

        for (int i=0; i<prey.getSizeUsed(); i++) {
            for (int j=0; j<field.length; j++)  {
                p3 = prey.get(i);
                coord = p3.getCoordinate();
                if (coord[0] == field[j][0] && coord[1] == field[j][1]) {
                    p3.setState(State.IRRADIATED);
                }
            }
        }        
    }


    public Agent search(Pool<Agent> p) {
        Agent a;
        for (int i=2; i<field.length; i++)   {
            for (int j=0; j<p.getSizeUsed(); j++)   {
                a = p.get(j);
                int[] coord = a.getCoordinate();
                if (coord[0] == field[i][0] && coord[1] == field[i][1]) {
                    return a;
                }
            }
        }
        return null;
    }

    public int getOrientation(int o)    {
        return this.orientation;
    }

    public void setOrientation(int o)   {
        this.orientation = o;
    }

}