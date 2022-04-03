package applications.simpleworld;
import worlds.World;

public class Pineapple extends Plant {

    private int numSlices;

    public Pineapple(int __x , int __y, WorldOfTrees __world)  {
        super(__x,__y,__world);
        this.growth_rate = 900;
    }

    public void step()  {
        super.step();
    }

/*
    public void drawFruit(float[] color, float radius1, float height, float altitude, int x2, int y2, World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)  {

        int[] sequence = new int[5];

        for (int i=0; i<8; i++) {
            sequence[0] = i;
            sequence[1] = i+8;
            sequence[2] = (i+7)%8 + 8;
            sequence[3] = (i+7)%8;
            sequence[4] = i;


            for (int j=0; j<sequence.length; j++)   {
                switch(sequence[i]) {                                                                                               // 0-7: vertices of the previous octagon, 8-15: vertices of the next octagon
                    case 0:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1 / 3, offset+y2*stepY-lenY*radius1, height*normalizeHeight + h);
                        break;
                    case 1:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1, offset+y2*stepY-lenY*radius1 / 3, height*normalizeHeight + h);
                        break;
                    case 2:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1, offset+y2*stepY+lenY*radius1 / 3, height*normalizeHeight + h);
                        break;
                    case 3:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1 / 3, offset+y2*stepY+lenY*radius1, height*normalizeHeight + h);
                        break;
                    case 4:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1 / 3, offset+y2*stepY+lenY*radius1, height*normalizeHeight + h);
                        break;
                    case 5:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1, offset+y2*stepY+lenY*radius1 / 3, height*normalizeHeight + h);
                        break;
                    case 6:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1, offset+y2*stepY-lenY*radius1 / 3, height*normalizeHeight + h);
                        break;
                    case 7:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1 / 3, offset+y2*stepY-lenY*radius1, height*normalizeHeight + h);
                        break;
                    case 8:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2 / 3, offset+y2*stepY-lenY*radius2, height*normalizeHeight + h);
                        break;
                    case 9:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2, offset+y2*stepY-lenY*radius2 / 3, height*normalizeHeight + h);
                        break;
                    case 10:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2, offset+y2*stepY+lenY*radius2 / 3, height*normalizeHeight + h);
                        break;
                    case 11:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2 / 3, offset+y2*stepY+lenY*radius2, height*normalizeHeight + h);
                        break;
                    case 12:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2 / 3, offset+y2*stepY+lenY*radius2, height*normalizeHeight + h);
                        break;
                    case 13:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2, offset+y2*stepY+lenY*radius2 / 3, height*normalizeHeight + h);
                        break;
                    case 14:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2, offset+y2*stepY-lenY*radius2 / 3, height*normalizeHeight + h);
                        break;
                    case 15:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2 / 3, offset+y2*stepY-lenY*radius2, height*normalizeHeight + h);
                        break;
                    default:
                }
            }
        }




    }

    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ) {

        float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );
        float altitude = (float)height * normalizeHeight ;

        int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();


        gl.glColor3f(color[0],color[1],color[2]);
        drawFruit(radius, height, altitude,x2,y2,myWorld, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight);

    }
    */
}