package applications.simpleworld;
import worlds.World;

public class Mushroom extends Plant {
    private float[] stemColor;
    private float centerRadius;
    private float centerHeight;

    public Mushroom(int __x , int __y, World __world)   {
        super(__x,__y,__world);
        this.stemColor = new float[] {1.f,0.f,1.f};
        this.centerRadius = 0.05f;
        this.centerHeight = 1.0f;    
        this.max_size = 11;
    }

    public void step()  {
        super.step();
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


    public void grow(int nbBands, float heightDecrement, float h, float bandWidth, float[] bandColor, float radius, int x2, int y2, float height, float altitude, GL2 gl,int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)  {
        /* This method recursively adds colored bands to the mushroom, incrementing its radius */
        int[] sequence = new int[4];
        
        for (int i=0; i<8; i++) {       //this 8-term sequence of quadruplets defines the 4 vertices needed to draw each of the 8 triangular components of a concentric box.
            sequence[0] = i;
            sequence[1] = (i+4)%8;
            if (i<4)
                sequence[2] = (i+1)%4;
            else
                sequence[2] = (i-1)%4 + 4;
            sequence[3] = i;
            
            gl.glColor3f(bandColor[0],bandColor[1],bandColor[2]);
            for (int j=0; j<sequence.length; j++)   {
                switch (sequence[j])   {        //8 vertices used to draw the concentric boxes.  0-3 : inner square, 4-7 : outer square
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
                        gl.glVertex3f( offset+x2*stepX-lenX*(radius+bandWidth), offset+y2*stepY-lenY*(radius+bandWidth), height*normalizeHeight + h - heightDecrement);
                        break;
                    case 5:
                        gl.glVertex3f( offset+x2*stepX-lenX*(radius+bandWidth), offset+y2*stepY+lenY*(radius+bandWidth), height*normalizeHeight + h - heightDecrement);
                        break;
                    case 6:
                        gl.glVertex3f( offset+x2*stepX+lenX*(radius+bandWidth), offset+y2*stepY+lenY*(radius+bandWidth), height*normalizeHeight + h - heightDecrement);
                        break;
                    case 7:
                        gl.glVertex3f( offset+x2*stepX+lenX*(radius+bandWidth), offset+y2*stepY-lenY*(radius+bandWidth), height*normalizeHeight + h - heightDecrement);
                        break;
                    default:
                        System.out.println("Erreur : creation d'une fleur");
                }

            }
        }

        //recursive call limited by size which represents the plant's growth state.
        if (nbBands < size)   
            grow(++nbBands, heightDecrement + 0.005f,h - heightDecrement, 0.1f, incrementColor(bandColor), radius+bandWidth, x2, y2, height, altitude, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight); 
        
        
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
        gl.glVertex3f( offset+x2*stepX-lenY/16.f, offset+y2*stepY+lenY/2.f, altitude + centerHeight );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX+lenY/16.f, offset+y2*stepY-lenY/2.f, altitude + centerHeight );
        
    	gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX-lenY/2.f, offset+y2*stepY+lenY/16.f, altitude + centerHeight );
        gl.glVertex3f( offset+x2*stepX, offset+y2*stepY, altitude );
        gl.glVertex3f( offset+x2*stepX+lenY/2.f, offset+y2*stepY-lenY/16.f, altitude + centerHeight );
        

        //floral disc
        gl.glColor3f(1.f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX-lenX*centerRadius, offset+y2*stepY-lenY*centerRadius, altitude + centerHeight);
        gl.glVertex3f( offset+x2*stepX-lenX*centerRadius, offset+y2*stepY+lenY*centerRadius, altitude + centerHeight);
        gl.glVertex3f( offset+x2*stepX+lenX*centerRadius, offset+y2*stepY+lenY*centerRadius, altitude + centerHeight);
        gl.glVertex3f( offset+x2*stepX+lenX*centerRadius, offset+y2*stepY-lenY*centerRadius, altitude + centerHeight);

        grow(0, 0, centerHeight, 0.1f, new float[]{1.f,0.5f,0.f}, 0.1f, x2, y2, height, altitude, gl, offsetCA_x, offsetCA_y, offset, stepX, stepY, lenX, lenY, normalizeHeight);
       

        

        
    }
}