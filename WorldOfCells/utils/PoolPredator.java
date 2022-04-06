package utils;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;


import applications.simpleworld.Predator;
import applications.simpleworld.WorldOfTrees;

public class PoolPredator extends Pool<Predator> {
	public static final int MAX_NB_PREDATORS = 400;

	public PoolPredator() {
		super();
	}

	
	public Predator add(int __x , int __y, WorldOfTrees __world, int[] offspringCharacters) {
		if (used.size() >= MAX_NB_PREDATORS)	{
			return null;
		}
		if (queue.isEmpty()) {
			Predator pred = new Predator(__x,__y,__world, offspringCharacters);
			super.used.add(pred);
			return pred;
		}
		Predator pred = queue.get(0);
        reinit(__x, __y, pred, offspringCharacters);
		queue.remove(0);
		used.add(pred);
		return pred;
	}
	
	public Predator add(int __x , int __y, WorldOfTrees __world) {
		if (queue.isEmpty()) {
			Predator pred = new Predator(__x,__y,__world);	//call constructor with random characters
			super.used.add(pred);
			return pred;
		}
		Predator pred = queue.get(0);
        reinit(pred);										//reinit with random characters
		queue.remove(0);
		used.add(pred);
		return pred;
	}

	public void reinit(int x, int y, Predator pred, int[] offspringCharacters) {
		pred.reinitialize(x, y, offspringCharacters);
	}

	public void reinit(Predator pred)	{
		pred.reinitialize();
	}
}