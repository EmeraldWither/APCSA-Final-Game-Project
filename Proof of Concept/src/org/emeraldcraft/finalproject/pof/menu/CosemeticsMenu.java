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
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.emeraldcraft.finalproject.pof.gameobjects.player.PlayerCosemetic.PlayerCosemetics;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class CosemeticsMenu extends JComponent {
	private final ArrayList<Image> cosemeticImages = new ArrayList<>();
	private int selectedImage = 0;
	private Image currentImage;
	private JLabel jLabel = new JLabel("", SwingConstants.CENTER);
	public CosemeticsMenu() {
		loadImages();
		currentImage = cosemeticImages.get(0);
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
				cosemeticImages.add(image);
			} catch (IOException e) {
				Logger.log("Unable to read");
				e.printStackTrace();
			}
		}
		Logger.log("Loaded " + cosemeticImages.size() + " images");
	}
	public void nextImage() {
		if(selectedImage + 1 >= cosemeticImages.size()) selectedImage = 0;
		else selectedImage++;
		currentImage = cosemeticImages.get(selectedImage);
		jLabel.setIcon(new ImageIcon(currentImage));
		revalidate();
		repaint();
	}
	public void previousImage() {
		if(selectedImage - 1 < 0) selectedImage = cosemeticImages.size() - 1;
		else selectedImage--;
		currentImage = cosemeticImages.get(selectedImage);
		jLabel.setIcon(new ImageIcon(currentImage));
		revalidate();
		repaint();
	}

}
