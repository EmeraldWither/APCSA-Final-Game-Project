/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setName("Seagull Swipe Game Launcher");
        frame.setSize(1920 / 2, 1080 / 2);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        GameRenderer renderer = new GameRenderer(panel, frame);
        SegallGame game = new SegallGame(renderer);
        SegallGame.setInstance(game);
        panel.add(renderer);
        frame.getContentPane().add(panel);
        renderer.start();
        new SegalCLI().start();
    }
}
