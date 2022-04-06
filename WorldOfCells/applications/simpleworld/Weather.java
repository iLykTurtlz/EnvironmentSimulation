package applications.simpleworld;

import applications.simpleworld.*;
import worlds.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.gl2.GLUT;

public class Weather {
    public enum Condition { SUNNY, SNOWING, RAINY };
    public enum Time { DAY, NIGHT };
    private WorldOfTrees world;
    private float elapsed_time = 0;
    private long last_checked = 0;
    private static final float WEATHER_UPDATE_DELAY = 10f; //10 seconds
    private Time time;
    private Condition weather = Condition.SUNNY; //sunny default
    private static float time_speed = 0.001f; //0.001 is good
    private GLUT glut;

    public Weather(WorldOfTrees world) {
        this.world = world;
        time = Time.DAY;
        glut = new GLUT();
    }

    public void step() {
        //updateWeather();
        float time_value = getTimeValue();
        if (time_value >= .5f) {
            if (time != Time.DAY)
                setTime(Time.DAY);
        } else {
            if (time != Time.NIGHT)
                setTime(Time.NIGHT);
        }
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

    public void setCondition(Condition cond) {
        this.weather = cond;
    }

    public void setTime(Time time) {
        this.time = time;
        //float time_value = getTimeValue();
        //if (
    }

    public void setTimeSpeed(float speed) {
        this.time_speed = Math.max(0f, speed);
    }

    //getTimeValue() is used locally to define time as a value with cosinus
    private float getTimeValue() {
        return (float) Math.max(0f, Math.cos(elapsed_time));
    }

    public static float getTimeSpeed() {
        return time_speed;
    }

    public Time getTime() {
        return time;
    }

    public float getElapsedTime() {
        return elapsed_time;
    }

    public Condition getCondition() {
        return weather;
    }

    public void drawSky(GL2 gl) { //draw(GL2 gl)
        //Math.sin simulates the weather condition as the function is perodically repeated and corresponds to the schedule of day to night
        //Math.sin simulates the weather conditions as the function is perodically repeated and corresponds to the schedule of day to night
        float time = getElapsedTime();
        float time_value = getTimeValue();
        gl.glClearColor(0.3f, time_value/2f, time_value, 0.5f); //sunset -> day
        //gl.glLoadIdentity();
        gl.glPushMatrix();

        //Drawing the sun

        float x = (float) Math.sin(time) * world.getLandscape().landscape.length * 2f;
        float y = (float) Math.sin(time) * world.getLandscape().landscape.length / 3f; //move the sun diagonally
        float z = 70f + (float) Math.cos(time) * world.getLandscape().landscape[0].length; //70f base height
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
        double[][] landscape = land;
        //1 - look on the map a place where the volcano can spawn
        x = (int) (Math.random() * (landscape.length - 2*range + 1) + range);
        y = (int) (Math.random() * (landscape[0].length - 2*range + 1) + range);
        System.out.println("Red point");
        //tests
        world.getLandscape().x = x;
        world.getLandscape().y = y;
        float color[] = {1f, 0f, 0f};
        //world.setCellState(x, y, color); draw Volcano
        //make it grow or instantly spawn?
        //2 - increase altitude based on the center thanks to sinus or cosinus or any increasing function (landscape[x][y] *= function(x/x,y)
        //we use sinus function since it is an increasing function between 0 and pi/2
        System.out.println("Increasing the borders");
        float i = 0;
        float max_height = 0.5f;
        int step = 1;
        while (step < range) {
            for (int xi = x - step; xi < x + step; xi++) {
                for (int yi = y - step; yi < y + step; yi++) {
                    int xm = (xi + landscape.length) % landscape.length;
                    int ym = (yi + landscape[0].length) % landscape[0].length;
                    if (landscape[xm][ym] >= WorldOfTrees.WATER_LEVEL) {
                        landscape[xm][ym] = Math.min(max_height, landscape[xm][ym] * (1f + (1 - Math.sin(i))));
                    }
                    i += 1/(((float)step)*2f);
                }
                i = 0;
            }
            step += 1;
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

    public void drawVolcano(GL2 gl) {
        double[][] landscape = world.getMap();
        float color[] = {0.2f, 0.2f, 0.2f};
        for (int xi = x - range - 4; xi < x + range + 4; xi++) {
            for (int yi = y - range - 4; yi < y + range + 4; yi++) {
                int xm = (xi + landscape.length-1) % (landscape.length - 1);
                int ym = (yi + landscape[0].length-1) % (landscape[0].length - 1);
                //world.setCellState(xm, ym, color);
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
