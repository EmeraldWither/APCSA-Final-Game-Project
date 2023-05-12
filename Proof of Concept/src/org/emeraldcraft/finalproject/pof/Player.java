package org.emeraldcraft.finalproject.pof;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;

public class Player extends GameObject implements Controllable {

	public Player() {
		super("The Player", new Rectangle(100, 100));
	}

	@Override
	public void control(double x, double y) {
		
	}

	@Override
	public void render(Graphics g) {
		
	}

}
