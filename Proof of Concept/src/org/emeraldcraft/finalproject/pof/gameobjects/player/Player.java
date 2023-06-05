package org.emeraldcraft.finalproject.pof.gameobjects.player;

import static org.emeraldcraft.finalproject.pof.GameSettings.GravityEngine.FORCE_DEBOUNCE;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.DIVING_PUNISHMENT;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.EATING_REWARD;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.FLY_PUNISHMENT;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.JUMPING_PUNISHMENT;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.WALKING_REWARD;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.emeraldcraft.finalproject.pof.SegalGame;
import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.gameobjects.human.Human;
import org.emeraldcraft.finalproject.pof.gameobjects.human.Umbrella;
import org.emeraldcraft.finalproject.pof.gravity.Gravity;
import org.emeraldcraft.finalproject.pof.utils.Logger;

/**
 * The Main Seagull Player
 */
public class Player extends GameObject implements Controllable {
    private final Image image;

    // Diving logic variables which track in which way the seagull is diving
    private boolean isDiving = false;
    private boolean divingDown = false;

    private final Gravity gravity = new Gravity();
  	private final Stamina stamina;
  	private boolean isWalking = false;
    private int foodEaten = 0;
    private boolean isDropping = false;

    private double x = 5;
    private double y = 5;
    
    
    private int coinsEarned = 0;



	// Provides a "debounce" to prevent a user from spamming set velocity which can make it
    // look stuttering when holding a key
    private long lastVelocityInput = System.currentTimeMillis();
    
    private final PlayerCosemetic cosmetic;

