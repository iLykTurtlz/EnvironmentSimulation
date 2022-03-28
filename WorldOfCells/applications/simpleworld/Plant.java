package applications.simpleworld;

import javax.lang.model.util.ElementScanner14;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

public class Plant extends UniqueDynamicObject {
    private static int MAX_PETALS = 11;
    private float[] stemColor;
    private int totalPetals;

    public Plant(int __x , int __y, World __world)  {
        super(__x,__y,__world);
        stemColor = new float[] {1.f,0.f,1.f};
        this.totalPetals = 0;           
    }

    public void step()  {
        if ( world.getIteration() % 100 == 0 && totalPetals <= MAX_PETALS) {
            totalPetals++;
        }
    }

    public static float[] incrementColor(float petalColor[])    {
        /* Increments the color according to the spectrum*/
        // TO DO : make this BETTER!
        float[] rgb = new float[3];
        for (int i=0; i<petalColor.length; i++) {
            if (petalColor[i] == 0.f)   {
                if (petalColor[(i+1)%petalColor.length] == 0.f)
                    rgb[i] = 0.5f;
                else
                    rgb[i] = petalColor[i];
            }
            else if (petalColor[i] == 0.5f) {
                if (petalColor[(i+1)%petalColor.length] == 0.f)
                    rgb[i] = 1.f;
                else
                    rgb[i] = 0.f;
            }
            else {
                if (petalColor[(i+1)%petalColor.length] == 1.f)
                    rgb[i] = 0.5f;
                else
                    rgb[i] = petalColor[i];
            }
        }
        return rgb;
    }

    public void bloom(int petals, float heightDecrement, float h, float petalWidth, float[] petalColor, float radius, int x2, int y2, float height, float altitude, GL2 gl,int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)  {
        /* This method recursively adds colored bands to the flower, incrementing its radius */
        int[] sequence = new int[4];
        
        for (int i=0; i<8; i++) {       //this 8-term sequence of quadruplets defines the 4 vertices (x8) needed to draw a petal: a concentric box.
            sequence[0] = i;
            sequence[1] = (i+4)%8;
            if (i<4)
                sequence[2] = (i+1)%4;
            else
                sequence[2] = (i-1)%4 + 4;
            sequence[3] = i;
            
            gl.glColor3f(petalColor[0],petalColor[1],petalColor[2]);
            for (int j=0; j<sequence.length; j++)   {
                switch (sequence[j])   {
                    case 0:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius, offset+y2*stepY-lenY*radius, height*normalizeHeight + h);
                        break;
                    case 1:
                        gl.glVertex3f( offset+x2*stepX-lenX*radius, offset+y2*stepY+lenY*radius, height*normalizeHeight + h);
                        break;
                    case 2:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius, offset+y2*stepY+lenY*radius, height*normalizeHeight + h);
                        break;
                    case 3:
                        gl.glVertex3f( offset+x2*stepX+lenX*radius, offset+y2*stepY-lenY*radius, height*normalizeHeight + h);
                        break;
                    case 4:
                        gl.glVertex3f( offset+x2*stepX-lenX*(radius+petalWidth), offset+y2*stepY-lenY*(radius+petalWidth), height*normalizeHeight + h - heightDecrement);
                        break;
                    case 5:
                        gl.glVertex3f( offset+x2*stepX-lenX*(radius+petalWidth), offset+y2*stepY+lenY*(radius+petalWidth), height*normalizeHeight + h - heightDecrement);
                        break;
                    case 6:
                        gl.glVertex3f( offset+x2*stepX+lenX*(radius+petalWidth), offset+y2*stepY+lenY*(radius+petalWidth), height*normalizeHeight + h - heightDecrement);
                        break;
                    case 7:
                        gl.glVertex3f( offset+x2*stepX+lenX*(radius+petalWidth), offset+y2*stepY-lenY*(radius+petalWidth), height*normalizeHeight + h - heightDecrement);
                        break;
                    default:
                        System.out.println("Erreur : creation d'une fleur");
                }

            }
        }

        if (petals < totalPetals)   
            bloom(++petals, heightDecrement + 0.03f,h - heightDecrement, 0.3f, incrementColor(petalColor), radius+petalWidth, x2, y2, height, altitude, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight); 
        
        
    }

    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ) {

        float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );
        float altitude = (float)height * normalizeHeight ;

        int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();
        
        //stems
        gl.glColor3f(stemColor[0],stemColor[1],stemColor[2]);
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX-lenY/16.f, offset+y2*stepY+lenY/2.f, altitude + 4.f );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX+lenY/16.f, offset+y2*stepY-lenY/2.f, altitude + 4.f );
        
    	gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX-lenY/2.f, offset+y2*stepY+lenY/16.f, altitude + 4.f );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX+lenY/2.f, offset+y2*stepY-lenY/16.f, altitude + 4.f );
        

        //floral disc
        gl.glColor3f(1.f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.2f, offset+y2*stepY-lenY*0.2f, height*normalizeHeight + 5.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.2f, offset+y2*stepY+lenY*0.2f, height*normalizeHeight + 5.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.2f, offset+y2*stepY+lenY*0.2f, height*normalizeHeight + 5.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.2f, offset+y2*stepY-lenY*0.2f, height*normalizeHeight + 5.0f);

        bloom(0, 0, 5.0f, 0.3f, new float[]{1.f,0.5f,0.f}, 0.2f, x2, y2, height, altitude, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight);
        /*

        gl.glColor3f(1.f,0.f,0.f);

        
        //2,6,3,2
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        //7,3,6,7
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);

        //3,7,4,3
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        //8,4,7,8
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);

        //4,8,1,4
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);

        //5,1,8,5
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);

        //1,5,2,1
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);

        //6,2,5,6
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        */


        /*
        //1,2,3,4 = inner square
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.0f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.0f);

        //5,6,7,8 = outer square
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY+lenY*0.6f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX*0.6f, offset+y2*stepY-lenY*0.6f, height*normalizeHeight + 4.1f);
        */






        //4 square petals

        /*
        gl.glColor3f(1.f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);

        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY*1.5f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY*1.5f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX+lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        */

        /*
        gl.glVertex3f( offset+x2*stepX-lenX*2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);

        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX*1.5f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY+lenY/2.f, height*normalizeHeight + 4.1f);
        gl.glVertex3f( offset+x2*stepX-lenX/2.f, offset+y2*stepY-lenY/2.f, height*normalizeHeight + 4.1f);
        */

        
    }

    public void reinitialize()  {
        return;
    }

}