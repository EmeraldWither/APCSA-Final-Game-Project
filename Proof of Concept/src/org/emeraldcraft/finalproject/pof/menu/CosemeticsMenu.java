package org.emeraldcraft.finalproject.pof.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.emeraldcraft.finalproject.pof.SegalGame;
import org.emeraldcraft.finalproject.pof.gameobjects.player.PlayerCosemetic.PlayerCosemetics;
import org.emeraldcraft.finalproject.pof.utils.Logger;
import org.emeraldcraft.finalproject.pof.utils.SoundManager;

public class CosemeticsMenu extends JComponent {
	private final HashMap<PlayerCosemetics, Image> cosemeticImages = new HashMap<>();
	private int selectedImage = 0;
	private Image currentImage;
	private JLabel jLabel = new JLabel("", SwingConstants.CENTER);
	private JButton applyButton = new JButton("Equip");
	private JButton purchaseButton = new JButton("Purchase");
	private JLabel coinsLabel = new JLabel("Loading your wallet!", SwingConstants.CENTER);
	private JLabel costLabel = new JLabel("This cosemetic costs UNKNOWN coins!", SwingConstants.CENTER);
	//user info
	private int coins = -1;
	private int cosemeticCost = -1;
	private ArrayList<PlayerCosemetics> unlockedCosemetics = new ArrayList<>();
	
	
	private Clip purchaseSoundClip;
	private Clip clickSoundClip;
	private Clip applySoundClip;
	public CosemeticsMenu(){
		//Load sound
		purchaseSoundClip = SoundManager.getSoundEffect("purchase");
		clickSoundClip = SoundManager.getSoundEffect("click");
		applySoundClip = SoundManager.getSoundEffect("apply");

		loadImages();
		
		loadOwnedCosemetics();
		updateCoins();
		//load the config file
		File file = new File("cosemetic/.config");
		Scanner in;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e1) {
			Logger.warn("Could not find cosemetics config file. Crashing...");
			e1.printStackTrace();
			System.exit(-1);
			return;
		}
		
		selectedImage = findIndex(PlayerCosemetics.valueOf(in.nextLine()));
		currentImage = (Image) cosemeticImages.values().toArray()[selectedImage];
		jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
		GridLayout layout = new GridLayout(8, 5);
        setLayout(layout);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        Panel textPanel = new Panel();

