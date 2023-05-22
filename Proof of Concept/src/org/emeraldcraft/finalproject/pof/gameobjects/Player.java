package org.emeraldcraft.finalproject.pof.gameobjects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.emeraldcraft.finalproject.pof.SegalGame;
import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class Player extends GameObject implements Controllable {	
	private final boolean flying = true;
	private boolean currentlyJumping = false;
	private int jumpCounter = 0;
	private boolean jumpingArch = false;
	
	private final Image image;
	private boolean isDiving = false;
	private boolean divingDown = true;
	
	private Stamina stamina;
	
	private boolean isWalking = false;

	private int foodEaten = 0;
	
	public Player() throws IOException {
		super("The Player", new Rectangle(240, 90), 1);
		File file = new File("seagull.png");
		Logger.log("Locating main player image at: " + file.getAbsolutePath());
		image = ImageIO.read(file);
		this.stamina = new Stamina(this);
		
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
		isWalking = getLocation().y >= 960;
		//The code above will prevent the seagull from going off the screen
		
	}
	public void setSamina(int stamina) {
		this.stamina.setStamina(stamina);
	}
	public int getStamina() {
		return this.stamina.getStamina();
	}
	public void dive() {
		isDiving = true;
		divingDown = true;
	}
	
	public void jump(boolean isJumping) {
		currentlyJumping = true;
	}
	
	@Override
	public void tick() {
		eatingLogic();
		walkLogic();
		divingLogic();
		staminaLogic();
		//check stamina
		if(stamina.getStamina() == 0) {
			Logger.log("Game end. Player died!");
			SegalGame.getInstance().stop();
		}
	}
	private void staminaLogic() {
		if(isDiving) stamina.decrease(5);
		else if(isWalking && !currentlyJumping) stamina.increase(5);
		else if (currentlyJumping) stamina.decrease(2);
		else stamina.decrease(2);
		
	}
	private void divingLogic(){
		//dont do anything if we are not diving
		if(!isDiving) return;

		//bounds check
		if(getLocation().x + getLocation().width >= 1920){
			isDiving = false;
			return;
		}


		if(divingDown && getLocation().y < 900){
			getLocation().y += 25;
			getLocation().x += 10;
		}
		//if we are at the bottom, then flip our diving direction
		else if(divingDown && getLocation().y >= 900) divingDown = !divingDown;
		else if(!divingDown && getLocation().y >= 100){
			getLocation().y -= 15;
			getLocation().x += 7;
		}
		else if (!divingDown && getLocation().y <= 300) {
			isDiving = false;
		}
	}
	private void walkLogic(){
		if(getLocation().y >= 700 && currentlyJumping && getLocation().x <= 1650 && getLocation().x >= 0) {
			//shLogger.log("The Jump command has been triggered");
			if(jumpCounter < 140 && !jumpingArch) {
				//Logger.log("jumping");
				getLocation().y-=10;
				getLocation().x+=4;
				jumpCounter+=5;
				if(jumpCounter >= 135) {
					//Logger.log("if statement go burrr");
					jumpingArch = true;
				}
			}
			else if(jumpingArch) {
//				Logger.log("falling");
				getLocation().y+=10;
				getLocation().x+=4;
				jumpCounter-=5;
				if(jumpCounter <= 0) {
					jumpingArch = false;
					jumpCounter = 0;
					currentlyJumping = false;
//					Logger.log("The Jump Command has ended");
				}
			}
		}
	}
	private void eatingLogic() {
		for(GameObject object : SegalGame.getInstance().getGameObjects()) {
			if(object instanceof Food) {
				if(object.getLocation().intersects(this.getLocation())) {
					Logger.log("Ate the food!");
					stamina.increase(400);
					foodEaten++;
					object.remove();
				}
			}
		}
	}

	@Override
	public boolean shouldRemove() {
		return false;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, getLocation().x, getLocation().y, null);
	}

	public int getFoodEaten() {
		return foodEaten;
	}

}
