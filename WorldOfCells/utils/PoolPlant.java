package utils;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;
import applications.simpleworld.Plant;


public class PoolPlant extends Pool<Plant> {
	public PoolPlant() {
		super();
	}

	public Plant add(int __x , int __y, World __world) {
		if (queue.isEmpty()) {
			Plant plant = new Plant(__x,__y,__world); //default paramaters
			super.used.add(plant);
			return plant;
		 }
		Plant plant = queue.get(0);
        plant.reinitialize();
		queue.remove(0);
		used.add(plant);
		return plant;
	}

	public void reinit(Plant plant) {
		plant.reinitialize();
	}
}