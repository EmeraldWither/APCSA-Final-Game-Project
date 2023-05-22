package org.emeraldcraft.finalproject.pof.gameobjects.human;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.emeraldcraft.finalproject.pof.components.GameObject;

public class Umbrella extends GameObject {
	private final Human owningHuman;
	public Umbrella(Human owningHuman) {
		super("Umbrella", new Rectangle(0, 0, 250, 200), 2);
		this.owningHuman = owningHuman;
	}

	@Override
	public boolean shouldRemove() {
		return owningHuman.shouldRemove();
	}

	@Override
	public void render(Graphics g) {

	}

	@Override
	public void tick() {
		getLocation().x = owningHuman.getLocation().x - 50;
		getLocation().y = owningHuman.getLocation().y - 100;
	}

}
