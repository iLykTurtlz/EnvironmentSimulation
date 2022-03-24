package utils;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

import applications.simpleworld.Prey;

public class PoolPrey extends Pool<Prey> {

	public PoolPrey() {
		super();
	}

	public Prey add(int __x , int __y, World __world) {
		if (queue.isEmpty()) {
			Prey prey = new Prey(__x,__y,__world); //default paramaters
			super.used.add(prey);
			return prey;
		}
		Prey prey = queue.get(0);
        prey.reinitialize();
		queue.remove(0);
		used.add(prey);
		return prey;
	}

	public void reinit(Prey prey) {
		prey.reinitialize();
	}
}