        JLabel label = new JLabel("Cosemetics", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 64));
        
        
        add(label);
        add(jLabel);
        
        
        //add(textPanel, new GridBagConstraints());
        //Play Now Button
        JButton next = new JButton("Next");
        next.setFont(new Font("Arial", Font.BOLD, 32));
        next.setPreferredSize(new Dimension(100, 100));
        next.setAlignmentX(JButton.CENTER_ALIGNMENT);
        next.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
				clickSoundClip.setFramePosition(0);
				clickSoundClip.start();
            	nextImage();
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

        add(next, new GridBagConstraints() );


        //Back Button
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 32));
        back.setPreferredSize(new Dimension(100, 100));
        back.setAlignmentX(JButton.CENTER_ALIGNMENT);
        back.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				clickSoundClip.setFramePosition(0);
				clickSoundClip.start();
				previousImage();
				
			}
		});

        add(back, new GridBagConstraints());
        
        
        applyButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        applyButton.setFont(new Font("Arial", Font.BOLD, 32));
        applyButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Logger.log(getSelectedCosemetic() + " has been selected.");
				SegalGame.getInstance().setAppliedCosemetic(getSelectedCosemetic());
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
        purchaseButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(coins >= cosemeticCost) {
					removeCoins(cosemeticCost, getSelectedCosemetic());
					purchaseSoundClip.setFramePosition(0);
					purchaseSoundClip.start();
				}
				updateCoins();
				updateCost();
				loadOwnedCosemetics();
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
	private void loadImages() {
		Logger.log("Loading images");
		for(PlayerCosemetics cosemetics : PlayerCosemetics.values()) {
			if(cosemetics == PlayerCosemetics.NONE) continue;
			Logger.log("Loading cosemetic " + cosemetics.toString());
			//find that file
			File file = new File("cosemetic/" + cosemetics.toString().toLowerCase() + ".png");
			try {
				Image image = ImageIO.read(file);
				cosemeticImages.put(cosemetics, image);
			} catch (IOException e) {
				Logger.log("Unable to read");
				e.printStackTrace();
			}
		}
		Logger.log("Loaded " + cosemeticImages.size() + " images");
	}
	public PlayerCosemetics getSelectedCosemetic() {
		return (PlayerCosemetics) cosemeticImages.keySet().toArray()[selectedImage];
	}
	public void nextImage() {
		if(selectedImage + 1 >= cosemeticImages.size()) selectedImage = 0;
		else selectedImage++;
		currentImage = (Image) cosemeticImages.values().toArray()[selectedImage];
		jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
		updateButtonState();
		updateCost();
		revalidate();
		repaint();
	}
	public void previousImage() {
		if(selectedImage - 1 < 0) selectedImage = cosemeticImages.size() - 1;
		else selectedImage--;
		currentImage = (Image) cosemeticImages.values().toArray()[selectedImage];
		jLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
		updateButtonState();
		updateCost();
		revalidate();
		repaint();
	}
	private void removeCoins(int amount, PlayerCosemetics cosemetic) {
		if(findIndex(cosemetic) == -1) return;
		Scanner in;
		File file = new File("cosemetic/.config");
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			Logger.warn("Could not load owned cosemetics!");
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		
		//Rewrite the damn file
		in.nextLine();
		String fileContent = cosemetic.toString() + "\n";
		fileContent += (coins - amount) + "\n";
		in.nextLine();
		fileContent += in.nextLine() + "," + cosemetic.toString();
		Logger.log(fileContent);
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(fileContent);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			Logger.warn("Could not load owned cosemetics!");
			e.printStackTrace();
			System.exit(-1);
			return;
		}

	}
	private void updateCost() {
		//find the cosemetic file
		PlayerCosemetics cosemetic = (PlayerCosemetics) cosemeticImages.keySet().toArray()[selectedImage];
		File file = new File("cosemetic/" + cosemetic.toString().toLowerCase() + ".info");
		try {
			Scanner in = new Scanner(file);
			in.nextLine(); in.nextLine();
			//Get cost
			cosemeticCost = in.nextInt();
			costLabel.setText("This cosemetic costs " + cosemeticCost + " coins!");
			
			
		} catch (FileNotFoundException e) {
			Logger.warn("Unable to load cosemetic file!");
			e.printStackTrace();
			System.exit(-1);
			return;
		}
	}
	private void updateCoins() {
		Scanner in;
		try {
			in = new Scanner(new File("cosemetic/.config"));
		} catch (FileNotFoundException e) {
			Logger.warn("Could not load owned cosemetics!");
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		//Skip a line to get to coins
		in.nextLine();
		coins = in.nextInt();
		coinsLabel.setText("You have " + coins + " coins!");
	}
	private void updateButtonState() {
		Logger.log("Checking to see if we own cosemetic: " + (ownsCosemetic((PlayerCosemetics) cosemeticImages.keySet().toArray()[selectedImage])));
		applyButton.setVisible(ownsCosemetic((PlayerCosemetics) cosemeticImages.keySet().toArray()[selectedImage]));
		purchaseButton.setVisible(!ownsCosemetic((PlayerCosemetics) cosemeticImages.keySet().toArray()[selectedImage]));
		applyButton.setEnabled(SegalGame.getInstance().getAppliedCosemetic() != getSelectedCosemetic());
		purchaseButton.setEnabled(coins >= cosemeticCost);
	}
	private int findIndex(PlayerCosemetics playerCosemetic) {
		Object[] cosemetic = cosemeticImages.keySet().toArray();
		for(int i = 0; i < cosemetic.length; i++) {
			if(cosemetic[i].equals(playerCosemetic)) return i;
		}
		return -1;
	}
	private boolean ownsCosemetic(PlayerCosemetics cosemetic) {
		return unlockedCosemetics.contains(cosemetic);
	}
	private void loadOwnedCosemetics() {
		unlockedCosemetics.clear();
		//loop over the array 
		Scanner in;
		try {
			in = new Scanner(new File("cosemetic/.config"));
		} catch (FileNotFoundException e) {
			Logger.warn("Could not load owned cosemetics!");
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		//Skip 2 lines to get to the owned cosemetics
		in.nextLine();
		in.nextLine();
		
		String cosemeticsStr = in.nextLine();
		String[] cosemetics = cosemeticsStr.split(",");
		for(String str : cosemetics) {
			unlockedCosemetics.add(PlayerCosemetics.valueOf(str));
		}
		Logger.log("Loaded unlocked cosemetics " + unlockedCosemetics);
	}
}
