package org.emeraldcraft.finalproject.pof;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Background extends GameObject {
    private final Image image;
    private int x = 0;
    private int imageX = 0;
    public Background() throws IOException {
        super("Background", new Rectangle(), Integer.MAX_VALUE);
        //Load our background image
        File file = new File("background.png");
        Logger.log("Locating background image at: " + file.getAbsolutePath());
        image = ImageIO.read(file);
    }
    public void moveBackground(){
        imageX-=3;
    }

    @Override
    public void render(Graphics g) {
        if(Math.abs(imageX) >= 1920) imageX = 1;
        g.drawImage(image, imageX, 0, null);
        g.drawImage(image, imageX + 1920, 0, null);
        g.setColor(Color.yellow);
    }
}
