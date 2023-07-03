/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.gameobjects.player;

import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles the cosmetic rendering for the Player
 */
public class PlayerCosmetic
{
    private final Player player;
    private final PlayerCosmetics cosmetics;
    private Image image;
    private int xOffset;
    private int yOffset;

    public PlayerCosmetic(Player player, PlayerCosmetics cosmetic)
    {
        this.player = player;
        this.cosmetics = cosmetic;
        if (cosmetics == PlayerCosmetics.NONE) return;
        try
        {
            image = ImageIO.read(new File("cosmetic/" + cosmetic.toString().toLowerCase() + ".png"));
            File file = new File("cosmetic/" + cosmetic.toString().toLowerCase() + ".info");
            //start reading the info file for the cosmetic

            /*
            File Format:
                x-offset
                y-offset
                cost
             */
            Scanner in = new Scanner(file);
            xOffset = Integer.parseInt(in.nextLine());
            yOffset = Integer.parseInt(in.nextLine());
            Logger.log("Loaded cosmetic info file:\n  X-Offset: " + xOffset + "\n  Y-Offset: " + yOffset);
            Logger.log("Loaded player cosmetic " + cosmetic);
            in.close();
        } catch (IOException e)
        {
            Logger.warn("Failed to create the image for the player cosmetic " + cosmetic);
            e.printStackTrace();
        }
    }

    //make our own render method
    public void render(Graphics g)
    {
        if (cosmetics == PlayerCosmetics.NONE) return;
        //draw it with the offsets in the info file
        int x = player.getLocation().x + xOffset;
        int y = player.getLocation().y + yOffset;
        g.drawImage(image, x, y, null);
    }

    /**
     * Represents all the cosmetics that the player can use. The enum name matches
     * the file name (although lowercase)
     */
    public enum PlayerCosmetics
    {
        PROPELLER_HAT,
        PARTY_HAT,
        KING_SEAGULL,
        FRENCH_SEAGULL,
        FLAMINGO,
        SEGALL_SEAGULL,
        PRIDE,
        NONE,
    }
}

