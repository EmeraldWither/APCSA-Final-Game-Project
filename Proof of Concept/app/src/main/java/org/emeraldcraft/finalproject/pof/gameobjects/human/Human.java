/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.gameobjects.human;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Represents the humans that are holding the food
 */
public class Human extends GameObject
{
    private final boolean hadFood;
    private Image human;
    private Food food;

    public Human()
    {
        super("Human", new Rectangle(1950, 800, 150, 338), 2);
        Random generator = new Random();
        int randomNumForHumanGeneration = generator.nextInt(3) + 1;
        File file = new File("assets/humanVersion" + randomNumForHumanGeneration + ".png");
        Logger.log("Attempting to load image from " + file.getAbsolutePath());
        try
        {
            human = ImageIO.read(file);
        } catch (IOException e)
        {
            Logger.warn("Failed to read image from " + file.getAbsolutePath());
            e.printStackTrace();
            System.exit(-1);
        }
        //Generate a random number to figure out what configuration the human will be in.
        int randomNum = generator.nextInt(3) + 1;
        
        if (randomNum == 1)
        {
            //Human no food
            hadFood = false;
        } else if (randomNum == 2)
        {
            //Human with food
            this.food = new Food("Sandwich", this);
            hadFood = true;
        } else
        {
            //Umbrella with human with food
            this.food = new Food("Sandwich", this);
            new Umbrella(this);
            hadFood = true;
        }
    }

    public Food getHeldFood()
    {
        return this.food;
    }

    /**
     * Removes the food from the Human.
     */
    public void removeFood()
    {
        this.food.remove();
        this.food = null;
    }

    public boolean hadFood()
    {
        return this.hadFood;
    }

    @Override
    public String toString()
    {
        String foodStr = food == null ? "null" : getHeldFood().toString();
        return super.toString() + " | HUMAN: { Food: " + foodStr + "}";
    }

    @Override
    public boolean shouldRemove()
    {
        //If we are off-screen, then we should probably remove ourselves
        if (getLocation().x + 150 < 0)
        {
            Logger.log("Removing human!");
        }
        return getLocation().x + 150 < 0;
    }

    @Override
    public void render(Graphics g)
    {
        g.drawImage(human, getLocation().x, getLocation().y, null);
    }

    @Override
    public void tick()
    {
        getLocation().y = 740;
        getLocation().x -= 3;
    }
}
