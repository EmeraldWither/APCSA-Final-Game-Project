package org.emeraldcraft.finalproject.pof;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.emeraldcraft.finalproject.pof.DebugValues.SHOW_HITBOXES;

public class GameRenderer extends JComponent {
    private final SegalGame game = SegalGame.getInstance();
    private boolean isRunning = false;
    private final JPanel panel;
    public GameRenderer(JPanel panel) {
    	this.panel = panel;
    }
    @Override
    public void paintComponent(Graphics g) {
    	if(SegalGame.getInstance().isMainMenu()) return;
        paintGame(g);
    }
    public void paintMenu() {
    	GridLayout layout = new GridLayout(5, 3);
    	panel.setLayout(layout);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
        Panel textPanel = new Panel();

        JLabel label = new JLabel("Sandwich Swipe");
        label.setFont(new Font("Arial", Font.BOLD, 64));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);


        textPanel.add(label, new GridBagConstraints());
        panel.add(textPanel);
        //Play Now Button
    	JButton playNow = new JButton("Play");
    	playNow.setFont(new Font("Arial", Font.BOLD, 32));
    	playNow.setPreferredSize(new Dimension(100, 100));
    	playNow.setAlignmentX(JButton.CENTER_ALIGNMENT);

    	panel.add(playNow, new GridBagConstraints() );
    	
    	
    	//Settings Button
    	JButton settings = new JButton("Settings");
    	settings.setFont(new Font("Arial", Font.BOLD, 32));
    	settings.setPreferredSize(new Dimension(100, 100));
    	settings.setAlignmentX(JButton.CENTER_ALIGNMENT);

    	panel.add(settings, new GridBagConstraints());
    }
    public void paintGame(Graphics g) {
    	ArrayList<GameObject> gameObjects = game.getGameObjects();
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.render(g);
            //see if we have to render hitboxes
            if(SHOW_HITBOXES){
                ((Graphics2D) g).setStroke(new BasicStroke(5));

                g.setColor(Color.BLUE);
                g.drawRect(gameObject.getLocation().x, gameObject.getLocation().y, gameObject.getHitbox().width, gameObject.getHitbox().height);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString("\"" + gameObject.getName() + "\"", gameObject.getLocation().x, gameObject.getLocation().y - 10);
            }
            g.setColor(Color.black);
        }
    }

    public void start()
    {
        if(isRunning) throw new IllegalStateException("The renderer has already been started and is running.");
        isRunning = true;
        Logger.log("Game Renderer has been initialized and is running.");
        if(SegalGame.getInstance().isMainMenu()) paintMenu();
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
