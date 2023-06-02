package org.emeraldcraft.finalproject.pof.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class Background extends GameObject {
    private final Image image;
    private int imageX = 0;
    public Background() throws IOException {
        super("Background", new Rectangle(1920, 1080), Integer.MAX_VALUE);
        //Load our background image
        File file = new File("assets/background.png");
        Logger.log("Locating background image at: " + file.getAbsolutePath());
        image = ImageIO.read(file);
    }
    @Override
    public void tick() {
        imageX-=3;
    	
    }

    @Override
    public boolean shouldRemove() {
        return false;
    }

    @Override
    public void render(Graphics g) {
        if(Math.abs(imageX) >= 1920) imageX = 1;
        g.drawImage(image, imageX, 0, null);
        g.drawImage(image, imageX + 1920, 0, null);
        g.setColor(Color.yellow);
    }
}