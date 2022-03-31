package applications.simpleworld;

import applications.simpleworld.*;
import worlds.*;
import com.jogamp.opengl.*;

public class Weather {
    public enum Condition { SUNNY, SNOWING, RAINY };
    private World world;
    private float elapsed_time = 0;
    private long last_checked = 0;
    private static final float WEATHER_UPDATE_DELAY = 10f; //10 seconds
    private Condition weather = Condition.SUNNY; //sunny default
    private float time_speed = 0.01f;

    public Weather(World world) {
        this.world = world;
    }

    public void step() {
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

    public Condition getCondition() {
        return weather;
    }

    public void setCondition(Condition cond) {
        this.weather = cond;
    }

    public void drawSky(GL2 gl) {
        //Math.sin simulates the weather condition as the function is perodically repeated and corresponds to the schedule of day to night
        float time_value = (float) Math.max(0f, Math.sin(elapsed_time));
        //gl.glClearColor(0.25f, time_value/8f, time_value, 0.0f); //sunset -> day
       gl.glClear(gl.GL_COLOR_BUFFER_BIT);
       gl.glColor3f(1f, 0f, 0f);
       drawSphere(gl, 5, 10, 10);
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
    private void drawSphere(GL2 gl, double radius, int lats, int longs) {
        /*
         * This algorithm moves along the z-axis, for PI radians and then at
         * each point rotates around the z-axis drawing a QUAD_STRIP.
         *
         * If you you start i at a non-zero value then you will see an open end
         * along the z-axis. If you start j at a none zero-axis then you will
         * see segements taken out.
         */

        for (int i = 0; i <= lats; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / lats);
            float zoff = 0; //world.getLandscape().getZOffset()
            double z0 = Math.sin(lat0) * radius + zoff;
            double zr0 = Math.cos(lat0) * radius;

            double lat1 = Math.PI * (-0.5 + (double) i / lats);
            double z1 = Math.sin(lat1) * radius + zoff;
            double zr1 = Math.cos(lat1) * radius;

            gl.glBegin(gl.GL_QUAD_STRIP);
            for (int j = 0; j <= longs; j++) {
                float pc1 = j / longs;
                float pc2 = j + 1 / longs;

                double lng = 2 * Math.PI * (double) (j - 1) / longs;
                double x = Math.cos(lng) * radius;
                double y = Math.sin(lng) * radius;

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
