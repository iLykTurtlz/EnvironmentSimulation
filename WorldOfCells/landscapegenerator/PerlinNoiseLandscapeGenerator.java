// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package landscapegenerator;

public class PerlinNoiseLandscapeGenerator {

    public static double[][] generatePerlinNoiseLandscape ( int dxView, int dyView, double scaling, double landscapeAltitudeRatio, int perlinLayerCount )
    {
    	double landscape[][] = new double[dxView][dyView];

    	// A ECRIRE ! 
    	// ...
    	for ( int x = 0 ; x < dxView ; x++ )
    		for ( int y = 0 ; y < dyView ; y++ )
    			landscape[x][y] = Math.random();
    	
    	// ...
    	// cf. http://freespace.virgin.net/hugo.elias/models/m_perlin.htm pour une explication


    	// scaling and polishing
    	landscape = LandscapeToolbox.scaleAndCenter(landscape, scaling, landscapeAltitudeRatio);
    	landscape = LandscapeToolbox.smoothLandscape(landscape);
    	
		return landscape;
    }
}
