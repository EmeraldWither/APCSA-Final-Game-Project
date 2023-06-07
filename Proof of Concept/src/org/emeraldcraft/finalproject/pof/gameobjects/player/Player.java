/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.gameobjects.player;

import org.emeraldcraft.finalproject.pof.SegallGame;
import org.emeraldcraft.finalproject.pof.components.Controllable;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.gameobjects.human.Human;
import org.emeraldcraft.finalproject.pof.gameobjects.human.Umbrella;
import org.emeraldcraft.finalproject.pof.gravity.Gravity;
import org.emeraldcraft.finalproject.pof.utils.Logger;
import org.emeraldcraft.finalproject.pof.utils.SoundManager;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import static org.emeraldcraft.finalproject.pof.GameSettings.GravityEngine.FORCE_DEBOUNCE;
import static org.emeraldcraft.finalproject.pof.GameSettings.StaminaSettings.*;

/**
 * The Main Seagull Player
 */
public class Player extends GameObject implements Controllable
{
    //normal Seagull
    private final Image image;
    //Seagull with legs when he is walking
    private final Image seagullWithLegs;
    private final Gravity gravity = new Gravity();
    private final Stamina stamina;
    private final PlayerCosmetic cosmetic;
    private final Clip eatClip;
    // Diving logic variables which track in which way the seagull is diving
    private boolean isDiving = false;
    private boolean divingDown = false;
    private boolean isWalking = false;

    //Keep track of the food eaten for points and the end game
    private int foodEaten = 0;

    //Keep track then Q is pushed
    private boolean isDropping = false;
    private double x = 5;
    private double y = 5;
    private int coinsEarned = 0;

    // Provides a "debounce" to prevent a user from spamming set velocity which can make it
    // look stuttering when holding a key
    private long lastVelocityInput = System.currentTimeMillis();

    public Player(PlayerCosmetic.PlayerCosmetics cosmetic) throws IOException
    {
        //do our hitbox stuff using our own method
        super("The Player", null, 1);

        File file = new File("assets/seagull.png");
        File walking = new File("assets/seagull_legs.png");
        Logger.log("Locating main player image at: " + file.getAbsolutePath());
        image = ImageIO.read(file);
        seagullWithLegs = ImageIO.read(walking);

        eatClip = SoundManager.getSoundEffect("eat");

        this.stamina = new Stamina();
        this.cosmetic = new PlayerCosmetic(this, cosmetic);

        //Disable gravity so we are flying at the start of the game
        gravity.setGravityEnabled(false);
    }

    public Gravity getGravityEngine()
    {
        return gravity;
    }

    //Override the location because of the weird way that our physics coordinates work
    //This is because we need to use doubles, but Rectangles can only use ints
    @Override
    public Rectangle getLocation()
    {
        return new Rectangle((int) x, (int) y, 240, 112);
    }

    /**
     * @param input The amount of stamina to decrease by
     */
    public void staminaDecrease(double input)
    {
        this.stamina.decrease(input);
    }

    /**
     * @return The amount of coins that were earned. Is the same as the amount of food eaten.
     */
    public int getCoinsEarned()
    {
        return coinsEarned;
    }

