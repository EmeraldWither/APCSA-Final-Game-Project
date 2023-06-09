/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.gameobjects.human;

import org.emeraldcraft.finalproject.pof.SegallGame;
import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.gameobjects.player.PlayerCosmetic;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A sandwich!
 * In French mode, is a croissant
 */
public class Food extends GameObject
{
    private final Human owningHuman;
    private Image foodImage;

    public Food(String name, Human owningHuman)
    {
        super(name, new Rectangle(owningHuman.getLocation().x - 30, owningHuman.getLocation().x - 30, 100, 95), 1);
        File file;
        // If we are in French mode, then we should load a French food
        if (SegallGame.getInstance().getAppliedCosmetic() == PlayerCosmetic.PlayerCosmetics.FRENCH_SEAGULL)
            file = new File("assets/food_french.png");
        else file = new File("assets/food_sandwich.png");

        Logger.log("Attempting to load image from " + file.getAbsolutePath());
        try
        {
            foodImage = ImageIO.read(file);
        } catch (IOException e)
        {
            Logger.warn("Failed to read image from " + file.getAbsolutePath());
            e.printStackTrace();
            System.exit(-1);
        }
        this.owningHuman = owningHuman;

    }

    @Override
    public boolean shouldRemove()
    {
        return getLocation().x + 100 < 0;
    }

    @Override
    public void render(Graphics g)
    {
        //Sometimes the Owning Human is null for some reason, so lets not render it if it is
        if (owningHuman == null) return;
        g.drawImage(foodImage, getLocation().x, getLocation().y, null);

    }

    @Override
    public void tick()
    {
        if (owningHuman == null)
        {
            return;
        }
        getLocation().x = owningHuman.getLocation().x - 30;
        getLocation().y = owningHuman.getLocation().y + 170;
    }

}
