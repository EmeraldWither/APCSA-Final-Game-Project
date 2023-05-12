package org.emeraldcraft.finalproject.pof.components;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.emeraldcraft.finalproject.pof.Logger;

public abstract class GameObject {
	private final String name;
	private final Rectangle hitbox; 
	private boolean isRemoved = false;
	
	public GameObject(String name, Rectangle hitbox) {
		this.name = name;
		this.hitbox = hitbox;
		Logger.debug("Created a new GameObject of: " + this);
	}
	public Rectangle getHitbox() {
		return this.hitbox;
	}
	public boolean isRemoved() {
		return isRemoved;
	}
	public void remove() {
		isRemoved = true;
	}
	public String getName() {
		return this.name;
	}
	@Override
	public String toString() {
		return "GameObject { name: \"" + name + "\n, Hitbox: " + hitbox.toString() + "}";
	}
	
	public abstract void render(Graphics g);

}
