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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.emeraldcraft.finalproject.pof.gameobjects.player.PlayerCosemetic.PlayerCosemetics;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class CosemeticsMenu extends JComponent {
	private final HashMap<PlayerCosemetics, Image> cosemeticImages = new HashMap<>();
	private int selectedImage = 0;
	private Image currentImage;
	private JLabel jLabel = new JLabel("", SwingConstants.CENTER);
	
	
	//user info
	private int coins = -1;
	private ArrayList<PlayerCosemetics> unlockedCosemetics = new ArrayList<>();
	
	public CosemeticsMenu() {
		loadImages();
		
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
		jLabel.setIcon(new ImageIcon(currentImage));
		GridLayout layout = new GridLayout(6, 4);
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


        //Settings Button
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
				previousImage();
				
			}
		});

        add(back, new GridBagConstraints());
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
		jLabel.setIcon(new ImageIcon(currentImage));
		revalidate();
		repaint();
	}
	public void previousImage() {
		if(selectedImage - 1 < 0) selectedImage = cosemeticImages.size() - 1;
		else selectedImage--;
		currentImage = (Image) cosemeticImages.values().toArray()[selectedImage];
		jLabel.setIcon(new ImageIcon(currentImage));
		revalidate();
		repaint();
	}
	private int findIndex(PlayerCosemetics playerCosemetic) {
		Object[] cosemetic = cosemeticImages.keySet().toArray();
		for(int i = 0; i < cosemetic.length; i++) {
			if(cosemetic[i].equals(playerCosemetic)) return i;
		}
		return -1;
	}
	private void loadOwnedCosemetics() {
		
	}
}
