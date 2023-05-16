package org.emeraldcraft.finalproject.pof.gameobjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class Player extends GameObject implements Controllable {
	private final Point loc = new Point(0, 0);
	
	private boolean flying = true;
	private boolean currentlyJumping = false;
	private boolean currentlyDiving = false;
	private boolean divingUp = false;
	
	private final Image image;
	public Player() throws IOException {
		super("The Player", new Rectangle(240, 90), 1);
		File file = new File("seagull.png");
		Logger.log("Locating main player image at: " + file.getAbsolutePath());
		image = ImageIO.read(file);
	}

	@Override
	public void control(double x, double y) {
		loc.x += x;
		loc.y += y;
	}
	public void dive(boolean isDivingUp) {
		currentlyDiving = true;
		this.divingUp = divingUp;
	}
	@Override
	public void tick() {
		if(loc.y < 1900 && currentlyDiving && !divingUp) {
			loc.y++;
			loc.x++;
		}
		if(currentlyDiving && divingUp) {
			loc.y--;
			loc.x++;
		}
	}

	@Override
	public boolean shouldRemove() {
		return false;
	}

	@Override
	public Point getLocation() {
		return loc;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, loc.x, loc.y, null);
	}

}
