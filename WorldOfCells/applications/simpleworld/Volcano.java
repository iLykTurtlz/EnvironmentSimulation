package applications.simpleworld;

import java.util.*;
import applications.simpleworld.*;
import worlds.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.gl2.GLUT;

public class Volcano {
    private WorldOfTrees world;
    private LavaCA lava;
    private int x = 0, y = 0;
    private int range;
    private static final int LAVA_RANGE = 4;
    private static final double ERUPTION_CHANCE = .1d;
    private boolean erupted = false;

    public Volcano(WorldOfTrees world, int dxView, int dyView, int range) {
        this.world = world;
        this.range = range;
        this.lava = new LavaCA(world, dxView, dyView);
    }

    public Volcano(WorldOfTrees world, int dxView, int dyView) {
        this(world, dxView, dyView, 25);
    }

    /*
     * The function initVolcano() has been put to pause and is used at its very primal version because of the deadline.
     * It only searches for the highest peak in the map in a square of size MAP_LENGTH-range considering a squared map.
     * Then it defines the x and y values of the center of the volcano which is the base of everything,
     * starting from the center it defines a disk of a radius of LAVA_RANGE where it lowers the heights values of the cells in this disk.
     * This allows the lava to be put in those cells without directly flowing at the beginning.
     */
    public void initVolcano() {
        double[][] landscape = world.getMap();
        double max = 0d;
        int off = 10;
        //1 - look on the map a place where the volcano can spawn
        for (int xi = range + off; xi < landscape.length - range - off; xi++) {
                for (int yi = range + off; yi < landscape[0].length - range - off; yi++) {
                    if (landscape[xi][yi] > max) {
                        max = landscape[xi][yi];
                        x = xi;
                        y = yi;
                    }
                }
            }

        /** NOTE : the second part is paused because of the deadline **/
        //make it grow or instantly spawn?
        //2 - increase altitude based on the center thanks to sinus or cosinus or any increasing function (landscape[x][y] *= function(x/x,y)
        //we use sinus function since it is an increasing function between 0 and pi/2
        /*System.out.println("Increasing the borders");
        float i = 0;
        float max_height = 0.5f;
        visited = new double[landscape.length][landscape[0].length];
        Random random = new Random();
        double step = -1d;
        for (int xi = x - range; xi < x + range; xi++) {
            for (int yi = y - range; yi < y + range; yi++) {
                int xm = (xi + landscape.length) % landscape.length;
                int ym = (yi + landscape[0].length) % landscape[0].length;
                if ((xm - x)*(xm - x) + (ym - y)*(ym - y) <= range*range/4) {
                if (visited[xm][ym] == 1) continue;
                    if (landscape[xm][ym] >= WorldOfTrees.WATER_LEVEL) {
                        visited[xm][ym] = 1;
                        System.out.println("nextGaussian("+step+") : " + nextGaussian(step));
                        landscape[xm][ym] = landscape[xm][ym] * (1d+nextGaussian(step));
                    }
                }
                step += 1d/(2d*range);
            }
            step = -1d;
            i = 0;
        }*/
        //will keep the previous perlin noise values but only increase it => random volcano)

        //3 - add lava inside of it (the generation of the volcano will make a hollow inside of it to pop lava
        //change color of cells (done in draw Volcano) and decrease height from center
        for (int xi = x - range; xi < x + range; xi++) {
            for (int yi = y - range; yi < y + range; yi++) {
                if ((xi - x)*(xi - x) + (yi - y)*(yi - y) <= LAVA_RANGE*LAVA_RANGE) {
                    world.getMap()[xi][yi] *= 0.95d;
                }
            }
        }

        //4 - init lava
        lava.init();
    }

    /*
     * The eruption itself is mainly raising the heights values of the cells at the center of the volcano (in a disk : circle equation).
     * The lava is set for those cells and because of the lava being a cellular automata responding to height difference it will flow
     * onto the map.
     */
    private long eruption_time;
    public void erupt() {
        if (erupted) return;
        eruption_time = System.currentTimeMillis();
        for (int xi = x - range; xi < x + range; xi++) {
            for (int yi = y - range; yi < y + range; yi++) {
                if ((xi - x)*(xi - x) + (yi - y)*(yi - y) <= LAVA_RANGE*LAVA_RANGE) {
                    world.getMap()[xi][yi] /= 0.95d; //tiny elevation for the lava to be able to flow
                    lava.setCellState(xi, yi, 1); //init lava
                }
            }
        }
        lava.swapBuffer(); // buffer has been changed by the previous call we need to come back to it
        erupted = true;
    }

