/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.gravity;

import static org.emeraldcraft.finalproject.pof.GameSettings.GravityEngine.*;

/**
 * Gravity engine which handles the gravity for the {@link org.emeraldcraft.finalproject.pof.gameobjects.player.Player} class.
 */
public class Gravity
{
    /*
    Store the current modified position from the old one (prePos)
     */
    private final double[] pos = new double[2];
    private final double[] prePos = new double[2];
    private double xVel = 0;
    private double yVel = 0;
    /*
      Know how long it has been since we have applied velocity
     */
    private long startTime = System.currentTimeMillis();
    private boolean enabled = true;

    /**
     * Sets the current velocity calculations based on the X and Y velocity.
     * It will reset the timer and set the initial velocity == 0.
     *
     * @param x X velocity
     * @param y Y Velocity
     */
    public void setVel(double x, double y)
    {
        yVel = y * Y_VEL_MODIFIER;
        xVel = x * X_VEL_MODIFIER;
        pos[0] = -1;
        pos[1] = -1;
        startTime = System.currentTimeMillis();
    }

    /**
     * Sets the X velocity factor
     * Does not reset the timer
     *
     * @param x X velocity
     */
    public void setVelX(double x)
    {
        xVel = x * X_VEL_MODIFIER;
        pos[0] = xVel;
    }

    /**
     * Sets the Y velocity factor
     * Does reset the timer
     *
     * @param y Y velocity
     */
    public void setVelY(double y)
    {
        yVel = y * Y_VEL_MODIFIER;
        pos[1] = -1;
        startTime = System.currentTimeMillis();
    }

    /**
     * Enabling gravity will cause the timer to be reset.
     */
    public void setGravityEnabled(boolean enabled)
    {
        this.enabled = enabled;
        startTime = System.currentTimeMillis();
    }

    public boolean isGravityDisabled()
    {
        return !enabled;
    }

    /**
     * Ticks the gravity and will update any position variables along with it.
     * WIll also run the calculations which can be passed into {@link org.emeraldcraft.finalproject.pof.components.Controllable#control}
     */
    public void tickGravity()
    {
        //If we have no gravity, don't move the player
        if (!enabled)
        {
            pos[0] = 0;
            pos[1] = 0;
            return;
        }
        //Measure the time since the last velocity input
        double time = (double) (System.currentTimeMillis() - startTime) / 1000.0;

        //Feed it into the velocity formula
        double yDistance = (yVel * time) - (0.5 * ((GRAVITY_CONSTANT) * time * time));

        //Calculate the difference from the previous position of the player
        if (pos[0] == -1 || pos[1] == -1)
        {
            prePos[0] = xVel;
            prePos[1] = yDistance;
        }
        pos[0] = xVel;
        pos[1] = -(yDistance - prePos[1]) * 50;
        prePos[1] = yDistance;
    }

    public double getXPos()
    {
        return pos[0];
    }

    public double getYPos()
    {
        return pos[1];
    }

}
