package org.emeraldcraft.finalproject.pof;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.emeraldcraft.finalproject.pof.DebugValues.SHOW_HITBOXES;

public class GameRenderer extends JComponent {
    private final SegalGame game = SegalGame.getInstance();
    private boolean isRunning = false;
    private final JPanel panel;
    private final JFrame frame;
    private final JFrame gameFrame = new JFrame();
    public GameRenderer(JPanel panel, JFrame frame) {
    	this.panel = panel;
        this.frame = frame;
    }
    @Override
    public void paintComponent(Graphics g) {
    	if(SegalGame.getInstance().isMainMenu()){
            return;
        }
        paintGame(g);
    }
    public void switchToGame(){
        Logger.log("Main game sequence started. Destroying the old JFrame and creating the new one");
        frame.dispose();
        gameFrame.add(this);
        gameFrame.setName("Seagull Swipe");
        gameFrame.setSize(1920, 1040);
        gameFrame.setUndecorated(true);
        gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameFrame.addKeyListener(new KeyListener() {
            //Special type of list which can only hold unique values
            private final Set<Character> keys = new HashSet<>();

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keys.add(e.getKeyChar());
                handleKeys();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys.remove(e.getKeyChar());
            }

            public void handleKeys() {
                for (char key : keys) {
                    //Logger.log("Key Event Fired! Char = " + e.getKeyChar());
                    int x = 0, y = 0;
                    if (key == 'a') x -= 10;
                    else if (key == 'd') x += 10;
                    else if (key == 'w') y -= 10;
                    else if (key == 's') y += 10;
                    game.getPlayer().control(x, y);
                }
            }
        });
        gameFrame.setVisible(true);

        SegalGame.getInstance().start();

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
        playNow.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchToGame();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }
            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        panel.add(playNow, new GridBagConstraints() );


        //Settings Button
        JButton settings = new JButton("Settings");
        settings.setFont(new Font("Arial", Font.BOLD, 32));
        settings.setPreferredSize(new Dimension(100, 100));
        settings.setAlignmentX(JButton.CENTER_ALIGNMENT);

        panel.add(settings, new GridBagConstraints());
        frame.add(panel);
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

    public void start() {
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