    /*
     * The step function updates the lava each 10 world iterations.
     * It also checks each 5 seconds if there is an eruption and if yes if it's done to reset the volcano (=> setting the heights of its center cells at a lower value).
     * There is also a probability of ERUPTION_CHANCE for the volcano to erupt.
     */
    public void step() {
        if (erupted) {
            boolean done = true;
            if ((System.currentTimeMillis() - eruption_time)/1000f >= 5f) { // check every 5 seconds if eruption is done
                loop:
                for (int i = 0; i < world.getMap().length; i++) {
                    for (int y = 0; y < world.getMap()[0].length; y++) {
                        if (lava.getCellState(i, y) == 1 || lava.getCellState(i, y) >= 3) { // if there is any lava then cannot erupt again
                            done = false;
                            break loop;
                        }
                    }
                }
                if (done) {
                    erupted = false;
                    for (int xi = x - range; xi < x + range; xi++) {
                        for (int yi = y - range; yi < y + range; yi++) {
                            if ((xi - x)*(xi - x) + (yi - y)*(yi - y) <= LAVA_RANGE*LAVA_RANGE) {
                                world.getMap()[xi][yi] *= 0.95d; //tiny decrementation return to initial state
                                lava.setCellState(xi, yi, 1);
                            }
                        }
                    }
                }
            }
        }

        if (world.getIteration() % 10 == 0) {
            if (Math.random() < ERUPTION_CHANCE)
                erupt();
            lava.step();
        }
    }

    /*
     *
     * This function does not draw the lava but draws the volcano at its initial state.
     * The volcano is not at eruption and the lava inside of it is at a height where it cannot flow.
     * The volcano is drawn according to a disk on the 2d map.
     *
     */
    private float[] init_color = null;
    private boolean init = true;
    float color1[] = {0.1f, 0.1f, 0.1f};
    float color2[] = {0.1f, 0.1f, 0.1f};
    public void drawVolcano(GL2 gl) {
        if (world.getIteration() % 10 != 0) return;
        double[][] landscape = world.getMap();
        for (int xi = x - range; xi < x + range; xi++) {
            for (int yi = y - range; yi < y + range; yi++) {
                int xm = (xi + landscape.length) % (landscape.length);
                int ym = (yi + landscape[0].length) % (landscape[0].length);
                if ((xm - x)*(xm - x) + (ym - y)*(ym - y) <= range*range) {
                    float height = (float) world.getCellHeight(xm, ym);
                    if (height >= WorldOfTrees.WATER_LEVEL) { //if the cells where the volcano will spawn are not water
                        if (world.getIteration() % 10 == 0 && (xm - x)*(xm - x) + (ym - y)*(ym - y) <= LAVA_RANGE*LAVA_RANGE) {
                            float r = (float) Math.random();
                            color2[0] = (float) (0.8d * r);
                            color2[1] = (float) (0.3d * r);
                            color2[2] = 0f;
                            world.setCellState(xm, ym, color2);
                        } else if (height >= WorldOfTrees.SNOW_LINE) { //if the cells are above the height SNOW_LINE then we take the initial color of the moutain and save it to draw a nice gradient according to the height
                            if (init_color == null) {
                                init_color = world.getCellColorValue(xm, ym);
                            }
                            color1[0] = init_color[0]*height;
                            color1[1] = init_color[1]*height;
                            color1[2] = init_color[2]*height;
                            world.setCellState(xm, ym, color1);
                        }
                    }
                }
            }
        }
    }

    public boolean isLava(int x, int y) {
        return lava.getCellState(x, y) == 1 || lava.getCellState(x, y) >= 3;
    }

    public boolean isStone(int x, int y) {
        return lava.getCellState(x, y) == 2;
    }
}
