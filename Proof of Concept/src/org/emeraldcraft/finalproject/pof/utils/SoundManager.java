/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof.utils;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager
{
    public static Clip getSoundEffect(String name)
    {
        try
        {
            String location = "assets/sounds/" + name + ".wav";
            Clip clip = AudioSystem.getClip();

            clip.open(AudioSystem.getAudioInputStream(new File(location)));
            return clip;
        } catch (Exception e)
        {
            Logger.warn("An error was thrown when trying to create sound effect for " + name + "!");
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
}
