package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

public class Prey extends Agent {

    public Prey( int __x , int __y, World __world ) {
        super(__x,__y,__world, new float[] {0.f,0.f,1.f});
    }

}