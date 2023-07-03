/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.menu.CosmeticsMenu;
import org.emeraldcraft.finalproject.pof.menu.MainMenu;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.emeraldcraft.finalproject.pof.DebugValues.SHOW_HITBOXES;

/**
 * Handles the main rendering for the game
 */
public class GameRenderer extends JComponent
{
    private final JPanel panel;
    private final JFrame frame;
    private SegallGame game;
    private boolean isRunning = false;
    private JFrame gameFrame;

    public GameRenderer(JPanel panel, JFrame frame)
    {
        this.panel = panel;
        this.frame = frame;
    }

    public JFrame getFrame()
    {
        return frame;
    }

    public JFrame getGameFrame()
    {
        return gameFrame;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        //If we are in the main menu, then don't paint
        if (SegallGame.getInstance().isMainMenu())
        {
            return;
        }
        paintGame(g);
    }

    public void switchToGame()
    {
        Logger.log("Main game sequence started. Destroying the old JFrame and creating the new one");
        frame.setVisible(false);
        gameFrame = new JFrame();
        gameFrame.add(this);
        gameFrame.setName("Seagull Swipe");
        gameFrame.setSize(1920, 1040);
        gameFrame.setUndecorated(true);
        gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        gameFrame.addWindowListener(new WindowListener()
        {

            @Override
            public void windowOpened(WindowEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowIconified(WindowEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeiconified(WindowEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeactivated(WindowEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowClosing(WindowEvent arg0)
            {
                game.stop();
            }

            @Override
            public void windowClosed(WindowEvent arg0)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void windowActivated(WindowEvent arg0)
            {
                // TODO Auto-generated method stub

            }
        });
        gameFrame.addKeyListener(new KeyListener()
        {
            //Special type of list which can only hold unique values
            private final Set<Character> keys = new HashSet<>();

            @Override
            public void keyTyped(KeyEvent e)
            {

            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                keys.add(e.getKeyChar());
                handleKeys();
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                keys.remove(e.getKeyChar());
            }

            public void handleKeys()
            {
                //Handle all of our keypresses here
                for (char key : keys)
                {
                    //left key
                    if (key == 'a') game.getPlayer().control(-20, 0);
                        //right key
                    else if (key == 'd') game.getPlayer().control(20, 0);
                    //up key
                    if (key == 'w')
                    {
                        if (game.getPlayer().getGravityEngine().isGravityDisabled())
                        {
                            game.getPlayer().control(0, -20);
                        }
                    }
                    //down key
                    else if (key == 's')
                    {
                        if (game.getPlayer().getGravityEngine().isGravityDisabled())
                        {
                            game.getPlayer().control(0, 20);
                        }
                        game.getPlayer().applyForceY(-10);
                    }
                    else if (key == 'x') game.getPlayer().dive();
                        //forward jump key
                    else if (key == 'g')
                    {
                        game.getPlayer().applyForce(3, 20);
                        game.getPlayer().staminaDecrease(GameSettings.StaminaSettings.JUMPING_PUNISHMENT);
                    }
                    //backward jump key
                    if (key == 'f')
                    {
                        game.getPlayer().applyForce(-3, 20);
                        game.getPlayer().staminaDecrease(GameSettings.StaminaSettings.JUMPING_PUNISHMENT);
                    }
                    //drop key to move from the sky to the ground without returning
                    if (key == 'q')
                    {
                        game.getPlayer().dropLogic();
                    }
                    if (key == 'j'){
                        game.getPlayer().applyForceY(10);
                    }

                }
            }
        });
        SegallGame.getInstance().init();
        SegallGame.getInstance().start();
        gameFrame.setVisible(true);

    }

    public void switchToCosmetics()
    {
        frame.setVisible(false);
        JFrame cosmeticsFrame = new JFrame();
        CosmeticsMenu cosmetics = new CosmeticsMenu();
        cosmeticsFrame.add(cosmetics);
        cosmeticsFrame.setResizable(false);
        cosmeticsFrame.addWindowListener(new WindowListener()
        {

            @Override
            public void windowOpened(WindowEvent arg0)
            {
            }

            @Override
            public void windowIconified(WindowEvent arg0)
            {
            }

            @Override
            public void windowDeiconified(WindowEvent arg0)
            {
            }

            @Override
            public void windowDeactivated(WindowEvent arg0)
            {
            }

            @Override
            public void windowClosing(WindowEvent arg0)
            {
            }

            @Override
            public void windowClosed(WindowEvent arg0)
            {
                //Save the current cosmetic that was selected when the user left
                cosmetics.writeCurrentCosmetic();
                frame.setVisible(true);
            }

            @Override
            public void windowActivated(WindowEvent arg0)
            {
            }
        });
        cosmeticsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cosmeticsFrame.setLocation(480, 270);
        cosmeticsFrame.setSize(1920 / 2, 1080 / 2);
        cosmeticsFrame.setVisible(true);
    }

    public void paintMenu()
    {
        //pass the methods to swtich to the games or cosmetics menu
        panel.add(new MainMenu(this::switchToGame, this::switchToCosmetics));
        frame.setLocation(480, 270);
        frame.add(panel);
        panel.setVisible(true);
        frame.setVisible(true);
    }

    public void paintGame(Graphics g)
    {
        //Linux Laggy Fix
        Toolkit.getDefaultToolkit().sync();
        ArrayList<GameObject> gameObjects = game.getGameObjects();
        //backwards loop so important items are painted last (first)
        for (int i = gameObjects.size() - 1; i >= 0; i--)
        {
            GameObject gameObject = gameObjects.get(i);
            gameObject.render(g);

            //see if we have to render hitboxes
            if (SHOW_HITBOXES)
            {
                ((Graphics2D) g).setStroke(new BasicStroke(5));

                //render based on the hitbox and location of the gameobject
                g.setColor(Color.BLUE);
                g.drawRect(gameObject.getLocation().x, gameObject.getLocation().y, gameObject.getLocation().width, gameObject.getLocation().height);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.drawString("\"" + gameObject.getName() + "\"", gameObject.getLocation().x, gameObject.getLocation().y - 10);
                ((Graphics2D) g).setStroke(new BasicStroke(1));
            }
            g.setColor(Color.black);
        }
    }

    /**
     * Start this renderer
     */
    public void start()
    {
        this.game = SegallGame.getInstance();
        if (isRunning) throw new IllegalStateException("The renderer has already been started and is running.");
        isRunning = true;

        Logger.log("Game Renderer has been initialized and is running.");
        new Thread(() ->
        {
            if (SegallGame.getInstance().isMainMenu()) paintMenu();
            while (isRunning)
            {//attempt to render as fast as possible (lotta fps)
                repaint();
            }
        }).start();
    }
}
