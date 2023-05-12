package org.emeraldcraft.finalproject.pof;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.emeraldcraft.finalproject.pof.components.GameObject;

/**
 * Represents the humans that are holding the food
 *
 */
public class Human extends GameObject {
	private Food food;
	public Human(Food food) {
		super("Human with " + food.getName(), new Rectangle(0,0));
		this.food = food;
	}
	public Food getHeldFood() {
		return this.food;
	}
	public void removeFood() {
		food = null;
	}
	@Override
	public void render(Graphics g) {
		//Render human
		
	}
}
