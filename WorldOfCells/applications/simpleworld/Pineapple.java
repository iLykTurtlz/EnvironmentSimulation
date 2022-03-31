package applications.simpleworld;
import worlds.World;

public class Pineapple extends Plant {


    public Pineapple(int __x , int __y, WorldOfTrees __world)  {
        super(__x,__y,__world);
    }

    public void step()  {
        super.step();
    }

/*
    public void grow(float radius, float height, float altitude, int x2, int y2, World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)  {

        switch(sequence[i]) {
            case 0:
                gl.glVertex3f( offset+x2*stepX-lenX*radius, offset+y2*stepY-lenY*radius, height*normalizeHeight + h);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;
            default:
        }

    }

    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ) {

        float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );
        float altitude = (float)height * normalizeHeight ;

        int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

        grow(radius, height, altitude,x2,y2,myWorld, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight);

    }
    */
}