package org.emeraldcraft.finalproject.pof.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.emeraldcraft.finalproject.pof.components.GameObject;

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
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
	
}
