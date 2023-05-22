package org.emeraldcraft.finalproject.pof.gameobjects.human;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Food extends GameObject {
	private Image foodImage;
	private final Human owningHuman;
	public Food(String name, Point location, Human owningHuman) {
		super(name, new Rectangle(owningHuman.getLocation().x - 30, owningHuman.getLocation().x - 30, 100, 95), 1);
		File file = new File("food_sandwich.png");
		Logger.log("Attempting to load image from " + file.getAbsolutePath());
		try {
			foodImage = ImageIO.read(file);
		} catch (IOException e) {
			Logger.warn("Failed to read image from " + file.getAbsolutePath());
			e.printStackTrace();
			System.exit(-1);
		}
		this.owningHuman = owningHuman;
		
	}

	@Override
	public boolean shouldRemove() {
		return getLocation().x + 100 < 0;
	}

	@Override
	public void render(Graphics g) {
		if(owningHuman == null) return;
		g.drawImage(foodImage, getLocation().x, getLocation().y, null);
		
	}

	@Override
	public void tick() {
		if(owningHuman == null) {
			return;
		}
		getLocation().x = owningHuman.getLocation().x - 30;
		getLocation().y = owningHuman.getLocation().y + 50;
	}
	
}
