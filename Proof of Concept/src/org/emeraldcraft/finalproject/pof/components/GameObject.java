/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.components;

import org.emeraldcraft.finalproject.pof.SegallGame;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import java.awt.*;

public abstract class GameObject
{
    private final String name;
    /**
     * Allows the game object to modify its position, and specify a hitbox
     */
    private final Rectangle location;
    private final int renderPriority;

    /**
     * @param name The name of the gameobject
     * @param hitbox A rectangle specifying the hitbox and point
     * @param renderPriority The lower the number the closer it to the front. Must be at least 1
     */
    public GameObject(String name, Rectangle hitbox, int renderPriority)
    {
        this.name = name;
        this.location = hitbox;
        this.renderPriority = renderPriority;
        SegallGame.getInstance().registerGameObject(this);
        Logger.log("Created a new GameObject of: " + this);
    }

    /**
     * @return The location of the game object with its hitbox
     */
    public Rectangle getLocation()
    {
        return this.location;
    }

    /**
     * @return True if the game object should be removed
     */
    public abstract boolean shouldRemove();

    /**
     * Removes the game object from being ticked.
     */
    public void remove()
    {
        Logger.log(this + " has been deregistered and has been removed.");
        SegallGame.getInstance().deRegisterGameObject(this);
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return "GameObject { name: \"" + name + "\", Location: " + getLocation().toString() + "}";
    }

    public abstract void render(Graphics g);

    /**
     * Called every {@link org.emeraldcraft.finalproject.pof.GameSettings.CoreSettings#TICK_TIME} ms.
     */
    public abstract void tick();

    public int getRenderPriority()
    {
        return renderPriority;
    }
}
