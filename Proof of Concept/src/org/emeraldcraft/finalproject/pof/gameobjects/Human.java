package org.emeraldcraft.finalproject.pof.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

/**
 * Represents the humans that are holding the food
 *
 */
public class Human extends GameObject {
	private Food food;
	public Human() {
		super("Human with food", new Rectangle(1950, 800, 150, 300), 1);
		this.food = new Food("Sandwich", new Point(0, 0), this);
	}
	public Food getHeldFood() {
		return this.food;
	}
	public void removeFood() {
		food = null;
	}

	@Override
	public String toString(){
		String foodStr = food == null ? "null" : getHeldFood().toString();
		return super.toString() + " | HUMAN: { Food: " + foodStr + "}";
	}
	@Override
	public boolean shouldRemove() {
		if(getLocation().x + 150 < 0) {
			Logger.log("Removing human!");
		}
		return getLocation().x + 150 < 0;
	}
	
	@Override
	public void render(Graphics g) {
		
	}
	@Override
	public void tick() {
		
		getLocation().y = 800;
		getLocation().x-=3;
		
	}
}
