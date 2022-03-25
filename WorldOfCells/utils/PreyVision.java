package utils;
import worlds.*;

public class PreyVision extends VisionField {


    public PreyVision(int x, int y, int range, World world) {
        super(x, y, range, world);
        calculateField();
    }

    protected void calculateField() {
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
                if (i < size)   {
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

}