    public Player(PlayerCosemetic.PlayerCosemetics cosmetic) throws IOException {
        //do our hitbox stuff using our own method
        super("The Player", null, 1);
        File file = new File("assets/seagull.png");
        Logger.log("Locating main player image at: " + file.getAbsolutePath());
        image = ImageIO.read(file);
      	this.stamina = new Stamina(this);
      	this.cosmetic = new PlayerCosemetic(this, cosmetic);
      	gravity.setGravityEnabled(false );
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
    public void staminaDecrease(int input) {
    	this.stamina.decrease(input);
    }
    public int getCoinsEarned() {
		return coinsEarned;
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
        isWalking = getLocation().y >= 960;
        //Make sure that gravity will disable when flying
        if(getLocation().y < 300 && !isDropping) {
        	gravity.setGravityEnabled(false);
        	gravity.setVel(0, 0);
        }
        else if(gravity.isGravityDisabled() && getLocation().y >= 300) {
        	gravity.setGravityEnabled(true);
        	gravity.setVelY(0);
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

			
			//check if we are under
			Point2D pointX = new Point2D.Double(gameObject.getLocation().getX(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
            Point2D pointY = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
            
            Line2D line = new Line2D.Double(pointX, pointY);
            
            if(getLocation().intersectsLine(line)) {
                gravity.setVelY(0);                
                return;
            }

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

    @Override
    public void tick() {
        eatingLogic();
        divingLogic();
        staminaLogic();
        umbrellaLogic();
        if(getLocation().y >= 300) {
			isDropping = false;
		}
        gravity.tickGravity();
        control(gravity.getXPos(), gravity.getYPos());
    }
    private void staminaLogic() {
        boolean currentlyJumping = false;
        if(isDiving) stamina.decrease(DIVING_PUNISHMENT);
      else if(isWalking && !currentlyJumping) stamina.increase(WALKING_REWARD);
      else if (currentlyJumping) stamina.decrease(JUMPING_PUNISHMENT);
      else stamina.decrease(FLY_PUNISHMENT);
      if(stamina.getStamina() == 0) {
          Logger.log("Game end. Player died!");
          SegalGame.getInstance().stop();
        }  
    }
    private void umbrellaLogic(){
    	for (GameObject gameObject : SegalGame.getInstance().getGameObjects()) {
    		if(!(gameObject instanceof Umbrella)) continue;
            if (!getLocation().intersects(gameObject.getLocation())) continue;
            
            
            isDiving = false;
            //make sure we are at the top
            //create a line at the bottom of the umbrella
            if(topUmbrellaIntersect(gameObject)) {
                control(0, -10);
                gravity.setVelY(20);                
                Logger.log("launch from right");
                return;
            }
            if(bottomUmbrellaIntersect(gameObject)) {
                control(0, 10);
                gravity.setVelY(-5);                
                Logger.log("launch from under");
                return;
            }
            if(leftSideUmbrellaIntersect(gameObject)) {
                control(-10, 0);
                gravity.setVelX(-5);
                gravity.setVelY(5);
                Logger.log("launch from left");
                return;
            } 
            if(rightSideUmbrellaIntersect(gameObject)) {
                control(10, 0);
                gravity.setVelX(5);
                gravity.setVelY(5);
                Logger.log("launch from right");
                return;
            } 
        }
    }
    
    private boolean topUmbrellaIntersect(GameObject gameObject) {
   	 	Point2D x = new Point2D.Double(gameObject.getLocation().getX(), gameObject.getLocation().getY());
        Point2D y = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY());
        
        Line2D line = new Line2D.Double(x, y);
        return getLocation().intersectsLine(line);
    }
    private boolean leftSideUmbrellaIntersect(GameObject gameObject) {
    	Point2D x = new Point2D.Double(gameObject.getLocation().getX(), gameObject.getLocation().getY());
        Point2D y = new Point2D.Double(gameObject.getLocation().getX(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
        
        Line2D line = new Line2D.Double(x, y);
        return getLocation().intersectsLine(line);       
   }
    private boolean rightSideUmbrellaIntersect(GameObject gameObject) {
    	Point2D x = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY());
        Point2D y = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
        
        Line2D line = new Line2D.Double(x, y);
        return getLocation().intersectsLine(line); 
    }
    private boolean bottomUmbrellaIntersect(GameObject gameObject) {
    	 Point2D x = new Point2D.Double(gameObject.getLocation().getX(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
         Point2D y = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
         
         Line2D line = new Line2D.Double(x, y);
         return getLocation().intersectsLine(line);
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
            y += 15;
            x += 5;
            gravity.setVel(5, 15);
        }
        //if we are at the bottom, then flip our diving direction
        else if (divingDown && getLocation().y >= 900) divingDown = !divingDown;
        else if (!divingDown && getLocation().y >= 100) {
            y -= 10;
            x += 4;
            gravity.setVel(4, -10);
        } else if (!divingDown && getLocation().y <= 300) {
            isDiving = false;
        }
    }
    

    public void applyForce(double x, double y) {
        if(System.currentTimeMillis() - lastVelocityInput >= FORCE_DEBOUNCE) {
            gravity.setVel(x, y);
            lastVelocityInput = System.currentTimeMillis();
        }
    }
    public void applyForceX(double x){
        if(System.currentTimeMillis() - lastVelocityInput >= FORCE_DEBOUNCE) {
            gravity.setVelX(x);
            lastVelocityInput = System.currentTimeMillis();
        }
    }
    public void applyForceY(double y){
        if(System.currentTimeMillis() - lastVelocityInput >= FORCE_DEBOUNCE) {
            gravity.setVelY(y);
            lastVelocityInput = System.currentTimeMillis();
        }
    }
  
	private void eatingLogic() {
		for(GameObject object : SegalGame.getInstance().getGameObjects()) {
			if(object instanceof Human) {
				if(object.getLocation().intersects(this.getLocation())) {
					if(!((Human) object).hadFood()) {
						 Logger.log("Game end. Player died to human!");
				          SegalGame.getInstance().stop();
				          return;
					}
					if(((Human) object).getHeldFood() == null) return;
					
					
					Logger.log("Ate the food!");
					stamina.increase(EATING_REWARD);
					foodEaten++;
					coinsEarned++;
					((Human) object).removeFood();
				}
			}
			
		}
	}
	
	public void dropLogic() {
		Logger.log("running dropLogic");
		if(getLocation().y <= 300 && gravity.isGravityDisabled()) {
			isDropping = true;
			gravity.setGravityEnabled(true);
		}
	}

	@Override
	public boolean shouldRemove() {
		return false;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, getLocation().x, getLocation().y, null);
		cosmetic.render(g);
	}

	public int getFoodEaten() {
		return foodEaten;
	}

}
