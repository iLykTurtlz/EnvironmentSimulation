// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package cellularautomata;

public class CellularAutomataDouble extends CellularAutomata {

	protected double Buffer0[][];
	protected double Buffer1[][];
	
	public CellularAutomataDouble ( int __dx , int __dy, boolean __buffering )
	{
		super(__dx,__dy,__buffering);

		Buffer0 = new double[_dx][_dy];
		Buffer1 = new double[_dx][_dy];
		
	    for ( int x = 0 ; x != _dx ; x++ )
	    	for ( int y = 0 ; y != _dy ; y++ )
	    	{
    			Buffer0[x][y]=0;
    			Buffer1[x][y]=0;
	    	}
	}
	
	public double getCellState ( int __x, int __y )
	{
		checkBounds (__x,__y);
		
		double value;

		if ( buffering == false )
		{
			value = Buffer0[__x][__y];
		}
		else
		{
			if ( activeIndex == 1 ) // read old buffer
			{
				value = Buffer0[__x][__y];
			}
			else
			{
				value = Buffer1[__x][__y];
			}
		}
		
		return value;
	}
	
	public void setCellState ( int __x, int __y, double __value )
	{
		checkBounds (__x,__y);
		
		if ( buffering == false )
		{
			Buffer0[__x][__y] = __value;
		}
		else
		{
			if ( activeIndex == 0 ) // write new buffer
			{
				Buffer0[__x][__y] = __value;
			}
			else
			{
				Buffer1[__x][__y] = __value;
			}
		}
	}
	
	public double[][] getCurrentBuffer()
	{
		if ( activeIndex == 0 || buffering == false ) 
			return Buffer0;
		else
			return Buffer1;		
	}
	
}
