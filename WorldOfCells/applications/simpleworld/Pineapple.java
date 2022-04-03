package applications.simpleworld;
import worlds.World;

public class Pineapple extends Plant {

    private int nbBands;
    private float baseHeight;
    private float[] fruitColor;

    public Pineapple(int __x , int __y, WorldOfTrees __world)  {
        super(__x,__y,__world);
        this.growth_rate = 900;
        this.nbBands = 10;
        this.baseHeight = 4.0f;
        this.fruitColor = new float[]{1.f,1.f,0.f};
    }

    public void step()  {
        super.step();
    }

/*

    public void drawBottomOrTop(GL2 gl, int x2, int y2, float offset, float stepX, float stepY, float lenX, float lenY, float radius, float altitude, float h)  {
        // Draws an octagon for the bottom or top of the fruit 
        gl.glVertex3f( offset+x2*stepX-lenX*radius / 3, offset+y2*stepY-lenY*radius, altitude + h );
        gl.glVertex3f( offset+x2*stepX-lenX*radius, offset+y2*stepY-lenY*radius / 3, altitude + h );
        gl.glVertex3f( offset+x2*stepX-lenX*radius, offset+y2*stepY+lenY*radius / 3, altitude + h );
        gl.glVertex3f( offset+x2*stepX-lenX*radius / 3, offset+y2*stepY+lenY*radius, altitude + h );
        gl.glVertex3f( offset+x2*stepX+lenX*radius / 3, offset+y2*stepY+lenY*radius, altitude + h );
        gl.glVertex3f( offset+x2*stepX+lenX*radius, offset+y2*stepY+lenY*radius / 3, altitude + h );
        gl.glVertex3f( offset+x2*stepX+lenX*radius, offset+y2*stepY-lenY*radius / 3, altitude + h );
        gl.glVertex3f( offset+x2*stepX+lenX*radius / 3, offset+y2*stepY-lenY*radius, altitude + h );
        gl.glVertex3f( offset+x2*stepX-lenX*radius / 3, offset+y2*stepY-lenY*radius, altitude + h );
    }


    public void drawFruit(float radius1, float radius2, float h, float altitude, int x2, int y2, World myWorld, GL2 gl, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)  {

        int[] sequence = new int[5];

        for (int i=0; i<8; i++) {
            sequence[0] = i;
            sequence[1] = i+8;
            sequence[2] = (i+7)%8 + 8;
            sequence[3] = (i+7)%8;
            sequence[4] = i;


            for (int j=0; j<sequence.length; j++)   {
                switch(sequence[j]) {                                                                                               // 0-7: vertices of the previous octagon, 8-15: vertices of the next octagon
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
    */
/*
    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ) {

        
        float bandThickness = size/(nbBands + 3);

        float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );
        float zoff = myWorld.getLandscape().getZOffset();
        float altitude = (float)height * normalizeHeight +zoff;
        //float radius1 = (float) size*(Math.sqrt(0.125 - (x-0.5)*(x-0.5)));

        int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

        //leaves under the fruit
        gl.glColor3f(0,0.6f,0);     //green
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX-lenY/16.f, offset+y2*stepY+lenY/2.f, altitude + baseHeight );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX+lenY/16.f, offset+y2*stepY-lenY/2.f, altitude + baseHeight );
        
    	gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX-lenY/2.f, offset+y2*stepY+lenY/16.f, altitude + baseHeight );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX+lenY/2.f, offset+y2*stepY-lenY/16.f, altitude + baseHeight );


        gl.glColor3f(fruitColor[0],fruitColor[1],fruitColor[2]);
        //drawBottomOrTop(gl, x2, y2, offset, stepX, stepY, lenX, lenY, radius, altitude, h);
        //drawFruit(radius, height, altitude,x2,y2,myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);

    }
    
    */
}