// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package landscapegenerator;

import java.lang.Math.*;
import java.util.*;

public class PerlinNoiseLandscapeGenerator {
	private static int[] p;
	private static int[] permutation;
	//private static double seed; //test
	private static int default_size;

    public static double[][] generatePerlinNoiseLandscape ( int dxView, int dyView, double scaling, double landscapeAltitudeRatio, int perlinLayerCount)
    {

		/* SECOND PERLIN NOISE VERSION AS TEST
		seed = new Random().nextGaussian() * 255;
    //ANOTHER PERLIN
    // Initialize the permutation array.
		p = new int[512];
		permutation = new int[] { 151, 160, 137, 91, 90, 15, 131, 13, 201,
				95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99,
				37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26,
				197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88,
				237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74,
				165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111,
				229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40,
				244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76,
				132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159,
				86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
				124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207,
				206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170,
				213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155,
				167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113,
				224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242,
				193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235,
				249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184,
				84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236,
				205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66,
				215, 61, 156, 180 };
		default_size = 35;

		// Populate it
		for (int i = 0; i < 256; i++) {
			p[256 + i] = p[i] = permutation[i];
		}*/

    //END
    	double landscape[][] = new double[dxView][dyView];
        double random_generation = Math.random() * 20 + 1; //allows different generations since perlin noise is based on noise based on fixed primal numbers
        //
    	for (int x = 0; x < dxView; x++) {
    		for (int y = 0; y < dyView; y++) {
    		//perlinNoise2D(((double) x / (double) dxView * random_generation), ((double) y / (double) dyView * random_generation), 0.4d, 10);
    		//noise2D(((double) x / (double) dxView * random_generation), ((double) y / (double) dyView * random_generation))
                landscape[x][y] = perlinNoise2D(((double) x / (double) dxView * random_generation), ((double) y / (double) dyView * random_generation), 0.4d, 1); //low persistence for a smoother landscape (argument after random_generation (0.4d seems stable now)
            }
        }

    	// scaling and polishing
    	landscape = LandscapeToolbox.scaleAndCenter(landscape, scaling, landscapeAltitudeRatio);
    	landscape = LandscapeToolbox.smoothLandscape(landscape);

		return landscape;
    }

    /***
    * Returns the cosinus interpolation of the x value between a and b (decimal value).
    * The cosinus interpolation allows a smoother curve compared to the curve of a linear interpolation.
    * Plus it's less consuming than the cubic interpolation.
    ***/
    private static double cosInterpolate(double a, double b, double x) {
        double ft = x * Math.PI;
        double f = (1 - Math.cos(ft)) / 2;
        return a + (b-a) * f;
    }

    /***
    * Returns a random decimal value based on the x and y values.
    * This is a function from N*N to R so for the same x and y the given generated value will be the same..
    * It allows a random generation of the values (amplitudes) of the control points of the function of the perlin noise.
    ***/
    private static double noise(int x, int y) {
        int n = x + y * 257;
        n = (n << 13) ^ n;
        return (1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0); //retourne une valeur flottante aléatoire basée sur une formule avec des nombres premiers
    }

    /***
    * Returns a noise value based on x and y but smoothed according to its neighbors' values in an average sense.
    * It gives a more uniform function.
    ***/
    private static double smoothedNoise(int x, int y) {
        double corners = (noise(x-1, y-1) + noise(x+1, y-1) + noise(x-1, y+1) + noise(x+1, y+1) ) / 16;
        double sides = (noise(x-1, y) + noise(x+1, y) + noise(x, y-1) + noise(x, y+1) ) / 8;
        double center = noise(x, y) / 4;
        return corners + sides + center;
    }

    private static double sCurve(double t)
    {
        return t*t*(3-2*t);
    }

    /***
    * Returns an interpolated value based on x, allows the curve of the perlin noise function to be smoother.
    ***/
    private static double interpolatedNoise(double x, double y) {
        int int_x = (int) x;
        x = x - int_x;
        double xs = sCurve(x);
        int int_y = (int) y;
        y = y - int_y;
        double ys = sCurve(y);

        double v1 = smoothedNoise(int_x, int_y);
        double v2 = smoothedNoise(int_x + 1, int_y);
        double v3 = smoothedNoise(int_x, int_y + 1);
        double v4 = smoothedNoise(int_x + 1, int_y + 1);

        double i1 = cosInterpolate(v1, v2, xs);
        double i2 = cosInterpolate(v3, v4, xs);

        return cosInterpolate(i1, i2, ys);
    }

