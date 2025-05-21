package project.ball;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import project.Application;

public class BallAudioUtil {
    
    public static float clipVolume;

    private static final String BALL_HIT_BORDER_PATH = "/sound/ball_hitting_border.wav";
    private static final String BALL_IN_HOLE_PATH = "/sound/ball_in_hole.wav";

    public static Clip getBallHitBorderClip() {
        return getClip(BALL_HIT_BORDER_PATH);
    }

    public static Clip getBallInHoleClip() {
        return getClip(BALL_IN_HOLE_PATH);
    }

    private static Clip getClip(String path) {
        try {
            AudioInputStream copyStream = getAudioInputStream(path);

            Clip clip = AudioSystem.getClip();
            clip.open(copyStream);

            Clip copy = AudioSystem.getClip();
            copy.open(getAudioInputStream(path));

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            float gain = min + (max - min) * clipVolume;
            gainControl.setValue(gain);

            return clip;
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static AudioInputStream getAudioInputStream(String path) {
        try {
            URL soundURL = Application.class.getResource(path);
            return AudioSystem.getAudioInputStream(soundURL);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
