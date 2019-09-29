package Yonatan;


import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * @author Yuval
 * credit to Yuval from the car project(he wrote this class)
 */
public class SoundPlayer {
    public static void playSound(File soundFile) {
        AudioInputStream audioStream = null;
        AudioFormat audioFormat;
        SourceDataLine sourceLine = null;

        if (soundFile == null)
            return;

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);

            audioFormat = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);

            sourceLine.start();

            int nBytesRead = 0;
            byte[] abData = new byte[1024];
            while (nBytesRead != -1) {
                nBytesRead = audioStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    sourceLine.write(abData, 0, nBytesRead);
            }
            sourceLine.drain();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try { audioStream.close();}  catch (Exception e) { }
            try { sourceLine.close(); } catch (Exception e) { }
        }
    }
}

