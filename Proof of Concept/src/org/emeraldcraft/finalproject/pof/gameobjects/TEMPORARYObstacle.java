package org.emeraldcraft.finalproject.pof.gameobjects;

import org.emeraldcraft.finalproject.pof.components.GameObject;

import java.awt.*;

public class TEMPORARYObstacle extends GameObject {

    public TEMPORARYObstacle(int x) {
        super("Temporary Obstacle", new Rectangle(x, 800, 150, 500), 1);
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
    }
}
