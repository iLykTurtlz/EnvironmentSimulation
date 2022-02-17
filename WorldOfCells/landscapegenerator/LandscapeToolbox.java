package landscapegenerator;

public class LandscapeToolbox {

	public static double[][] scaleAndCenter(double[][] landscape, double __scaling, double landscapeAltitudeRatio) 
	{
		double[][] myLandscape = landscape.clone();
		
    	// * translate values to center around 0. re-scale, balance water/mountain.
    	
    	double minValue = landscape[0][0];
    	double maxValue = landscape[0][0];

    	for ( int x = 0 ; x != landscape.length ; x++ )
			for ( int y = 0 ; y != landscape[0].length ; y++ )
			{
				if ( landscape[x][y] < minValue )
					minValue = landscape[x][y];
				else
					if ( landscape[x][y] > maxValue )
						maxValue = landscape[x][y];
			}
    	
    	double normalizeFactor = 1.0/(maxValue-minValue);
    	
    	for ( int x = 0 ; x != landscape.length ; x++ )
			for ( int y = 0 ; y != landscape[0].length ; y++ )
			{
				landscape[x][y] = landscape[x][y] - minValue;
				landscape[x][y] *= normalizeFactor; // [0;1]
				landscape[x][y] = landscape[x][y] - landscapeAltitudeRatio;
				landscape[x][y] *= __scaling;
			}
    	
    	return myLandscape;
	}


	public static double[][] smoothLandscape ( double[][] landscape )
	{
		int dxView = landscape.length;
		int dyView = landscape[0].length;
		
    	// smoothing coasts (coast tiles will have a zero)
		for ( int x = 0 ; x != dxView ; x++ )
			for ( int y = 0 ; y != dyView ; y++ )
			{
				if ( landscape[x][y] < 0 )
				{
					if ( // one neighbor above ground is enough.
							landscape[(x-1+dxView)%dxView][(y-1+dyView)%dyView]>0 || landscape[x][(y-1+dyView)%dyView]>0 || landscape[(x+1)%dxView][(y-1+dyView)%dyView]>0 ||
							landscape[(x-1+dxView)%dxView][y]>0                   										 || landscape[(x+1)%dxView][y]>0      ||
							landscape[(x-1+dxView)%dxView][(y+1+dyView)%dyView]>0 || landscape[x][(y+1+dyView)%dyView]>0 || landscape[(x+1)%dxView][(y+1+dyView)%dyView]>0 ) 
						landscape[x][y] = 0.0;
				}
			}
		
		return landscape;
	}
	
}
