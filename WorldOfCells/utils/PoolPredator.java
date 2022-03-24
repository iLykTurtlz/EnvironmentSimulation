package utils;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;


import applications.simpleworld.Predator;

public class PoolPredator extends Pool<Predator> {
	public PoolPredator() {
		super();
	}

	public Predator add(int __x , int __y, World __world) {
		if (queue.isEmpty()) {
			Predator pred = new Predator(__x,__y,__world); //default paramaters
			super.used.add(pred);
			return pred;
		 }
		Predator pred = queue.get(0);
        pred.reinitialize();
		queue.remove(0);
		used.add(pred);
		return pred;
	}

	public void reinit(Predator pred) {
		pred.reinitialize();
	}
}