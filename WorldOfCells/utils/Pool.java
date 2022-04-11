package utils;

import com.jogamp.opengl.GL2;

import objects.UniqueDynamicObject;

import worlds.World;

import applications.simpleworld.Prey;
import applications.simpleworld.WorldOfTrees;

import java.util.ArrayList;

import worlds.*;

public abstract class Pool<E> {
	protected ArrayList<E> used;
	protected ArrayList<E> queue;

    /*
     * The Pool class is used as a specialized version of an ArrayList.
     * It uses two arraylists : used and queue.
     *      used - contains the used elements of the Pool (list)
     *      queue - contains the recycled/waiting/removed elements of the Pool (list)
     * The class is made in order to keep all elements added/created and not remove them which in the cycle of creation(allocation)/destruction(removal) would stress the game during execution.
     * It has it its child classes a limit set to the maximum number of elements the Pool can contain.
     * When the user wants to add a new element the Pool checks if there is an element queuing in the queue list which means it's not used, if yes then it returns it as a simulated new object
     * with its properties/attributes reinitialized for the user to have a fresh plain object. Otherwise it creates a new object, since the object is dependent of the generic type and is considered
     * a specific class the add function is abstract and should be carefully implemented by the user.
     * The remove function adds the element to remove to the queue list and remove it from the add list, the Pool class basically simulates an object manager for the efficiency of the program.
     * It is particularly useful for objects often removed and used like agents but not for static objects such as plants.
     */

	public Pool() {
		this.used = new ArrayList<E>();
		this.queue = new ArrayList<E>();
	}

	abstract E add(int __x, int __y, WorldOfTrees myWorld);

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

	public int getSizeQueue()	{
		return queue.size();
	}
}
