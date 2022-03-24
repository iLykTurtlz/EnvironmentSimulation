package utils;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

import applications.simpleworld.Prey;

import java.util.ArrayList;

import worlds.*;

abstract class Pool<E> {
	protected ArrayList<E> used;
	protected ArrayList<E> queue;

	public Pool() {
		this.used = new ArrayList<E>();
		this.queue = new ArrayList<E>();
	}

	abstract E add(int __x , int __y, World __world);

	public E get(int index) {
		return used.get(index);
	}

	public void remove(int index) {
		E e = used.get(index);
		if (e != null) {
			queue.add(e);
			used.remove(index);
		}
	}

	public void remove(E e) {
		if (used.contains(e)) {
			queue.add(e);
			used.remove(e);
		}
	}

	abstract void reinit(E e);

	public void clear() {
		//TO DO clear() method in Agent and so on
		used.clear();
		queue.clear();
	}

	public int getSizeUsed()	{
		return used.size();
	}

	public int getSizedQueue()	{
		return queue.size();
	}
}
