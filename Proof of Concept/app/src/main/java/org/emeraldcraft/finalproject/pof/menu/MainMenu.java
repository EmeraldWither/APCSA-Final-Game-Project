/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The JComponent representing the menu of the game
 */
public class MainMenu extends JComponent
{

    public MainMenu(Runnable onGameStart, Runnable onCosmeticsShow)
    {
        //Uses basically the same code as the cosmetics menu
        //A bit rougher, but it works and I ain't touching it

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
        playNow.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                //Trigger the runnable to start the game in GameRenderer
                onGameStart.run();
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

        add(playNow, new GridBagConstraints());


        //Cosmetics Button
        JButton cosmetics = new JButton("Cosmetics");
        cosmetics.addMouseListener(new MouseListener()
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
                //Trigger the runnable to open the cosmetics menu
                onCosmeticsShow.run();
            }
        });
        cosmetics.setFont(new Font("Arial", Font.BOLD, 32));
        cosmetics.setPreferredSize(new Dimension(100, 100));
        cosmetics.setAlignmentX(JButton.CENTER_ALIGNMENT);

        add(cosmetics, new GridBagConstraints());
    }

}
