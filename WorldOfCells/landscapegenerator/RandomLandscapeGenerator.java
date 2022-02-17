// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package landscapegenerator;

public class RandomLandscapeGenerator {

    /**
     * 
     */
    public static double[][] generateRandomLandscape ( int dxView, int dyView )
    {
    	double landscape[][] = new double[dxView][dyView];
    	
		for ( int i = 0 ; i != dxView ; i++ )
			for ( int j = 0 ; j != dxView ; j++ )
			{
				landscape[i][j] = Math.random();
			}
		
		landscape = LandscapeToolbox.smoothLandscape(landscape);
		
		return landscape;
    }
    
}
