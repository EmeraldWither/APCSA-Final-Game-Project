package org.emeraldcraft.finalproject.pof.gameobjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class Player extends GameObject implements Controllable {	
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
		getLocation().x += x;
		getLocation().y += y;
		//If statements for controlling and creating the border
		if(getLocation().x >= 1680) { 
			getLocation().x-=10;
		}
		if(getLocation().x <= 0) {
			getLocation().x+=10;
		}
		if(getLocation().y >= 980) {
			getLocation().y-=10;
		}
		if(getLocation().y <= 0) {
			getLocation().y+=10;
		}
		//The code above will prevent the seagull from going off the screen
		
	}
	public void dive(boolean isDivingUp) {
		currentlyDiving = true;
		this.divingUp = divingUp;
	}
	
	public void jump(boolean isJumping) {
		currentlyJumping = true;
	}
	
	@Override
	public void tick() {
		//Diving method logic
		int jumpCounter = 0;
		
		if(getLocation().y < 1900 && currentlyDiving && !divingUp) {
			getLocation().y++;
			getLocation().x++;
		}
		if(currentlyDiving && divingUp) {
			getLocation().y--;
			getLocation().x++;
		}
		//Jumping logic
		if(getLocation().y >= 800 && currentlyJumping) {
			Logger.log("The Jump command has been triggered");
			if(jumpCounter < 800) {
				getLocation().y-=5;
				getLocation().x+=2;
				jumpCounter+=5;
			}
			if(jumpCounter==800) jumpCounter = 0;
			currentlyJumping = false;
			Logger.log("The Jump Command has ended");
		}
		
		//Walking Logic
	}

	@Override
	public boolean shouldRemove() {
		return false;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, getLocation().x, getLocation().y, null);
	}

}
