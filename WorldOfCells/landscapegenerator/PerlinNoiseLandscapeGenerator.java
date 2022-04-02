// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package landscapegenerator;

import java.lang.Math.*;

public class PerlinNoiseLandscapeGenerator {

    public static double[][] generatePerlinNoiseLandscape ( int dxView, int dyView, double scaling, double landscapeAltitudeRatio, int perlinLayerCount)
    {
    	double landscape[][] = new double[dxView][dyView];
        double random_generation = Math.random() * 20 + 1; //allows different generations since perlin noise is based on noise based on fixed primal numbers
    	for (int x = 0; x < dxView; x++) {
    		for (int y = 0; y < dyView; y++) {
                landscape[x][y] = perlinNoise2D((double) x / (double) dxView * random_generation, (double) y / (double) dyView * random_generation, 0.4d, perlinLayerCount); //low persistence for a smoother landscape (argument after random_generation (0.4d seems stable now)
            }
        }

    	// scaling and polishing
    	landscape = LandscapeToolbox.scaleAndCenter(landscape, scaling, landscapeAltitudeRatio);
    	landscape = LandscapeToolbox.smoothLandscape(landscape);
    	System.out.println("landscape:");

		return landscape;
    }

    /***
    * Retourne l'interpolation cosinusoidale d'une valeur x comprise entre a et b (un flottant)
    * L'interpolation cosinusoidale permet l'obtention d'une courbe plus lisse comparée à une courbe
    * obtenue par interpolation linéaire, de plus elle est moins consommante que l'interpolation cubique
    ***/
    private static double cosInterpolate(double a, double b, double x) {
        double ft = x * Math.PI;
        double f = (1 - Math.cos(ft)) / 2;
        return a + (b-a) * f;
    }

    /***
    * Retourne une valeur décimale aléatoire au dépend des valeurs x et y
    * c'est une fonction de N*N dans R donc pour des memes x et y la valeur générée sera la meme.
    * Cela permet la génération aléatoire des valeurs (amplitudes) des points de controle
    * de la fonction du bruit de perlin.
    ***/
    private static double noise(int x, int y) {
        int n = x + y * 257;
        n = (n << 13) ^ n;
        return (1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0); //retourne une valeur flottante aléatoire basée sur une formule avec des nombres premiers
    }

    /***
    * Reprend la meme définition plus haut du bruit mais retourne une valeur
    * moyenne selon les voisins moyennés également, permet d'avoir une courbe de fonction du bruit de perlin
    * plus uniforme pour la génération de terrain.
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
    * Retourne une valeur interpolée au dépend de x
    * permet le lissage de la courbe de la fonction du bruit de perlin.
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

    private static double perlinNoise2D(double x, double y, double persistence, int nb_octaves) {
      double total = 0d;
      int n = nb_octaves - 1;
      double totalAmplitude = 0;

      for (int i = 0; i <= n; i++) {
          double frequency = 1/Math.pow(2, i);
          double amplitude = Math.pow(persistence, i);

          total += interpolatedNoise(x * frequency, y * frequency) * amplitude;
      }

      return total;
    }
}
