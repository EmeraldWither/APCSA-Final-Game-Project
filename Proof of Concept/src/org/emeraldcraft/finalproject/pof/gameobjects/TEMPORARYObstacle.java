package org.emeraldcraft.finalproject.pof.gameobjects;

import org.emeraldcraft.finalproject.pof.components.GameObject;

import java.awt.*;

public class TEMPORARYObstacle extends GameObject {

    public TEMPORARYObstacle() {
        super("Temporary Obstacle", new Rectangle(600, 800, 150, 150), 1);
    }

    @Override
    public boolean shouldRemove() {
        return false;
    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public void tick() {
        getLocation().x = 600;
        getLocation().y = 800;
    }
}
