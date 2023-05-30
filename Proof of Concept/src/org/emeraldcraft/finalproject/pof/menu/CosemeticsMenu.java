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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.emeraldcraft.finalproject.pof.gameobjects.player.PlayerCosemetic.PlayerCosemetics;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class CosemeticsMenu extends JComponent {
	private final ArrayList<Image> cosemeticImages = new ArrayList<>();
	public CosemeticsMenu() {
		loadImages();
		GridLayout layout = new GridLayout(6, 4);
        setLayout(layout);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        Panel textPanel = new Panel();

        JLabel label = new JLabel("Cosemetics", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 64));
        
        add(label);
        
        //add(textPanel, new GridBagConstraints());
        //Play Now Button
        JButton playNow = new JButton("cosemetics menu");
        playNow.setFont(new Font("Arial", Font.BOLD, 32));
        playNow.setPreferredSize(new Dimension(100, 100));
        playNow.setAlignmentX(JButton.CENTER_ALIGNMENT);
        playNow.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	
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

        add(playNow, new GridBagConstraints() );


        //Settings Button
        JButton settings = new JButton("Settings");
        settings.setFont(new Font("Arial", Font.BOLD, 32));
        settings.setPreferredSize(new Dimension(100, 100));
        settings.setAlignmentX(JButton.CENTER_ALIGNMENT);

        add(settings, new GridBagConstraints());
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

}
