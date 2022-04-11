package objects;

import com.jogamp.opengl.GL2;

import worlds.World;

public class Cloud extends UniqueObject{

    private float len, size; //width, height
    private float x_inc = 0f;
    private float altitude = 70f;
    private float color;
    private float speed;

    //define attributes of the object
	public Cloud ( float __x , float __y , World __world )
	{
		super(__x,__y,__world);
		len = 20f + (float) (Math.random() * 100);
		size = 1f + ((float) Math.random()) % .35f + 0.05f;
		altitude += (float) Math.random() * 200f;
		color = (float) Math.random() * 0.35f + 0.25f;
		speed = __world.getLandscape().getWeather().getTimeSpeed()*((float) (Math.random() * 100f + 25f));
	}

    /*
     * The cloud object is quite simple, it is only a big square with some transparency that is moving according to elapsed time along the x axis.
     * The cloud is drawn using a gradient is its color depends on the time of the day, the night it becomes more yellow and the day it becomes more white.
     * The size of the cloud and also the length are generated randomly through the constructor.
     */
    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)
    {
		speed = myWorld.getLandscape().getWeather().getTimeSpeed()*((float) (Math.random() * 100f + 25f)); //update speed according to time speed
        x = (x + speed) % (myWorld.getWidth());

    	float x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	float y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

    	float zoff = myWorld.getLandscape().getZOffset() + altitude;

        float t = (float) Math.cos(myWorld.getLandscape().getWeather().getElapsedTime());
        gl.glColor4f(1f, 1f, 1f, color);

    	//lower face
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight + zoff);

        gl.glColor4f(1f, 1f, t, color);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoff);

        //gl.glColor3f(1f, 1f, 1f);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY-lenY, 0.1f*normalizeHeight*size + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight*size + zoff);

        //gl.glColor3f(1f,0.f,0.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight*size + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight*size + zoff);

        //gl.glColor3f(0f,1f,0.f);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight*size + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight*size + zoff);

        //gl.glColor3f(0f,0.f,1f);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight*size + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY-lenY, 0.1f*normalizeHeight*size + zoff);

        //upper face
        //gl.glColor3f(0f,1f,1f);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY-lenY, 0.1f*normalizeHeight*size + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight*size + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight*size + zoff);
        gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, 0.1f*normalizeHeight*size + zoff);


    }
}
