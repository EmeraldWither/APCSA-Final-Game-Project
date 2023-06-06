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
import org.emeraldcraft.finalproject.pof.utils.SoundManager;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
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

public class SegalGame
{
    private static SegalGame instance;
    private final ArrayList<Human> humans = new ArrayList<>();
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    //destroy queue
    private final List<GameObject> removeObjectsQueue = new ArrayList<GameObject>();
    private final List<GameObject> addObjectsQueue = new ArrayList<GameObject>();
    private final File file = new File(".config");
    private final GameRenderer gameRenderer;
    private Player player;
    private Background background;
    private boolean isMainMenu = true;
    private boolean isRunning = false;
    private long lastHumanSpawn = 0;
    private PlayerCosmetics appliedCosmetic = PlayerCosmetics.NONE;
    private int coins = -1;
    private Clip backgroundMusic;

    public SegalGame(GameRenderer game)
    {
        this.gameRenderer = game;
    }

    public static SegalGame getInstance()
    {
        return instance;
    }

    public static void setInstance(SegalGame game)
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

    public void setAppliedcosmetic(PlayerCosmetics appliedcosmetic)
    {
        this.appliedCosmetic = appliedcosmetic;
    }

    public void init()
    {
        Logger.log("Game init Called");
        try
        {
            gameObjects.clear();
            removeObjectsQueue.clear();
            addObjectsQueue.clear();
            System.gc();

            //Read the current playercosmetic
            Scanner in = new Scanner(file);
            //skip over cosmetic
            in.nextLine();
            this.coins = Integer.parseInt(in.nextLine());
            in.close();
            player = new Player(this.appliedCosmetic);
            background = new Background();

        } catch (IOException e)
        {
            Logger.log("Failed to load images.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void stop()
    {
        isRunning = false;
        if (backgroundMusic != null) backgroundMusic.stop();

        SoundManager.getSoundEffect("game_over").start();
        int coinsEarned = player.getCoinsEarned();
        Scanner in;
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
        gameRenderer.getGameFrame().dispose();
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.addWindowListener(new WindowListener()
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
                // TODO Auto-generated method stub
                gameRenderer.getFrame().setVisible(true);

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
        jframe.setVisible(true);
    }


    public Human createHuman()
    {
        Human human = new Human();
        humans.add(human);
        lastHumanSpawn = System.currentTimeMillis();
        return human;
    }

    public ArrayList<Human> getHumans()
    {
        return new ArrayList<>(humans);
    }

    public void removeHuman(Human human)
    {
        humans.remove(human);
    }

    public ArrayList<GameObject> getGameObjects()
    {
        return new ArrayList<>(gameObjects);
    }

    public void registerGameObject(GameObject gameObject)
    {
        Logger.log("Registering a game object");
        addObjectsQueue.add(gameObject);
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
        if (appliedCosmetic == PlayerCosmetics.FRENCH_SEAGULL)
        {
            Logger.log("Activating french mode");
            backgroundMusic = SoundManager.getSoundEffect("french_background");
        } else
        {
            Logger.log("Normal background mode");
            backgroundMusic = SoundManager.getSoundEffect("background");
        }
        FloatControl volume = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
        backgroundMusic.loop(-1);
        backgroundMusic.setFramePosition(0);
        float range = volume.getMinimum();
        float result = range * (1 - 70.0f / 100.0f);
        volume.setValue(result);
        backgroundMusic.start();

        new Thread(() ->
        {
            //move the player
            long lastTickTime;
            createHuman();
            while (isRunning)
            {
                lastTickTime = System.currentTimeMillis();
                for (GameObject gameObject : addObjectsQueue)
                {
                    if (gameObject.getRenderPriority() >= gameObjects.size()) gameObjects.add(gameObject);
                    else gameObjects.add(gameObject.getRenderPriority() - 1, gameObject);
                }
                addObjectsQueue.clear();

                //actual stuff to do
                for (GameObject gameObject : gameObjects)
                {
                    gameObject.tick();
                    if (gameObject.shouldRemove()) gameObject.remove();
                }

                //queue removing any game objects
                gameObjects.removeAll(removeObjectsQueue);
                removeObjectsQueue.clear();

                double time = (double) (System.currentTimeMillis() - lastHumanSpawn) / 1000.0;
                if (time >= Math.random() * 100 * HUMAN_MAX_SPAWN_TIME + HUMAN_MIN_SPAWN_TIME)
                {
                    Logger.log(String.valueOf(time));
                    createHuman();
                }

                //Tick calculations
                long timeElapsed = System.currentTimeMillis() - lastTickTime;
                if (timeElapsed > TICK_TIME)
                    Logger.warn("Is the main thread lagging? Thread took " + timeElapsed + "ms to successfully tick.");
                else
                {
                    try
                    {
                        Thread.sleep(TICK_TIME - timeElapsed);
                    } catch (InterruptedException e)
                    {
                        Logger.warn("Failed to sleep the main thread. Crashing with stacktrace");
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
            }
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

    public GameRenderer getGameRenderer()
    {
        return gameRenderer;
    }

    public void addCoins(int amount)
    {
        coins += amount;
    }

    public void removeCoins(int amount)
    {
        coins -= amount;
    }
}
