package org.emeraldcraft.finalproject.pof.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class MainMenu extends JComponent {

	public MainMenu(Runnable onGameStart, Runnable oncosmeticsShow) {
		GridLayout layout = new GridLayout(5, 3);
        setLayout(layout);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        Panel textPanel = new Panel();

        JLabel label = new JLabel("Sandwich Swipe");
        label.setFont(new Font("Arial", Font.BOLD, 64));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);


        textPanel.add(label, new GridBagConstraints());
        add(textPanel);
        //Play Now Button
        JButton playNow = new JButton("Play");
        playNow.setFont(new Font("Arial", Font.BOLD, 32));
        playNow.setPreferredSize(new Dimension(100, 100));
        playNow.setAlignmentX(JButton.CENTER_ALIGNMENT);
        playNow.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	onGameStart.run();
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
        JButton cosmetics = new JButton("Cosmetics");
        cosmetics.addMouseListener(new MouseListener() {
			
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
				oncosmeticsShow.run();
			}
		});
        cosmetics.setFont(new Font("Arial", Font.BOLD, 32));
        cosmetics.setPreferredSize(new Dimension(100, 100));
        cosmetics.setAlignmentX(JButton.CENTER_ALIGNMENT);

        add(cosmetics, new GridBagConstraints());
	}

}
