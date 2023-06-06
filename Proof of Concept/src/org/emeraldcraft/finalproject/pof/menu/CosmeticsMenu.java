/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.menu;

import org.emeraldcraft.finalproject.pof.SegalGame;
import org.emeraldcraft.finalproject.pof.gameobjects.player.PlayerCosmetic.PlayerCosmetics;
import org.emeraldcraft.finalproject.pof.utils.Logger;
import org.emeraldcraft.finalproject.pof.utils.SoundManager;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
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

public class CosmeticsMenu extends JComponent
{
    private final HashMap<PlayerCosmetics, Image> cosmeticImages = new HashMap<>();
    private final JLabel jLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton applyButton = new JButton("Equip");
    private final JButton purchaseButton = new JButton("Purchase");
    private final JLabel coinsLabel = new JLabel("Loading your wallet!", SwingConstants.CENTER);
    private final JLabel costLabel = new JLabel("This cosmetic costs UNKNOWN coins!", SwingConstants.CENTER);
    private final ArrayList<PlayerCosmetics> unlockedcosmetics = new ArrayList<>();
    private final Clip purchaseSoundClip;
    private final Clip clickSoundClip;
    private final Clip applySoundClip;
    private int selectedImage = 0;
    private Image currentImage;
    //user info
    private int coins = -1;
    private int cosmeticCost = -1;

    public CosmeticsMenu()
    {
        //Load sound
        purchaseSoundClip = SoundManager.getSoundEffect("purchase");
        clickSoundClip = SoundManager.getSoundEffect("click");
        applySoundClip = SoundManager.getSoundEffect("apply");

        loadImages();

        loadOwnedcosmetics();
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

        selectedImage = findIndex(PlayerCosmetics.valueOf(in.nextLine()));
        currentImage = (Image) cosmeticImages.values().toArray()[selectedImage];
        jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(-1, 70, Image.SCALE_SMOOTH)));
        GridLayout layout = new GridLayout(8, 5);
        setLayout(layout);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        Panel textPanel = new Panel();

        JLabel label = new JLabel("Cosmetics", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 64));


        add(label);
        add(jLabel);


        //add(textPanel, new GridBagConstraints());
        //Play Now Button
        JButton next = new JButton("Next");
        next.setFont(new Font("Arial", Font.BOLD, 32));
        next.setPreferredSize(new Dimension(100, 100));
        next.setAlignmentX(JButton.CENTER_ALIGNMENT);
        next.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                clickSoundClip.setFramePosition(0);
                clickSoundClip.start();
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
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                clickSoundClip.setFramePosition(0);
                clickSoundClip.start();
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
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                Logger.log(getSelectedCosmetic() + " has been selected.");
                SegalGame.getInstance().setAppliedcosmetic(getSelectedCosmetic());
                applySoundClip.setFramePosition(0);
                applySoundClip.start();
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
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                if (coins >= cosmeticCost)
                {
                    removeCoins(cosmeticCost, getSelectedCosmetic());
                    purchaseSoundClip.setFramePosition(0);
                    purchaseSoundClip.start();
                }
                updateCoins();
                updateCost();
                loadOwnedcosmetics();
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
        updateButtonState();
        updateCost();
    }

    private void loadImages()
    {
        Logger.log("Loading images");
        for (PlayerCosmetics cosmetics : PlayerCosmetics.values())
        {
            if (cosmetics == PlayerCosmetics.NONE) continue;
            Logger.log("Loading cosmetic " + cosmetics.toString());
            //find that file
            File file = new File("cosmetic/" + cosmetics.toString().toLowerCase() + ".png");
            try
            {
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
        return (PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage];
    }

    public void nextImage()
    {
        if (selectedImage + 1 >= cosmeticImages.size()) selectedImage = 0;
        else selectedImage++;
        currentImage = (Image) cosmeticImages.values().toArray()[selectedImage];
        jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(-1, 70, Image.SCALE_SMOOTH)));
        updateButtonState();
        updateCost();
        revalidate();
        repaint();
    }

    public void previousImage()
    {
        if (selectedImage - 1 < 0) selectedImage = cosmeticImages.size() - 1;
        else selectedImage--;
        currentImage = (Image) cosmeticImages.values().toArray()[selectedImage];
        jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(-1, 70, Image.SCALE_SMOOTH)));
        updateButtonState();
        updateCost();
        revalidate();
        repaint();
    }

    private void removeCoins(int amount, PlayerCosmetics cosmetic)
    {
        if (findIndex(cosmetic) == -1) return;
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
        in.nextLine();
        String fileContent = cosmetic.toString() + "\n";
        fileContent += (coins - amount) + "\n";
        in.nextLine();
        fileContent += in.nextLine() + "," + cosmetic;
        Logger.log(fileContent);
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.flush();
            writer.close();
        } catch (IOException e)
        {
            Logger.warn("Could not load owned cosmetics!");
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public void writeCurrentCosmetic()
    {
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
        in.nextLine();
        String fileContent = getSelectedCosmetic().toString() + "\n";
        fileContent += in.nextInt() + "\n";
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
            Logger.warn("Could not load owned cosmetics!");
            e.printStackTrace();
            System.exit(-1);
        }

    }

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

    private void updateCoins()
    {
        Scanner in;
        try
        {
            in = new Scanner(new File("cosmetic/.config"));
        } catch (FileNotFoundException e)
        {
            Logger.warn("Could not load owned cosmetics!");
            e.printStackTrace();
            System.exit(-1);
            return;
        }
        //Skip a line to get to coins
        in.nextLine();
        coins = in.nextInt();
        coinsLabel.setText("You have " + coins + " coins!");
    }

    private void updateButtonState()
    {
        Logger.log("Checking to see if we own cosmetic: " + (ownscosmetic((PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage])));
        applyButton.setVisible(ownscosmetic((PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage]));
        purchaseButton.setVisible(!ownscosmetic((PlayerCosmetics) cosmeticImages.keySet().toArray()[selectedImage]));
        applyButton.setEnabled(SegalGame.getInstance().getAppliedCosmetic() != getSelectedCosmetic());
        purchaseButton.setEnabled(coins >= cosmeticCost);
    }

    private int findIndex(PlayerCosmetics playercosmetic)
    {
        Object[] cosmetic = cosmeticImages.keySet().toArray();
        for (int i = 0; i < cosmetic.length; i++)
        {
            if (cosmetic[i].equals(playercosmetic)) return i;
        }
        return -1;
    }

    private boolean ownscosmetic(PlayerCosmetics cosmetic)
    {
        return unlockedcosmetics.contains(cosmetic);
    }

    private void loadOwnedcosmetics()
    {
        unlockedcosmetics.clear();
        //loop over the array
        Scanner in;
        try
        {
            in = new Scanner(new File("cosmetic/.config"));
        } catch (FileNotFoundException e)
        {
            Logger.warn("Could not load owned cosmetics!");
            e.printStackTrace();
            System.exit(-1);
            return;
        }
        //Skip 2 lines to get to the owned cosmetics
        in.nextLine();
        in.nextLine();

        String cosmeticsStr = in.nextLine();
        String[] cosmetics = cosmeticsStr.split(",");
        for (String str : cosmetics)
        {
            unlockedcosmetics.add(PlayerCosmetics.valueOf(str));
        }
        Logger.log("Loaded unlocked cosmetics " + unlockedcosmetics);
    }
}
