package org.emeraldcraft.finalproject.pof.gameobjects;

import org.emeraldcraft.finalproject.pof.components.GameObject;

import java.awt.*;

public class Food extends GameObject {

	public Food(String name, Point location) {
		super(name, new Rectangle(25, 25), 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean shouldRemove() {
		return false;
	}

	@Override
	public Point getLocation() {
		return new Point(0,0);
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
