package utils;
import applications.simpleworld.Prey;
import worlds.*;

public class PredatorVision extends VisionField {

    protected int orientation;

    public PredatorVision(int x, int y, int range, int orientation, World world) {
        super(x, y, range, world);
        this.orientation = orientation;
        calculateField();
    }

    protected void calculateField() {
    /* Creates n x 2 array, where n is the area of the triangular vision field, fanning out in front of the predator.
       This array serves as a priority queue, so that nearer prey will have priority over more distant prey. */

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
                System.out.println("Erreur : calcul de champs de vision, predator orientation");
        }
    }

    public Prey searchPrey(PoolPrey prey)    {  
    /* Finds the nearest prey in the field of vision and returns it.
       Returns null if no prey is found. */          
        Prey p;
        for (int i=1; i<field.length; i++)  {               //If there's a prey at i=0, it has already been eaten.
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


    public int getOrientation(int o)    {
        return this.orientation;
    }

    public void setOrientation(int o)   {
        this.orientation = o;
    }

}