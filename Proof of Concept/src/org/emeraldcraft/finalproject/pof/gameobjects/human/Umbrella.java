package org.emeraldcraft.finalproject.pof.gameobjects.human;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Umbrella extends GameObject {
	private final Human owningHuman;
	private Image image;
	public Umbrella(Human owningHuman) {
		super("Umbrella", new Rectangle(0, 0, 250, 200), 3);
		this.owningHuman = owningHuman;
		File file = new File("assets/umbrella.png");
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			Logger.warn("Could not load asset image for the umbrella");
			e.printStackTrace();
		}
	}

	@Override
	public boolean shouldRemove() {
		return owningHuman.shouldRemove();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, getLocation().x, getLocation().y, null);
	}

	@Override
	public void tick() {
		getLocation().x = owningHuman.getLocation().x - 50;
		getLocation().y = owningHuman.getLocation().y - 80;
	}

}
