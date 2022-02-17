// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package cellularautomata;

public abstract class CellularAutomata {

	protected int _dx;
	protected int _dy;

	boolean buffering;
	
	int activeIndex;
	
	public CellularAutomata(int __dx, int __dy, boolean __buffering ) 
	{
		_dx = __dx;
		_dy = __dy;

		buffering = __buffering;
		
		activeIndex = 0;
	}

	public void checkBounds( int __x , int __y )
	{
		if ( __x < 0 || __x > _dx || __y < 0 || __y > _dy )
		{
			System.err.println("[error] out of bounds ("+__x+","+__y+")");
			System.exit(-1);
		}
	}
	
	public int getWidth()
	{
		return _dx;
	}
	
	public int getHeight()
	{
		return _dy;
	}

	public void init()
	{
		// ...
	}
	
	public void step() 
	{ 
		if ( buffering )
			swapBuffer();
	}
	
	public void swapBuffer() // should be used carefully (except for initial step)
	{
		activeIndex = ( activeIndex+1 ) % 2;
	}
	
}
