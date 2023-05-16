	package org.emeraldcraft.finalproject.pof;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setName("Seagull Swipe");
        frame.setSize(1920, 1000);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //frame.setUndecorated(true);
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        GameRenderer renderer = new GameRenderer(panel);
        panel.add(renderer);
        frame.getContentPane().add(panel);
        SegalGame game = SegalGame.getInstance();
        game.init();
        frame.addKeyListener(new KeyListener() {
            //Special type of list which can only hold unique values
            private final Set<Character> keys = new HashSet<>();

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keys.add(e.getKeyChar());
                handleKeys();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys.remove(e.getKeyChar());
            }

            public void handleKeys() {
                for (char key : keys) {
                    //Logger.log("Key Event Fired! Char = " + e.getKeyChar());
                    int x = 0, y = 0;
                    if (key == 'a') x -= 10;
                    else if (key == 'd') x += 10;
                    else if (key == 'w') y -= 10;
                    else if (key == 's') y += 10;
                    game.getPlayer().control(x, y);
                }
            }
        });
        renderer.start();
        frame.setVisible(true);
        new SegalCLI().start();
    }
}
