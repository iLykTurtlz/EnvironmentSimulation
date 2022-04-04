package applications.simpleworld;
import worlds.World;

import com.jogamp.opengl.GL2;

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


    public void drawFruit(int band, float radius1, float radius2, float h1, float h2, float h1Norm, float h2Norm, float bandThickness, float bandThicknessNorm, float altitude, int x2, int y2, World myWorld, GL2 gl, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)  {

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
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1 / 3, offset+y2*stepY-lenY*radius1, altitude + h1);
                        break;
                    case 1:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1, offset+y2*stepY-lenY*radius1 / 3, altitude + h1);
                        break;
                    case 2:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1, offset+y2*stepY+lenY*radius1 / 3, altitude + h1);
                        break;
                    case 3:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius1 / 3, offset+y2*stepY+lenY*radius1, altitude + h1);
                        break;
                    case 4:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1 / 3, offset+y2*stepY+lenY*radius1, altitude + h1);
                        break;
                    case 5:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1, offset+y2*stepY+lenY*radius1 / 3, altitude + h1);
                        break;
                    case 6:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1, offset+y2*stepY-lenY*radius1 / 3, altitude + h1);
                        break;
                    case 7:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius1 / 3, offset+y2*stepY-lenY*radius1, altitude + h1);
                        break;
                    case 8:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2 / 3, offset+y2*stepY-lenY*radius2, altitude + h2);
                        break;
                    case 9:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2, offset+y2*stepY-lenY*radius2 / 3, altitude + h2);
                        break;
                    case 10:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2, offset+y2*stepY+lenY*radius2 / 3, altitude + h2);
                        break;
                    case 11:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius2 / 3, offset+y2*stepY+lenY*radius2, altitude + h2);
                        break;
                    case 12:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2 / 3, offset+y2*stepY+lenY*radius2, altitude + h2);
                        break;
                    case 13:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2, offset+y2*stepY+lenY*radius2 / 3, altitude + h2);
                        break;
                    case 14:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2, offset+y2*stepY-lenY*radius2 / 3, altitude + h2);
                        break;
                    case 15:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius2 / 3, offset+y2*stepY-lenY*radius2, altitude + h2);
                        break;
                    default:
                }
            }
        }

        float h3Norm = h2Norm+bandThicknessNorm;
        float radius3 = (float)(size*(Math.sqrt(0.125 - ((h3Norm-0.5)*(h3Norm-0.5))/2)));

        if (band < nbBands) {
            drawFruit(++band, radius2, radius3, h2, h2+bandThickness, h2Norm, h2Norm + bandThicknessNorm, bandThickness, bandThicknessNorm, altitude, x2, y2, myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        } else {
            drawBottomOrTop(gl, x2, y2, offset, stepX, stepY, lenX, lenY, radius2, altitude, h2);
        }





    }
    

    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ) {


        float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );
        float zoff = myWorld.getLandscape().getZOffset();
        float altitude = (float)height * normalizeHeight +zoff;


        float bandThickness = size/(nbBands + 2);
        float bandThicknessNorm = 1/(nbBands + 2);

        float h1 = baseHeight;
        float h2 = h1 + bandThickness;

        float h1Norm = bandThicknessNorm;
        float h2Norm = h1Norm+bandThicknessNorm;


        

        float radius1 = (float)(size*(Math.sqrt(0.125 - ((h1Norm-0.5)*(h1Norm-0.5))/2)));     //upper half of an ellipse whose major axis spans the interval [0,1], multiplied by size which is the height of the ellipse that the pineapple is based on (top and bottom cut off)
        float radius2 = (float)(size*(Math.sqrt(0.125 - ((h2Norm-0.5)*(h2Norm-0.5))/2)));

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
        drawBottomOrTop(gl, x2, y2, offset, stepX, stepY, lenX, lenY, radius1, altitude, baseHeight);
        drawFruit(0, radius1, radius2, h1, h2, h1Norm, h2Norm, bandThickness, bandThicknessNorm, altitude,x2,y2,myWorld, gl, offset, stepX, stepY, lenX, lenY, normalizeHeight);

    }
    
    
}