    /***
     * Returns the value of the perlin noise at the x and y coordinates according to the persistence and the number of octaves.
     *      persistence - the perlin noise is a sum of numerous functions at different frequencies and amplitudes
                          for each function the higher the persistence is the more the amplitude will be closer to the previous added function.
                          It basically tells if the added functions should have a persisting (constant way) value of the initial amplitude.
            nb_octaves - the number of functions to sum up.
     */
    private static double perlinNoise2D(double x, double y, double persistence, int nb_octaves) {
      double total = 0d;
      int n = nb_octaves - 1;

      for (int i = 0; i <= n; i++) {
          double frequency = Math.pow(2, i);
          double amplitude = Math.pow(persistence, i);

          total += interpolatedNoise(x * frequency, y * frequency) * amplitude;
      }

      return total;
    }










    /* ANOTHER PERLIN used as a TEST */

	public static double noise3D(double x, double y, double z) {
		double value = 0.0;
		double size = default_size;
		double initialSize = size;

		while (size >= 1) {
			value += smoothNoise((x / size), (y / size), (z / size)) * size;
			size /= 2.0;
		}

		return value / initialSize;
	}

	public static double noise2D(double x, double y) {
		/*double value = 0.0;
		double size = default_size;
		double initialSize = size;

		while (size >= 1) {
			value += smoothNoise((x / size), (y / size), (0f / size)) * size;
			size /= 2.0;
		}

		return value / initialSize;*/

        // for this to tile correctly
        double c=40, a=10; // torus parameters (controlling size)
        double xt = (c+a*Math.cos(2*Math.PI*y))*Math.cos(2*Math.PI*x);
        double yt = (c+a*Math.cos(2*Math.PI*y))*Math.sin(2*Math.PI*x);
        double zt = a*Math.sin(2*Math.PI*y);
        double val = noise3D( xt,yt,zt) ; // torus
        return val;
	}


	public static double smoothNoise(double x, double y, double z) {
		// Offset each coordinate by the seed value
		x += seed;
		y += seed;
		x += seed;

		int X = (int) Math.floor(x) & 255; // FIND UNIT CUBE THAT
		int Y = (int) Math.floor(y) & 255; // CONTAINS POINT.
		int Z = (int) Math.floor(z) & 255;

		x -= Math.floor(x); // FIND RELATIVE X,Y,Z
		y -= Math.floor(y); // OF POINT IN CUBE.
		z -= Math.floor(z);

		double u = fade(x); // COMPUTE FADE CURVES
		double v = fade(y); // FOR EACH OF X,Y,Z.
		double w = fade(z);

		int A = p[X] + Y;
		int AA = p[A] + Z;
		int AB = p[A + 1] + Z; // HASH COORDINATES OF
		int B = p[X + 1] + Y;
		int BA = p[B] + Z;
		int BB = p[B + 1] + Z; // THE 8 CUBE CORNERS,

		return lerp(w, lerp(v, lerp(u, grad(p[AA], 		x, 		y, 		z		), 	// AND ADD
										grad(p[BA],		x - 1, 	y, 		z		)), // BLENDED
								lerp(u, grad(p[AB], 	x, 		y - 1, 	z		), 	// RESULTS
										grad(p[BB], 	x - 1, 	y - 1, 	z		))),// FROM 8
						lerp(v, lerp(u, grad(p[AA + 1], x, 		y, 		z - 1	), 	// CORNERS
										grad(p[BA + 1], x - 1, 	y, 		z - 1	)), // OF CUBE
								lerp(u, grad(p[AB + 1], x, 		y - 1,	z - 1	),
										grad(p[BB + 1], x - 1, 	y - 1, 	z - 1	))));
	}

	private static double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private static double lerp(double t, double a, double b) {
		return cosInterpolate(a, b, t);
	}

	private static double grad(int hash, double x, double y, double z) {
		int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
		double u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
		v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}








}
