/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.menu;

import org.emeraldcraft.finalproject.pof.SegallGame;
import org.emeraldcraft.finalproject.pof.gameobjects.player.PlayerCosmetic.PlayerCosmetics;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * JComponent representing the Cosmetics Menu
 */
public class CosmeticsMenu extends JComponent
{
    private final HashMap<PlayerCosmetics, Image> cosmeticImages = new HashMap<>();
    private final JLabel jLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton applyButton = new JButton("Equip");
    private final JButton purchaseButton = new JButton("Purchase");
    private final JLabel coinsLabel = new JLabel("Loading your wallet!", SwingConstants.CENTER);
    private final JLabel costLabel = new JLabel("This cosmetic costs UNKNOWN coins!", SwingConstants.CENTER);

    //Keep track of which ones we have unlocked
    private final ArrayList<PlayerCosmetics> unlockedCosmetics = new ArrayList<>();

    //Sound
    private int selectedImage = 0;
    private Image currentImage;


    //user info
    private int coins = -1;
    private int cosmeticCost = -1;

    public CosmeticsMenu()
    {
        //Load sound

        loadImages();

        loadOwnedCosmetics();
        updateCoins();
        //load the config file
        File file = new File("cosmetic/.config");
        Scanner in;
        try
        {
            in = new Scanner(file);
        } catch (FileNotFoundException e1)
        {
            Logger.warn("Could not find cosmetics config file. Crashing...");
            e1.printStackTrace();
            System.exit(-1);
            return;
        }

        //Get the selected cosmetic from last time and it s image
        selectedImage = findIndex(PlayerCosmetics.valueOf(in.nextLine()));
        currentImage = (Image) cosmeticImages.values().toArray()[selectedImage];
        //Scale it so it fits
        jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(-1, 70, Image.SCALE_SMOOTH)));

        GridLayout layout = new GridLayout(8, 5);
        setLayout(layout);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        //Create all of our buttons and titles
        JLabel label = new JLabel("Cosmetics", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 64));


        add(label);
        add(jLabel);


        //Next Cosmetic  Button
        JButton next = new JButton("Next");
        next.setFont(new Font("Arial", Font.BOLD, 32));
        next.setPreferredSize(new Dimension(100, 100));
        next.setAlignmentX(JButton.CENTER_ALIGNMENT);
        next.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                nextImage();
            }

            @Override
            public void mousePressed(MouseEvent e)
            {

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {

            }

            @Override
            public void mouseEntered(MouseEvent e)
            {

            }

            @Override
            public void mouseExited(MouseEvent e)
            {

            }
        });

        add(next, new GridBagConstraints());


        //Back Button
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 32));
        back.setPreferredSize(new Dimension(100, 100));
        back.setAlignmentX(JButton.CENTER_ALIGNMENT);
        back.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseReleased(MouseEvent arg0)
            {
            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {

            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {

            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {

            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                previousImage();
            }
        });

        add(back, new GridBagConstraints());


        applyButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        applyButton.setFont(new Font("Arial", Font.BOLD, 32));
        applyButton.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseReleased(MouseEvent arg0)
            {
            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                //Set our applied cosmetic, and update all the buttons to reflect this new change
                Logger.log(getSelectedCosmetic() + " has been selected.");
                SegallGame.getInstance().setAppliedCosmetic(getSelectedCosmetic());
                updateButtonState();
                revalidate();
                repaint();
            }
        });
        add(applyButton);

        purchaseButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        purchaseButton.setFont(new Font("Arial", Font.BOLD, 32));
        purchaseButton.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseReleased(MouseEvent arg0)
            {
            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                //make sure we have enough coins
                if (coins >= cosmeticCost)
                {
                    //if we do, purchase
                    purchaseCosmetic(cosmeticCost, getSelectedCosmetic());
                }
                //update all the info from the files
                updateCoins();
                updateCost();
                loadOwnedCosmetics();
                updateButtonState();
                revalidate();
                repaint();
            }
        });
        add(purchaseButton);

        coinsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(coinsLabel);

        costLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(costLabel);

        //finally, update everything
        updateButtonState();
        updateCost();
    }

    /**
     * Loads all the images from the .png files of all the cosmetics
     */
    private void loadImages()
    {
        Logger.log("Loading images");
        for (PlayerCosmetics cosmetics : PlayerCosmetics.values())
        {
            //Skip the none cosmetic
            if (cosmetics == PlayerCosmetics.NONE) continue;
            Logger.log("Loading cosmetic " + cosmetics.toString());
            //find that file
            File file = new File("cosmetic/" + cosmetics.toString().toLowerCase() + ".png");
            try
            {
                //read that image and put it in the hashmap
                Image image = ImageIO.read(file);
                cosmeticImages.put(cosmetics, image);
            } catch (IOException e)
            {
                Logger.log("Unable to read");
                e.printStackTrace();
            }
        }
        Logger.log("Loaded " + cosmeticImages.size() + " images");
    }

    public PlayerCosmetics getSelectedCosmetic()
    {
        //finds the cosmetic by looking at the index
        return (PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage];
    }

    public void nextImage()
    {
        //increment, or bring it back to 0 to fully cycle it
        if (selectedImage + 1 >= cosmeticImages.size()) selectedImage = 0;
        else selectedImage++;

        //update the image and all the button states
        currentImage = (Image) cosmeticImages.values().toArray()[selectedImage];
        jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(-1, 70, Image.SCALE_SMOOTH)));
        updateCost();
        updateButtonState();
        revalidate();
        repaint();
    }

    public void previousImage()
    {
        //decrement or bring it back to max value to cycle it
        if (selectedImage - 1 < 0) selectedImage = cosmeticImages.size() - 1;
        else selectedImage--;

        //update images and button states
        currentImage = (Image) cosmeticImages.values().toArray()[selectedImage];
        jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(-1, 70, Image.SCALE_SMOOTH)));
        updateCost();
        updateButtonState();
        revalidate();
        repaint();
    }

    private void purchaseCosmetic(int amount, PlayerCosmetics cosmetic)
    {
        if (findIndex(cosmetic) == -1) return;
        //make sure that the cosmetic exists
        File file = new File("cosmetic/.config");
        //Rewrite the damn file
        Scanner in = getFileScanner(file);
        in.nextLine(); //skip the selected one, as we will set that ourselves
        String fileContent = cosmetic.toString() + "\n";
        fileContent += (coins - amount) + "\n"; //subtract the coin value
        in.nextLine(); //skip the coin value in the file
        fileContent += in.nextLine() + "," + cosmetic; //add our new cosmetic to the owned list
        writeToFile(file, fileContent); //write and flush it to the disk
    }

    /**
     * Creates a scanner based on the file.
     *
     * @param file The file needed
     * @return Creates a scanner which handles exception checking
     */
    private Scanner getFileScanner(File file)
    {
        Scanner in;
        try
        {
            in = new Scanner(file);
        } catch (FileNotFoundException e)
        {
            Logger.warn("Could not create a file scanner!");
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
        return in;

    }

    /**
     * Writes to a file and then flushes the values to the disk.
     *
     * @param file        The file to write to
     * @param fileContent The content
     */
    private void writeToFile(File file, String fileContent)
    {
        Logger.log(fileContent);
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.flush();
            writer.close();
        } catch (IOException e)
        {
            Logger.warn("Could not write to the file " + file.getAbsolutePath() + "!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Update which is the currently selected cosmetic manually
     */
    public void writeCurrentCosmetic()
    {
        File file = new File("cosmetic/.config");
        Scanner in = getFileScanner(file);

        //Rewrite the damn file, uses the same formula as #purchaseCosmetic()
        in.nextLine();
        String fileContent = getSelectedCosmetic().toString() + "\n";
        fileContent += in.nextInt() + "\n";
        in.nextLine();
        fileContent += in.nextLine();
        writeToFile(file, fileContent);
    }

    /**
     * Updates the cost label of the current cosmetic
     */
    private void updateCost()
    {
        //find the cosmetic file
        PlayerCosmetics cosmetic = (PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage];
        File file = new File("cosmetic/" + cosmetic.toString().toLowerCase() + ".info");
        try
        {
            Scanner in = new Scanner(file);
            in.nextLine();
            in.nextLine();
            //Get cost
            cosmeticCost = in.nextInt();
            costLabel.setText("This cosmetic costs " + cosmeticCost + " coins!");


        } catch (FileNotFoundException e)
        {
            Logger.warn("Unable to load cosmetic file!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Updates the current coin count of the user
     */
    private void updateCoins()
    {
        Scanner in = getFileScanner(new File("cosmetic/.config"));
        //Skip a line to get to coins
        in.nextLine();
        coins = in.nextInt();
        coinsLabel.setText("You have " + coins + " coins!");
    }

    /**
     * Update all the buttons to reflect what is enabled/shown
     */
    private void updateButtonState()
    {
        Logger.log("Checking to see if we own cosmetic: " + (ownsCosmetic((PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage])));
        applyButton.setVisible(ownsCosmetic((PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage]));
        purchaseButton.setVisible(!ownsCosmetic((PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage]));
        applyButton.setEnabled(SegallGame.getInstance().getAppliedCosmetic() != getSelectedCosmetic());
        purchaseButton.setEnabled(coins >= cosmeticCost);
    }

    /**
     * @param playerCosmetics The cosmetic
     * @return The index that the cosmetic is located at
     */
    private int findIndex(PlayerCosmetics playerCosmetics)
    {
        Object[] cosmetic = cosmeticImages.keySet().toArray();
        for (int i = 0; i < cosmetic.length; i++)
        {
            if (cosmetic[i].equals(playerCosmetics)) return i;
        }
        return -1;
    }

    private boolean ownsCosmetic(PlayerCosmetics cosmetic)
    {
        return unlockedCosmetics.contains(cosmetic);
    }

    private void loadOwnedCosmetics()
    {
        //clear the list to start a-new
        unlockedCosmetics.clear();
        //loop over the array
        Scanner in = getFileScanner(new File("cosmetic/.config"));
        //Skip 2 lines to get to the owned cosmetics
        in.nextLine();
        in.nextLine();

        String cosmeticsStr = in.nextLine();
        String[] cosmetics = cosmeticsStr.split(",");
        //Parse at the , so it turns into an array
        /*
        FORMAT:
          HAT1,HAT2,HAT3,HAT4

        */
        for (String str : cosmetics)
        {
            unlockedCosmetics.add(PlayerCosmetics.valueOf(str));
        }
        Logger.log("Loaded unlocked cosmetics " + unlockedCosmetics);
    }
}
