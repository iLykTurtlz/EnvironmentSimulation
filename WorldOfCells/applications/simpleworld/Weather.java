package applications.simpleworld;

import applications.simpleworld.*;
import worlds.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.gl2.GLUT;

public class Weather {
    public enum Condition { SUNNY, SNOWING, RAINY };
    private WorldOfTrees world;
    private float elapsed_time = 0;
    private long last_checked = 0;
    private static final float WEATHER_UPDATE_DELAY = 10f; //10 seconds
    private Condition weather = Condition.SUNNY; //sunny default
    private static float time_speed = 0.001f; //0.001 is good
    private GLUT glut;

    public Weather(WorldOfTrees world) {
        this.world = world;
        glut = new GLUT();
    }

    public void step() {
        //updateWeather();
        elapsed_time += time_speed;
    }

    public void updateWeather() {
        //calculus...
        //check every 10 seconds to update the weather
        long current_time = System.currentTimeMillis();
        if ((last_checked - current_time)/1000f >= WEATHER_UPDATE_DELAY) {
            int random = (int) (Math.random() * Condition.values().length); //between 2 and 0 by default
            setCondition(Condition.values()[random]);
            System.out.println("[DEBUG] Weather updated to " + getCondition().toString());
        }
    }

    public void setTimeSpeed(float speed) {
        this.time_speed = Math.max(0f, speed);
    }

    public static float getTimeSpeed() {
        return time_speed;
    }

    public float getTime() {
        return elapsed_time;
    }

    public Condition getCondition() {
        return weather;
    }

    public void setCondition(Condition cond) {
        this.weather = cond;
    }

    public void drawSky(GL2 gl) { //draw(GL2 gl)
        //Math.sin simulates the weather condition as the function is perodically repeated and corresponds to the schedule of day to night
        //Math.sin simulates the weather conditions as the function is perodically repeated and corresponds to the schedule of day to night
        float speed = getTime();
        float time_value = (float) Math.max(0f, Math.cos(speed));
        gl.glClearColor(0.3f, time_value/2f, time_value, 0.5f); //sunset -> day
        //gl.glLoadIdentity();
        gl.glPushMatrix();

        //Drawing the sun

        float x = (float) Math.sin(speed) * world.getLandscape().landscape.length * 2f;
        float y = (float) Math.sin(speed) * world.getLandscape().landscape.length / 3f; //move the sun diagonally
        float z = 70f + (float) Math.cos(speed) * world.getLandscape().landscape[0].length; //70f base height
        gl.glTranslatef(x, y, z + world.getLandscape().getZOffset());
        gl.glColor3f(1f, 0.8f, 0f);
        glut.glutSolidSphere(10, 20, 20);

        gl.glPopMatrix();
      /* drawSphere(gl, 5, 5, 20, 20);
       gl.glPopMatrix();*/
    }

    private boolean init = true;
    private int x = 0, y = 0;
    private int range = 25;
    private double[][] volcano_cells;

    public double[][] initVolcano(double land[][]) {


        //1 - look on the map a place where the volcano can spawn
        double max = 0;
        double landscape[][] = land;
        for (int i = 0; i < landscape.length; i++) {
            for (int k = 0; k < landscape[i].length; k++) {
                if (landscape[i][k] > max) {
                    max = landscape[i][k];
                    x = i;
                    y = k;
                }
            }
        }
        System.out.println("Found max height at " + x + ", " + y + " : " + max);
        System.out.println("Red point");
        world.getLandscape().x = x;
        world.getLandscape().y = y;
        float color[] = {1f, 0f, 0f};
        //world.setCellState(x, y, color); draw Volcano
        //make it grow or instantly spawn?
        //2 - increase altitude based on the center thanks to sinus or cosinus or any growiwng function (landscape[x][y] *= function(x/x,y)
        //we use sinus function since it is a growing function between 0 and pi/2
        System.out.println("Increasing the borders");
        float i = 0;
        float max_height = 0.55f;
        for (int xi = x - range; xi < x + range; xi++) {
            for (int yi = y - range; yi < y + range; yi++) {
                int xm = (xi + landscape.length) % landscape.length;
                int ym = (yi + landscape[0].length) % landscape[0].length;
                landscape[xm][ym] = Math.min(max_height, landscape[xm][ym] * (1f + (1 - Math.sin(i))));
                if (landscape[xm][ym] >= max_height)
                    landscape[xm][ym] *= 0.8;
                i += 1/(((float)range)*2f);
            }
            i = 0;
        }
        //will keep the previous perlin noise values but only increase it => random volcano)
        //3 - add lava inside of it (the generation of the volcano will make a hollow inside of it to pop lava
        //change color of cells and decrease height from center
        /*for (int xi = x - 4; xi < x + 4; xi++) {
            for (int yi = y - 4; yi < y + 4; yi++) {
                int xm = (xi + landscape.length) % landscape.length;
                int ym = (yi + landscape[0].length) % landscape[0].length;
                landscape[xm][ym] *= 0.5;
            }
        }*/

        return landscape;
    }

    public boolean onVolcano(int xi, int yi) {
        double landscape[][] = world.getMap();
        return xi <= (x + range) % landscape.length && xi >= ((x - range) + landscape.length) % landscape.length && yi >= ((y - range) + landscape[0].length) % landscape[0].length && yi <= (yi + range) % landscape.length;
    }

    public void drawVolcano(GL2 gl) {
        double[][] landscape = world.getMap();
        float color[] = {1f, 0f, 0f};
        for (int xi = x - 4; xi < x + 4; xi++) {
            for (int yi = y - 4; yi < y + 4; yi++) {
                int xm = (xi + landscape.length-1) % (landscape.length - 1);
                int ym = (yi + landscape[0].length-1) % (landscape[0].length - 1);
                world.setCellState(xm, ym, color);
            }
        }
    }

    /**
     * Method to draw a sphere in OpenGL.
     *
     * Source taken from: http://ozark.hendrix.edu/~burch/cs/490/sched/feb8/
     *
     * @param gl
     * @param radius
     * @param lats
     *            number of sub-divisions along the latitude
     * @param longs
     *            number of sub-divisions along the longitude
     */
    private void drawSphere(GL2 gl, double radiusH, double radiusV, float lats, float longs) {
        float zoff = world.getLandscape().getZOffset() + 50;
       for (int i = 0; i <= lats; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / lats);
            double z0 = Math.sin(lat0) * radiusH + zoff;
            double zr0 = Math.cos(lat0) * radiusV;

            double lat1 = Math.PI * (-0.5 + (double) i / lats);
            double z1 = Math.sin(lat1) * radiusH + zoff;
            double zr1 = Math.cos(lat1) * radiusV;

            gl.glBegin(gl.GL_QUAD_STRIP);
            for (int j = 0; j <= longs; j++) {
                double lng = 2 * Math.PI * (double) (j - 1) / longs;
                double x = Math.cos(lng) * radiusH;
                double y = Math.sin(lng) * radiusV;

                gl.glNormal3d(x * zr0, y * zr0, z0);
                gl.glVertex3d(x * zr0, y * zr0, z0);
                gl.glNormal3d(x * zr1, y * zr1, z1);
                gl.glVertex3d(x * zr1, y * zr1, z1);
            }
            gl.glEnd();
        }
    }

    //do we draw the clouds here ?
    //public void draw(GL2 gl);
}
