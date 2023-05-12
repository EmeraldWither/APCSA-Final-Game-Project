package org.emeraldcraft.finalproject.pof;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameRenderer extends JComponent {
    private final SegalGame game = SegalGame.getInstance();
    private boolean isRunning = false;
    @Override
    public void paintComponent(Graphics g) {
        ArrayList<GameObject> gameObjects = game.getGameObjects();
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.render(g);
            g.setColor(Color.black);
        }
    }

    public void start()
    {
        if(isRunning) throw new IllegalStateException("The renderer has already been started and is running.");
        isRunning = true;
        Logger.log("Game Renderer has been initialized and is running.");
        new Thread(() -> {
            while (isRunning)
            {//attempt to render as fast as possible
                repaint();
            }
        }).start();
    }
    public void stop(){
        isRunning = false;
    }
}
