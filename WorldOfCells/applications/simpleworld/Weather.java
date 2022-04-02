package applications.simpleworld;

import applications.simpleworld.*;
import worlds.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.gl2.GLUT;

public class Weather {
    public enum Condition { SUNNY, SNOWING, RAINY };
    private World world;
    private float elapsed_time = 0;
    private long last_checked = 0;
    private static final float WEATHER_UPDATE_DELAY = 10f; //10 seconds
    private Condition weather = Condition.SUNNY; //sunny default
    private static float time_speed = 0.001f; //0.001 is good
    private GLUT glut;

    public Weather(World world) {
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
        float x = (float) Math.sin(speed) * world.getLandscape().landscape.length;
        float y = (float) Math.sin(speed) * world.getLandscape().landscape.length / 3f; //move the sun diagonally
        float z = 50f + (float) Math.cos(speed) * world.getLandscape().landscape[0].length; //50f base height
        gl.glTranslatef(x, y, z + world.getLandscape().getZOffset());
        gl.glColor3f(1f, 0.8f, 0f);
        glut.glutSolidSphere(10, 20, 20);

        gl.glPopMatrix();
      /* drawSphere(gl, 5, 5, 20, 20);
       gl.glPopMatrix();*/
    }

    public void drawVolcano(GL2 gl) {
        //1 - look on the map a place where the volcano can spawn
        //make it grow or instantly spawn?
        //2 - increase altitude based on the center thanks to sinus or cosinus or any growiwng function (landscape[x][y] *= function(x/x,y)
        //will keep the previous perlin noise values but only increase it => random volcano)
        //3 - add lava inside of it (the generation of the volcano will make a hollow inside of it to pop lava
        //change color of cells and increase height
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
