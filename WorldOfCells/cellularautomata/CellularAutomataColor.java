// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package cellularautomata;

public class CellularAutomataColor extends CellularAutomata {

	protected float Buffer0[][][];
	protected float Buffer1[][][];
	
	
	public CellularAutomataColor ( int __dx , int __dy, boolean __buffering )
	{
		super(__dx,__dy,__buffering);

		Buffer0 = new float[_dx][_dy][3];
		Buffer1 = new float[_dx][_dy][3];
		
	    for ( int x = 0 ; x != _dx ; x++ )
	    	for ( int y = 0 ; y != _dy ; y++ )
	    	{
    			Buffer0[x][y][0]=255;
    			Buffer0[x][y][1]=255;
    			Buffer0[x][y][2]=255;
    			Buffer1[x][y][0]=255;
    			Buffer1[x][y][1]=255;
    			Buffer1[x][y][2]=255;
	    	}
	}
	
	public float[] getCellState ( int __x, int __y )
	{
		checkBounds (__x,__y);
		
		float color[] = new float[3];

		if ( buffering == false )
		{
			color[0] = Buffer0[__x][__y][0];
			color[1] = Buffer0[__x][__y][1];
			color[2] = Buffer0[__x][__y][2];
		}
		else
		{
			if ( activeIndex == 1 ) // read old buffer
			{
				color[0] = Buffer0[__x][__y][0];
				color[1] = Buffer0[__x][__y][1];
				color[2] = Buffer0[__x][__y][2];
			}
			else
			{
				color[0] = Buffer1[__x][__y][0];
				color[1] = Buffer1[__x][__y][1];
				color[2] = Buffer1[__x][__y][2];
			}
		}
		
		return color;
	}
	
	public void setCellState ( int __x, int __y, float __r, float __g, float __b )
	{
		checkBounds (__x,__y);
		
		if ( __r > 1.0f || __g > 1.0f || __b > 1.0f )
		{
			System.err.println("[WARNING] CellularAutomataColor - value must be in [0.0,1.0[ ( was: " + __r + "," + __g + "," + __b + " ) -- THRESHOLDING.");
			if ( __r > 1.0f ) __r = 1.0f;
			if ( __g > 1.0f ) __g = 1.0f;
			if ( __b > 1.0f ) __b = 1.0f;
		}
		
		if ( buffering == false )
		{
			Buffer0[__x][__y][0] = __r;
			Buffer0[__x][__y][1] = __g;
			Buffer0[__x][__y][2] = __b;
		}
		else
		{
			if ( activeIndex == 0 ) // write new buffer
			{
				Buffer0[__x][__y][0] = __r;
				Buffer0[__x][__y][1] = __g;
				Buffer0[__x][__y][2] = __b;
			}
			else
			{
				Buffer1[__x][__y][0] = __r;
				Buffer1[__x][__y][1] = __g;
				Buffer1[__x][__y][2] = __b;
			}
		}
	}
	
	public void setCellState ( int __x, int __y, float __color[] )
	{
		checkBounds (__x,__y);
			
		if ( __color[0] > 1.0 || __color[1] > 1.0 || __color[2] > 1.0 )
		{
			System.err.println("[WARNING] CellularAutomataColor - value must be in [0.0,1.0[ ( was: " + __color[0] + "," + __color[1] + "," + __color[2] + " ) -- THRESHOLDING.");
			if ( __color[0] > 1.0f ) __color[0] = 1.0f;
			if ( __color[1] > 1.0f ) __color[1] = 1.0f;
			if ( __color[2] > 1.0f ) __color[2] = 1.0f;
		}
		
		if ( buffering == false )
		{
			Buffer0[__x][__y][0] = __color[0];
			Buffer0[__x][__y][1] = __color[1];
			Buffer0[__x][__y][2] = __color[2];
		}
		else
		{
			if ( activeIndex == 0 )
			{
				Buffer0[__x][__y][0] = __color[0];
				Buffer0[__x][__y][1] = __color[1];
				Buffer0[__x][__y][2] = __color[2];
			}
			else
			{
				Buffer1[__x][__y][0] = __color[0];
				Buffer1[__x][__y][1] = __color[1];
				Buffer1[__x][__y][2] = __color[2];
			}	
		}
	}
	
	public float[][][] getCurrentBuffer()
	{
		if ( activeIndex == 0 || buffering == false ) 
			return Buffer0;
		else
			return Buffer1;		
	}
	
}
