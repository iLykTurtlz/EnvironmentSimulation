// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package landscapegenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoadFromFileLandscape {

	public static double[][] load ( String __filename, double __scaling, double __landscapeAltitudeRatio )
    {
		double landscape[][] = null;
				
    	try {
    		BufferedImage bi = ImageIO.read(new File(__filename));
    		
    		landscape = new double[bi.getWidth()][bi.getHeight()];
    		
    		for ( int x = 0 ; x != bi.getWidth() ; x++ )
    			for ( int y = 0 ; y != bi.getHeight() ; y++ )
    			{
    				int rawvalue = bi.getRGB(x, y);
    				int [] rgb = new int[3];
    				rgb[0] = ( rawvalue & 0x00FF0000 ) / (int)Math.pow(256,2); // red
    				rgb[1] = ( rawvalue & 0x0000FF00 ) / 256; // green
    				rgb[2] = ( rawvalue & 0x000000FF ); // blue
    				landscape[x][bi.getHeight()-1-y] = ((double)rgb[0])/255.0; // use only red as value.
    			}
    	}
    	catch ( IOException e )
    	{
    		System.err.println("[error] image \""+__filename+"\" could not be loaded.");
    		System.exit(-1);
    	}
    	
    	landscape = LandscapeToolbox.scaleAndCenter(landscape, __scaling, __landscapeAltitudeRatio);
    	
    	landscape = LandscapeToolbox.smoothLandscape(landscape);
    	
    	return landscape;
    }

}
