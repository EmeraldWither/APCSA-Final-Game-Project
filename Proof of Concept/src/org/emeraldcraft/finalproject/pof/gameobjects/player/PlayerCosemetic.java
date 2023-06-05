package org.emeraldcraft.finalproject.pof.gameobjects.player;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.emeraldcraft.finalproject.pof.utils.Logger;

public class PlayerCosemetic {
	private final Player player;
	private final PlayerCosemetics cosemetics;
	private Image image;
	private File file;
	private int xOffset;
	private int yOffset;
	public PlayerCosemetic(Player player, PlayerCosemetics cosemetic) {
		this.player = player;
		this.cosemetics = cosemetic;
		if(cosemetics == PlayerCosemetics.NONE) return;
		try {
			image = ImageIO.read(new File("cosemetic/" + cosemetic.toString().toLowerCase() + ".png"));
			//info file
			file = new File("cosemetic/" + cosemetic.toString().toLowerCase() + ".info");
			//start reading file
			Scanner in = new Scanner(file);
			xOffset = Integer.parseInt(in.nextLine());
			yOffset = Integer.parseInt(in.nextLine());
			Logger.log("Loaded cosemetic info file:\n  X-Offset: " + xOffset + "\n  Y-Offset: " + yOffset);
			Logger.log("Loaded player cosemetic " + cosemetic.toString());
			in.close();
		} catch (IOException e) {
			Logger.warn("Failed to create the image for the player cosemetic " + cosemetic.toString());
			e.printStackTrace();
		}
		
	}
	//make our own render method
	public void render(Graphics g) {
		if(cosemetics == PlayerCosemetics.NONE) return;
		int x = player.getLocation().x + xOffset;
		int y = player.getLocation().y + yOffset;
		g.drawImage(image, x, y, null);
	}
	
	public enum PlayerCosemetics {
		PROPELLER_HAT,
		PARTY_HAT,
		KING_SEAGULL,
		FRENCH_SEAGULL,
		NONE,
	}
}

