import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
public class Music {
    AudioInputStream audioInput;
    Clip clip;
    void playMusic(String path,boolean isloop) {
        try {
            File musicPath = new File(path);
            if (musicPath.exists()) {
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip=AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                if(isloop)
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    void stop(){
        clip.close();
    }
}