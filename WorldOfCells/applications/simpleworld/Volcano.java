package applications.simpleworld;

import java.util.*;
import applications.simpleworld.*;
import worlds.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.gl2.GLUT;

public class Volcano {
    private WorldOfTrees world;
    private int x = 0, y = 0;
    private int range;
    private int lava_range = 3;

    public Volcano(WorldOfTrees world, int range) {
        this.world = world;
        this.range = range;
    }

    public Volcano(WorldOfTrees world) {
        this(world, 25);
    }

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
        for (int xi = x - 4; xi < x + 4; xi++) {
            for (int yi = y - 4; yi < y + 4; yi++) {
                int xm = (xi + landscape.length) % landscape.length;
                int ym = (yi + landscape[0].length) % landscape[0].length;
                landscape[xm][ym] *= 0.95;
            }
        }
    }
    private float[] init_color = null;

    public void drawVolcano(GL2 gl) {
        if (world.getIteration() % 10 != 0) return;
        double[][] landscape = world.getMap();
        float color[] = {0.1f, 0.1f, 0.1f};
        for (int xi = x - range; xi < x + range; xi++) {
            for (int yi = y - range; yi < y + range; yi++) {
                int xm = (xi + landscape.length) % (landscape.length);
                int ym = (yi + landscape[0].length) % (landscape[0].length);
                if ((xm - x)*(xm - x) + (ym - y)*(ym - y) <= range*range) {
                    float height = (float) world.getCellHeight(xm, ym);
                    if (height >= WorldOfTrees.WATER_LEVEL) {
                        if (world.getIteration() % 10 == 0 && (xm - x)*(xm - x) + (ym - y)*(ym - y) <= lava_range) {
                            float r = (float) Math.random();
                            color[0] = (float) (0.8d * r);
                            color[1] = (float) (0.3d * r);
                            color[2] = 0f;
                            world.setCellState(xm, ym, color);
                        } else if (height >= WorldOfTrees.SNOW_LINE) {
                            if (init_color == null) {
                                init_color = world.getCellColorValue(xm, ym);
                            }
                            color[0] = init_color[0]*height;
                            color[1] = init_color[1]*height;
                            color[2] = init_color[2]*height;
                            world.setCellState(xm, ym, color);
                        }
                    }
                }
            }
        }
    }
}