    @Override
    public void control(double x, double y)
    {
        this.x += x;
        this.y += y;

        //If statements for controlling and creating the border
        if (getLocation().x >= 1680)
        {
            //undo the operation
            this.x -= x;
        }
        //Value has to be negative one, otherwise there is a weird glitch upon startup with 
        //the player being stuck in the top left hand corner
        if (getLocation().x <= -1)
        {
            this.x -= x;
            gravity.setVel(0, 0);
        }
        if (getLocation().y >= 969)
        {
            this.y -= y;
            gravity.setVel(0, 0);
        }
        if (getLocation().y <= 0)
        {
            this.y -= y;
            gravity.setVel(0, 0);
        }

        //Makes sure stamina will regenerate when on the ground
        isWalking = getLocation().y >= 960;
        //Make sure that gravity will disable when flying, but not when dropping
        if (getLocation().y < 300 && !isDropping)
        {
            gravity.setGravityEnabled(false);
            gravity.setVel(0, 0);
        } else if (gravity.isGravityDisabled() && getLocation().y >= 300)
        {
            //but if we are under a threshold then we should have gravity enabled
            gravity.setGravityEnabled(true);
            gravity.setVelY(0);
        }


        //check for collisions
        //create a temporary rectangle to compare with
        Rectangle hitbox = new Rectangle(getLocation());
        for (GameObject gameObject : SegallGame.getInstance().getGameObjects())
        {
            //make sure that we have an umbrella
            if (!(gameObject instanceof Umbrella)) continue;
            if (!hitbox.intersects(gameObject.getLocation())) continue;

            //if we are intersecting, lets move back out of the intersecting either
            this.x -= x;
            gravity.setVelX(0);


            //check if we are under the umbrella
            Point2D pointY = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());

            //create a line of the umbrella
            Line2D line = new Line2D.Double(getTopLeftLocation(gameObject), pointY);

            if (getLocation().intersectsLine(line))
            {
                gravity.setVelY(0);
                return;
            }

            //check down
            //first check to see if we should be going up or down
            int amount = y < 0 ? -1 : 1;
            hitbox.y += amount;
            //if we do not intersect, then continue
            if (!hitbox.intersects(gameObject.getLocation())) continue;

            //cancel our velocity and go back to our previous position
            this.y -= y;
            gravity.setVelY(0);

            return;
        }
    }

    private Point2D.Double getTopLeftLocation(GameObject gameObject)
    {
        return new Point2D.Double(gameObject.getLocation().getX(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
    }

    public void dive()
    {
        isDiving = true;
        divingDown = true;
    }

    @Override
    public void tick()
    {
        eatingLogic();
        divingLogic();
        staminaLogic();
        umbrellaLogic();

        //reset our dropping variable if are under the threshold
        if (getLocation().y >= 300)
        {
            isDropping = false;
        }
        gravity.tickGravity();
        control(gravity.getXPos(), gravity.getYPos());
    }

    /**
     * Performs the logic for the stamina
     */
    private void staminaLogic()
    {
        //Decrease our stamina depending on what condition the player is currently in
        boolean currentlyJumping = false;
        if (isDiving) stamina.decrease(DIVING_PUNISHMENT);
        else if (isWalking && !currentlyJumping) stamina.increase(WALKING_REWARD);
        else stamina.decrease(FLY_PUNISHMENT);

        //if we have no stamina we die :(
        if (stamina.getStamina() == 0)
        {
            Logger.log("Game end. Player died!");
            SegallGame.getInstance().stop();
        }
    }

    /**
     * The logic to see if the umbrella
     */
    private void umbrellaLogic()
    {
        for (GameObject gameObject : SegallGame.getInstance().getGameObjects())
        {
            //hunt for umbrellas
            if (!(gameObject instanceof Umbrella)) continue;
            if (!getLocation().intersects(gameObject.getLocation())) continue;


            //if we are intersecting, then make sure that we are not diving
            isDiving = false;

            //perform the intersection checks
            //if we are intersecting, also play the bounce noise
            //and teleport us out of the hitbox of the umbrella
            if (topUmbrellaIntersect(gameObject))
            {
                control(0, -10);
                gravity.setVelY(20);
                SoundManager.getSoundEffect("bounce").start();
                return;
            }
            if (bottomUmbrellaIntersect(gameObject))
            {
                control(0, 10);
                gravity.setVelY(-5);
                SoundManager.getSoundEffect("bounce").start();
                return;
            }
            if (leftSideUmbrellaIntersect(gameObject))
            {
                control(-10, 0);
                gravity.setVelX(-5);
                gravity.setVelY(5);
                SoundManager.getSoundEffect("bounce").start();
                return;
            }
            if (rightSideUmbrellaIntersect(gameObject))
            {
                control(10, 0);
                gravity.setVelX(5);
                gravity.setVelY(5);
                SoundManager.getSoundEffect("bounce").start();
                return;
            }
        }
    }

    //The following methods run umbrellas intersection checks

    private boolean topUmbrellaIntersect(GameObject gameObject)
    {
        Point2D y = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY());
        Line2D line = new Line2D.Double(getTopLeftLocation(gameObject), y);
        return getLocation().intersectsLine(line);
    }

    private boolean leftSideUmbrellaIntersect(GameObject gameObject)
    {
        Point2D y = new Point2D.Double(gameObject.getLocation().getX(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
        Line2D line = new Line2D.Double(getTopLeftLocation(gameObject), y);
        return getLocation().intersectsLine(line);
    }

    private boolean rightSideUmbrellaIntersect(GameObject gameObject)
    {
        Point2D x = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY());
        Point2D y = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
        Line2D line = new Line2D.Double(x, y);
        return getLocation().intersectsLine(line);
    }

    private boolean bottomUmbrellaIntersect(GameObject gameObject)
    {
        Point2D x = new Point2D.Double(gameObject.getLocation().getX(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());
        Point2D y = new Point2D.Double(gameObject.getLocation().getX() + gameObject.getLocation().getWidth(), gameObject.getLocation().getY() + gameObject.getLocation().getHeight());

        Line2D line = new Line2D.Double(x, y);
        return getLocation().intersectsLine(line);
    }

    private void divingLogic()
    {
        //don't do anything if we are not diving
        if (!isDiving) return;

        //bounds check
        if (getLocation().x + getLocation().width >= 1920)
        {
            isDiving = false;
            return;
        }

        //Normal Diving Logic
        //go down
        if (divingDown && getLocation().y < 900)
        {
            y += 8;
            x += 4;
            gravity.setVel(1, 9);
        }
        //if we are at the bottom, then flip our diving direction
        else if (divingDown && getLocation().y >= 900) divingDown = !divingDown;
            //go UP instead
        else if (!divingDown && getLocation().y >= 100)
        {
            y -= 8;
            x += 2;
            gravity.setVel(4, -10);
        }
        //we have made it to the top, we can stop diving now
        else if (!divingDown && getLocation().y <= 300)
        {
            isDiving = false;
        }
    }


    /**
     * Applies a force on the player using the gravity engine, and if gravity is enabled.
     * There is a debounce check to prevent spamming as well.
     *
     * @param x X velocity factor
     * @param y Y velocity factor
     */
    public void applyForce(double x, double y)
    {
        if (System.currentTimeMillis() - lastVelocityInput >= FORCE_DEBOUNCE)
        {
            gravity.setVel(x, y);
            lastVelocityInput = System.currentTimeMillis();
        }
    }

    /**
     * Applies a force on the player using the gravity engine, and if gravity is enabled.
     *
     * @param y Y velocity factor
     */
    public void applyForceY(double y)
    {
        if (System.currentTimeMillis() - lastVelocityInput >= FORCE_DEBOUNCE)
        {
            gravity.setVelY(y);
            lastVelocityInput = System.currentTimeMillis();
        }
    }

    private void eatingLogic()
    {
        for (GameObject object : SegallGame.getInstance().getGameObjects())
        {
            //check to see if we are intersecting human
            if (!(object instanceof Human)) continue;
            if (!object.getLocation().intersects(this.getLocation())) return;

            //if they do not have food, then they caught us and we died
            if (!((Human) object).hadFood()){
                Logger.log("Game end. Player died to human!");
                SegallGame.getInstance().stop();
                return;
            }
            if (((Human) object).getHeldFood() == null) return;

            //give the player reward for stealing sandwich
            Logger.log("Ate the food!");
            stamina.increase(EATING_REWARD);
            foodEaten++;
            coinsEarned++;

            //play the sound clip
            eatClip.setFramePosition(0);
            eatClip.start();
            //get rid of the food (we ate it)
            ((Human) object).removeFood();

        }
    }

    /**
     * Drops the player from the sky
     */
    public void dropLogic()
    {
        if (getLocation().y <= 300 && gravity.isGravityDisabled())
        {
            isDropping = true;
            gravity.setGravityEnabled(true);
        }
    }

    @Override
    public boolean shouldRemove()
    {
        return false;
    }

    @Override
    public void render(Graphics g)
    {
        //If we are in the sky (gravity is disabled), then retract our legs
        if (gravity.isGravityDisabled())
        {
            g.drawImage(image, getLocation().x, getLocation().y, null);
        }
        //or we extend them
        else
        {
            g.drawImage(seagullWithLegs, getLocation().x, getLocation().y, null);
        }
        //draw the cosmetic
        cosmetic.render(g);
    }

    public int getFoodEaten()
    {
        return foodEaten;
    }

}
