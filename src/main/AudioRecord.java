/*
 * Tutorial followed: https://youtu.be/WSyTrdjKeqQ
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class AudioRecord {
    private AudioFormat audioFormat;
    private TargetDataLine targetLine;
    private final String PATH_NAME = "Test-files/question.wav";
    
    /**
     * Start recording and create audio file with audio
     */
    public void startRecording() {
        try {
            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            // handle info regarding the data line
            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(dataInfo)) {
                System.out.println("Not Supported");
            }

            // ready mic to start recording
            targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetLine.open();
            targetLine.start();

            // multi-thread to allow other user input while recording
            Thread audioRecorderThread = new Thread() {
                @Override
                // record and create file called question.wav
                public void run() {
                    AudioInputStream recordingStream = new AudioInputStream(targetLine);
                    File outputFile = new File(PATH_NAME);
                    try {
                        AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    System.out.println("Stopped Recording");
                }
            };
            audioRecorderThread.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /** 
     * Stop recording and finalize audio file 
     */
    public void stopRecording() {
        targetLine.stop();
        targetLine.close();
    }
}
