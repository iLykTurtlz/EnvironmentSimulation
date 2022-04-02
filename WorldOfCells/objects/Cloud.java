// ### WORLD OF CELLS ###
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package objects;

import com.jogamp.opengl.GL2;

import worlds.World;

public class Cloud extends UniqueObject{

    private float len, size; //width, height

	public Cloud ( int __x , int __y , World __world )
	{
		super(__x,__y,__world);
		len = 50f + (float) (Math.random() * 100);
		size = 1f + ((float) Math.random()) % .5f + 0.1f;
	}

    public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight)
    {

    	int x2 = (x-(offsetCA_x%myWorld.getWidth()));
    	if ( x2 < 0) x2+=myWorld.getWidth();
    	int y2 = (y-(offsetCA_y%myWorld.getHeight()));
    	if ( y2 < 0) y2+=myWorld.getHeight();

    	float zoff = myWorld.getLandscape().getZOffset() + 40;

        gl.glColor4f(1f, 1f, 1f, 0.5f);

    	//lower face
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY-lenY, 0.1f*normalizeHeight + zoff);
        gl.glVertex3f( offset+x2*stepX-lenX-len, offset+y2*stepY+lenY+len, 0.1f*normalizeHeight + zoff);
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
