// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package landscapegenerator;

import java.lang.Math.*;

public class PerlinNoiseLandscapeGenerator {

    public static double[][] generatePerlinNoiseLandscape ( int dxView, int dyView, double scaling, double landscapeAltitudeRatio, int perlinLayerCount)
    {
    	double landscape[][] = new double[dxView][dyView];

    	for (int x = 0; x < dxView; x++) {
    		for (int y = 0; y < dyView; y++) {
    			landscape[x][y] = perlinNoise2D(x, y, 1f, 8);
            }
        }

    	// scaling and polishing
    	landscape = LandscapeToolbox.scaleAndCenter(landscape, scaling, landscapeAltitudeRatio);
    	landscape = LandscapeToolbox.smoothLandscape(landscape);
    	
		return landscape;
    }

    /***
    * Retourne l'interpolation cosinusoidale d'une valeur x comprise entre a et b (un flottant)
    * L'interpolation cosinusoidale permet l'obtention d'une courbe plus lisse comparée à une courbe
    * obtenue par interpolation linéaire, de plus elle est moins consommante que l'interpolation cubique
    ***/
    private static float cosInterpolate(float a, float b, float x) {
        float ft = (float) (x * Math.PI);
        float f = (float) ((1 - Math.cos(ft)) * 0.5);
        return a*(1-f) + b*f;
    }

    /***
    * Retourne une valeur flottante aléatoire au dépend des valeurs x et y
    * c'est une fonction de N*N dans R donc pour des memes x et y la valeur générée sera la meme.
    * Cela permet la génération aléatoire des valeurs (amplitudes) des points de controle
    * de la fonction du bruit de perlin.
    ***/
    private static float noise(int x, int y) {
        int n = x + y * 57;
        n = (int) Math.pow(n<<13, n);
        return (1f - ((n * (n * n * 15731 + 789221) + 1376312589) & Integer.MAX_VALUE) / 1073741824f); //retourne une valeur flottante aléatoire basée sur une formule avec des nombres premiers
    }

    /***
    * Reprend la meme définition plus haut du bruit mais retourne une valeur
    * moyenne selon les voisins moyennés également, permet d'avoir une courbe de fonction du bruit de perlin
    * plus uniforme pour la génération de terrain.
    ***/
    private static float smoothedNoise(int x, int y) {
        float corners = (noise(x-1, y-1) + noise(x+1, y-1) + noise(x-1, y+1) + noise(x+1, y+1) ) / 16;
        float sides = (noise(x-1, y) + noise(x+1, y) + noise(x, y-1) + noise(x, y+1) ) / 8;
        float center = noise(x, y) / 4;
        return corners + sides + center;
    }

    /***
    * Retourne une valeur interpolée au dépend de x
    * permet le lissage de la courbe de la fonction du bruit de perlin.
    ***/
    private static float interpolatedNoise(float x, float y) {
        int int_x = (int) x;
        x = x - int_x;
        int int_y = (int) y;
        y = y - int_y;

        float v1 = smoothedNoise(int_x, int_y);
        float v2 = smoothedNoise(int_x + 1, int_y);
        float v3 = smoothedNoise(int_x, int_y + 1);
        float v4 = smoothedNoise(int_x + 1, int_y + 1);

        float i1 = cosInterpolate(v1, v2, x);
        float i2 = cosInterpolate(v3, v4, x);

        return cosInterpolate(i1 , i2 , y);
    }

    private static float perlinNoise2D(float x, float y, float persistence, int nb_octaves) {
      float total = 0f;
      int n = nb_octaves - 1;

      for (int i = 0; i <= n; i++) {
          float frequency = (float) Math.pow(2, i);
          float amplitude = (float) Math.pow(persistence, i);

          total += interpolatedNoise(x * frequency, y * frequency) * amplitude;
      }

      return total;
    }
}
