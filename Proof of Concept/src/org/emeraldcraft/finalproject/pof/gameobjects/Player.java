package org.emeraldcraft.finalproject.pof.gameobjects;

import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Player extends GameObject implements Controllable {
	private final Point loc = new Point(0, 0);
	private final Image image;
	public Player() throws IOException {
		super("The Player", new Rectangle(100, 100), 1);
		File file = new File("seagull.png");
		Logger.log("Locating main player image at: " + file.getAbsolutePath());
		image = ImageIO.read(file);
	}

	@Override
	public void control(double x, double y) {
		loc.x += x;
		loc.y += y;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, loc.x, loc.y, null);
	}

}
