package org.emeraldcraft.finalproject.pof.gameobjects;

import org.emeraldcraft.finalproject.pof.SegalGame;
import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.gravity.Gravity;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Player extends GameObject implements Controllable {	
	private final boolean flying = true;
	private boolean currentlyJumping = false;
	private int jumpCounter = 0;
	private boolean jumpingArch = false;
	
	private final Image image;
	private boolean isDiving = false;
	private boolean divingDown = true;

	private final Gravity gravity = new Gravity(this);

	private double x;

	private double y;
	public Player() throws IOException {
		//do our hitbox stuff using our own method
		super("The Player", null, 1);
		File file = new File("seagull.png");
		Logger.log("Locating main player image at: " + file.getAbsolutePath());
		image = ImageIO.read(file);
	}

	public Gravity getGravityEngine() {
		return gravity;
	}
	//Override the location because of the weird way that our physics coordinates work
	@Override
	public Rectangle getLocation(){
		return new Rectangle((int) x, (int) y, 240, 90);
	}

	@Override
	public void control(double x, double y) {
		this.x += x;
		this.y += y;
		//If statements for controlling and creating the border
		if(getLocation().x >= 1680) {
			//undo the operation
			this.x-=x;
		}
		if(getLocation().x <= 0) {
			this.x+=x;
		}
		if(getLocation().y >= 980) {
			this.y-=y;
			gravity.setVel(0, 0);
		}
		if(getLocation().y <= 0) {
			this.y+=y;
		}
		//The code above will prevent the seagull from going off the screen
		
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
		gravity.tickGravity();
//		getLocation().x = (int) gravity.getXVel();
//		getLocation().y = (int) gravity.getYVel();
		control(gravity.getXVel(), gravity.getYVel());
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
					object.remove();
				}
			}
		}
	}
	public void applyForce(double x, double y) {
		gravity.setVel(x, y);
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
