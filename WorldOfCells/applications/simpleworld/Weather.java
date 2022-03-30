import com.jogamp.opengl.GL;

public class Weather {
    public enum Time { SUNNY, SNOWING, RAINY };
    private float elapsed_time = 0;
    private long last_checked = 0;
    private static final float WEATHER_UPDATE_DELAY = 10f; //10 seconds
    private Time weather = Time.SUNNY; //sunny default
    private float time_speed = 0.001f;

    public void step() {
        elapsed_time += time_speed;
    }

    public void updateWeather() {
        //calculus...
        //check every 10 seconds to update the weather
        long current_time = System.getCurrentMillis();
        if ((last_checked - current_time)/1000f >= WEATHER_UPDATE_DELAY) {
            int random = Math.random() * Time.values().length; //between 2 and 0 by default
            setTime(Time.values()[random]);
            System.out.println("[DEBUG] Weather updated to " + getTime().toString());
        }
    }

    public Time getTime() {
        return weather;
    }

    public void setTime(Time time) {
        this.weather = time;
    }

    public void drawSky(GL2 gl) {
        //Math.sin simulates the time as the function is perodically repeated and corresponds to the schedule of day to night
        float time_value = (float) Math.max(0f, Math.sin(elapsed_time));
        gl.glClearColor(0.25f, time_value/8f, time_value, 0.0f); //sunset -> day
    }

    //do we draw the clouds here ?
    //public void draw(GL2 gl);
}
