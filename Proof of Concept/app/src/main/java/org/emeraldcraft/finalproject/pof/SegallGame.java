/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.gameobjects.Background;
import org.emeraldcraft.finalproject.pof.gameobjects.human.Human;
import org.emeraldcraft.finalproject.pof.gameobjects.player.Player;
import org.emeraldcraft.finalproject.pof.gameobjects.player.PlayerCosmetic.PlayerCosmetics;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.emeraldcraft.finalproject.pof.GameSettings.CoreSettings.TICK_TIME;
import static org.emeraldcraft.finalproject.pof.GameSettings.HumanSettings.HUMAN_MAX_SPAWN_TIME;
import static org.emeraldcraft.finalproject.pof.GameSettings.HumanSettings.HUMAN_MIN_SPAWN_TIME;

public class SegallGame
{
    private static SegallGame instance;
    private final ArrayList<Human> humans = new ArrayList<>();
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    //queues for the gameobjects to be modified
    private final List<GameObject> removeObjectsQueue = new ArrayList<>();
    private final List<GameObject> addObjectQueue = new ArrayList<>();


    private final GameRenderer gameRenderer;
    private Player player;
    private boolean isMainMenu = true;
    private boolean isRunning = false;
    private long lastHumanSpawn = 0;

    //default to nothing
    private PlayerCosmetics appliedCosmetic = PlayerCosmetics.NONE;

    public SegallGame(GameRenderer game)
    {
        this.gameRenderer = game;
    }

    public static SegallGame getInstance()
    {
        return instance;
    }

    //static instance to access the segallgame anywehre
    public static void setInstance(SegallGame game)
    {
        if (instance == null)
        {
            instance = game;
        }
    }

    public PlayerCosmetics getAppliedCosmetic()
    {
        return appliedCosmetic;
    }

    public void setAppliedCosmetic(PlayerCosmetics appliedCosmetic)
    {
        this.appliedCosmetic = appliedCosmetic;
    }

    public void init()
    {
        Logger.log("Game init Called");
        try
        {
            //remove everything
            gameObjects.clear();
            removeObjectsQueue.clear();
            addObjectQueue.clear();
            //clear memory
            System.gc();

            //create player and background, but don't tick yet
            player = new Player(this.appliedCosmetic);
            new Background();

        } catch (IOException e)
        {
            Logger.log("Failed to load images.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Ends the game.
     */
    public void stop()
    {

        isRunning = false;
        //stop background music
        int coinsEarned = player.getCoinsEarned();
        Scanner in;

        //add our newly earned coins!
        File file = new File("cosmetic/.config");
        try
        {
            in = new Scanner(file);
        } catch (FileNotFoundException e)
        {
            Logger.warn("Could not load owned cosmetics!");
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        //Rewrite the damn file
        String fileContent = in.nextLine() + "\n";
        fileContent += (in.nextInt() + coinsEarned) + "\n";
        in.nextLine();
        fileContent += in.nextLine();
        Logger.log(fileContent);
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.flush();
            writer.close();
        } catch (IOException e)
        {
            Logger.warn("Could not update coins!");
            e.printStackTrace();
            return;
        }

        //finish writing new coins


        //Game has ended
        //Show game end JFrame
        JFrame jframe = new JFrame();
        GridLayout layout = new GridLayout(5, 3);
        jframe.setLayout(layout);
        jframe.setName("Game Over!");
        //game over text
        Panel textPanel = new Panel();

        JLabel label = new JLabel("Game Over!");
        label.setFont(new Font("Arial", Font.BOLD, 64));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        textPanel.add(label, new GridBagConstraints());
        jframe.add(textPanel);


        Panel scorePanel = new Panel();

        JLabel scoreLabel = new JLabel("You ate " + player.getFoodEaten() + " sandwiches and earned " + player.getCoinsEarned() + " coins!");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 32));
        scoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        scorePanel.add(scoreLabel, new GridBagConstraints());
        jframe.add(scorePanel);

        jframe.setSize(1920 / 2, 1080 / 2);
        jframe.setLocation(480, 270);
        //get rid of the game frame
        gameRenderer.getGameFrame().dispose();
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.addWindowListener(new WindowListener()
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
                gameRenderer.getFrame().setVisible(true);

            }

            @Override
            public void windowClosed(WindowEvent arg0)
            {
            }

            @Override
            public void windowActivated(WindowEvent arg0)
            {
            }
        });
        //set the game over screen to be visible
        jframe.setVisible(true);
    }


    /**
     * Spawns a human into the game.
     */
    public void createHuman()
    {
        Human human = new Human();
        humans.add(human);
        lastHumanSpawn = System.currentTimeMillis();
    }

    public ArrayList<Human> getHumans()
    {
        return new ArrayList<>(humans);
    }

    public ArrayList<GameObject> getGameObjects()
    {
        return new ArrayList<>(gameObjects);
    }

    public void registerGameObject(GameObject gameObject)
    {
        Logger.log("Registering a game object");
        addObjectQueue.add(gameObject);
    }

    public boolean isMainMenu()
    {
        return this.isMainMenu;
    }

    public void start()
    {
        Logger.log("STARTING A NEW GAME");
        isMainMenu = false;
        isRunning = true;

        //spawn the game logic thread

        new Thread(() ->
        {
            //measure how long we are ticking for
            long lastTickTime;

            //create an inital human
            createHuman();
            while (isRunning)
            {
                lastTickTime = System.currentTimeMillis();
                for (GameObject gameObject : addObjectQueue)
                {
                    //add any game objects that needed to be added
                    if (gameObject.getRenderPriority() >= gameObjects.size()) gameObjects.add(gameObject);
                    else gameObjects.add(gameObject.getRenderPriority() - 1, gameObject);
                }
                //clear the object queue since we added all of them now
                addObjectQueue.clear();

                //tick everything
                for (GameObject gameObject : gameObjects)
                {
                    gameObject.tick();
                    if (gameObject.shouldRemove()) gameObject.remove();
                }

                //queue removing any game objects
                gameObjects.removeAll(removeObjectsQueue);
                removeObjectsQueue.clear();

                //calculate if we should spawn a human
                double time = (double) (System.currentTimeMillis() - lastHumanSpawn) / 1000.0;
                if (time >= Math.random() * 100.0 * HUMAN_MAX_SPAWN_TIME + HUMAN_MIN_SPAWN_TIME)
                {
                    createHuman();
                }

                //Tick calculations, and how long we should sleep for
                long timeElapsed = System.currentTimeMillis() - lastTickTime;
                if (timeElapsed > TICK_TIME)
                    //spawn a warning in case we take too long to tick
                    Logger.warn("Is the main thread lagging? Thread took " + timeElapsed + "ms to successfully tick.");
                else
                {
                    try
                    {
                        //noinspection BusyWait
                        Thread.sleep(TICK_TIME - timeElapsed);
                    } catch (InterruptedException e)
                    {
                        Logger.warn("Failed to sleep the main thread. Crashing with stacktrace");
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
            }
            //and start it immediately
        }).start();
    }

    public void deRegisterGameObject(GameObject gameObject)
    {
        removeObjectsQueue.add(gameObject);
    }

    public Player getPlayer()
    {
        return player;
    }

}
