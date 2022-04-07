package applications.simpleworld;

import java.util.*;
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


    private static double nextGaussian(double x) {
        return Math.exp(-4d*x*x);
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
        gl.glBegin(gl.GL_QUADS);
    }

    //do we draw the clouds here ?
    //public void draw(GL2 gl);
}
