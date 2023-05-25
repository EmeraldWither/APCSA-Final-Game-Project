package org.emeraldcraft.finalproject.pof.gameobjects;

import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.DIVING_PUNISHMENT;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.FLY_PUNISHMENT;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.JUMPING_PUNISHMENT;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.WALKING_REWARD;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.emeraldcraft.finalproject.pof.SegalGame;
import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.gameobjects.human.Food;
import org.emeraldcraft.finalproject.pof.gameobjects.human.Umbrella;
import org.emeraldcraft.finalproject.pof.gravity.Gravity;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class Player extends GameObject implements Controllable {
    private final boolean flying = true;
    private boolean currentlyJumping = false;
    private int jumpCounter = 0;
    private boolean jumpingArch = false;

    private final Image image;
    private boolean isDiving = false;
    private boolean divingDown = false;
    private boolean isflying = false;
    private boolean gravityOn = true;

    private final Gravity gravity = new Gravity(this);
  	private Stamina stamina;
  	private boolean isWalking = false;
	  private int foodEaten = 0;

    private double x;

    private double y;

    public Player() throws IOException {
        //do our hitbox stuff using our own method
        super("The Player", null, 1);
        File file = new File("seagull.png");
        Logger.log("Locating main player image at: " + file.getAbsolutePath());
        image = ImageIO.read(file);
      	this.stamina = new Stamina(this);
    }

    public Gravity getGravityEngine() {
        return gravity;
    }

    //Override the location because of the weird way that our physics coordinates work
    @Override
    public Rectangle getLocation() {
        return new Rectangle((int) x, (int) y, 240, 90);
    }
    public void setStamina(int stamina) {
      this.stamina.setStamina(stamina);
    }
    public int getStamina() {
      return this.stamina.getStamina();
    }
    public void staminaIncrease(int input) {
    	this.stamina.increase(input);
    }
    

    @Override
    public void control(double x, double y) {
        this.x += x;
        this.y += y;

        //If statements for controlling and creating the border
        //USED AS PRIMARY COMMAND BACKUP LOGIC IN @GameRenderer
        if (getLocation().x >= 1680) {
            //undo the operation
            this.x -= x;
        }
        //Value has to be negative one, otherwise there is a weird glitch upon startup with 
        //the player being stuck in the top left hand corner
        if (getLocation().x <= -1) {
            this.x -= x;
            gravity.setVel(0,0);
        }
        if (getLocation().y >= 980) {
            this.y -= y;
            gravity.setVel(0, 0);
        }
        if (getLocation().y <= 0) {
            this.y -= y;
            gravity.setVel(0, 0);
        }
        //Makes sure stamina will regenerate when on the ground
        if(getLocation().y >= 960) {
        	isWalking = true;
        }
        else isWalking = false;
        //Make sure that gravity will disable when flying
        if(getLocation().y <= 200) {
        	gravity.setGravityEnabled(false);
        	gravity.setVel(0, 0);
        }
        else if(gravity.isGravityEnabled() == false && getLocation().y >= 200) {
        	gravity.setGravityEnabled(true);
        }
        //The code above will prevent the seagull from going off the screen
        
        
        //check for collisions
        //create a temporary rectangle to compare with
        Rectangle hitbox = new Rectangle(getLocation());
        for (GameObject gameObject : SegalGame.getInstance().getGameObjects()) {
            if (!gameObject.canCollide()) continue;
            if (!hitbox.intersects(gameObject.getLocation())) continue;
            //loop until we are no longer in the x
            this.x -= x;
			gravity.setVelX(0);


            //check down
            int amount = y < 0 ? -1 : 1;
            hitbox.y += amount;
            if (!hitbox.intersects(gameObject.getLocation())) continue;
            this.y -= y;
            gravity.setVelY(0);

            return;
        }
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
        jumpLogic();
        divingLogic();
        staminaLogic();  
        umbrellaLogic();
        gravity.tickGravity();
        control(gravity.getXVel(), gravity.getYVel());
      
    }
    private void staminaLogic() {
      if(isDiving) stamina.decrease(DIVING_PUNISHMENT);
      else if(isWalking && !currentlyJumping) stamina.increase(WALKING_REWARD);
      else if (currentlyJumping) stamina.decrease(JUMPING_PUNISHMENT);
      else stamina.decrease(FLY_PUNISHMENT);
      if(stamina.getStamina() == 0) {
          Logger.log("Game end. Player died!");
          SegalGame.getInstance().stop();
        }  
    }
    
    private void fly() {
    	if(getLocation().y <= 400) {
    		isflying = true;
    	}
    	if(isflying) {
    		
    	}
    }
    private void umbrellaLogic(){
    	for (GameObject gameObject : SegalGame.getInstance().getGameObjects()) {
    		if(!(gameObject instanceof Umbrella)) continue;
            if (!getLocation().intersects(gameObject.getLocation())) continue;
            control(0, -10);
            gravity.setVelY(20);
            Logger.log("launched from umbrella");
            return;
        }
    }

    private void divingLogic() {
        //don't do anything if we are not diving
        if (!isDiving) return;

        //bounds check
        if (getLocation().x + getLocation().width >= 1920) {
            isDiving = false;
            return;
        }
        
        //Normal Diving Logic
        if (divingDown && getLocation().y < 900) {
        	//TODO Currently getting stuck in an infinite loop with the log statement below
//        	Logger.log("dive movement up");
            y += 15;
            x += 5;
            //TODO NEED TO SET VELOCITY FOR DIVE DOWN METHOD
            gravity.setVel(5, 15);
        }
        //if we are at the bottom, then flip our diving direction
        else if (divingDown && getLocation().y >= 900) divingDown = !divingDown;
        else if (!divingDown && getLocation().y >= 100) {
//            Logger.log("dive movement down");
            y -= 10;
            x += 4;
            gravity.setVel(4, -10);
        } else if (!divingDown && getLocation().y <= 300) {
            isDiving = false;
        }
    }
    
    private void jumpLogic() {
        if (getLocation().y >= 700 && currentlyJumping && getLocation().x <= 1650 && getLocation().x >= 0) {
            //shLogger.log("The Jump command has been triggered");
            if (jumpCounter < 140 && !jumpingArch) {
                //Logger.log("jumping");
                getLocation().y -= 10;
                getLocation().x += 4;
                jumpCounter += 5;
                if (jumpCounter >= 135) {
                    //Logger.log("if statement go burrr");
                    jumpingArch = true;
                }
            } else if (jumpingArch) {
//				Logger.log("falling");
                getLocation().y += 10;
                getLocation().x += 4;
                jumpCounter -= 5;
                if (jumpCounter <= 0) {
                    jumpingArch = false;
                    jumpCounter = 0;
                    currentlyJumping = false;
//					Logger.log("The Jump Command has ended");
                }
            }
        }
    }

    public void applyForce(double x, double y) {
        gravity.setVel(x, y);
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
