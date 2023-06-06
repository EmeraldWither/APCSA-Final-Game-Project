package org.emeraldcraft.finalproject.pof.gameobjects.player;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.emeraldcraft.finalproject.pof.utils.Logger;

public class PlayerCosmetic {
	private final Player player;
	private final PlayerCosmetics cosmetics;
	private Image image;
	private File file;
	private int xOffset;
	private int yOffset;
	public PlayerCosmetic(Player player, PlayerCosmetics cosmetic) {
		this.player = player;
		this.cosmetics = cosmetic;
		if(cosmetics == PlayerCosmetics.NONE) return;
		try {
			image = ImageIO.read(new File("cosmetic/" + cosmetic.toString().toLowerCase() + ".png"));
			//info file
			file = new File("cosmetic/" + cosmetic.toString().toLowerCase() + ".info");
			//start reading file
			Scanner in = new Scanner(file);
			xOffset = Integer.parseInt(in.nextLine());
			yOffset = Integer.parseInt(in.nextLine());
			Logger.log("Loaded cosmetic info file:\n  X-Offset: " + xOffset + "\n  Y-Offset: " + yOffset);
			Logger.log("Loaded player cosmetic " + cosmetic.toString());
			in.close();
		} catch (IOException e) {
			Logger.warn("Failed to create the image for the player cosmetic " + cosmetic.toString());
			e.printStackTrace();
		}
		
	}
	//make our own render method
	public void render(Graphics g) {
		if(cosmetics == PlayerCosmetics.NONE) return;
		int x = player.getLocation().x + xOffset;
		int y = player.getLocation().y + yOffset;
		g.drawImage(image, x, y, null);
	}
	
	public enum PlayerCosmetics {
		PROPELLER_HAT,
		PARTY_HAT,
		KING_SEAGULL,
		FRENCH_SEAGULL,
		FLAMINGO,
		PRIDE,
		NONE,
	}
}

