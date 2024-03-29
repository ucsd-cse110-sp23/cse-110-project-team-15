package sayit.Server.BusinessLogic;

import java.io.*;
import javax.sound.sampled.*;

/**
 * Responsible for audio-based functionality
 */
public class AudioRecord implements IAudioRecord{
    // Objects needed for AudioRecord
    private AudioFormat audioFormat;
    private TargetDataLine targetLine;
    private final String PATH_NAME = "src/main/java/sayit/Server/BusinessLogic/Test-files/question.wav";
    
    /**
     * Start recording and create audio file with input audio
     */
    public void startRecording() {
        try {
            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);

            // handle info regarding the data line
            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(dataInfo)) {
                System.out.println("AudioRecord.java: Not Supported");
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
                        // System.out.println("Started Recording");
                        AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                    } catch (IOException e) {
                        System.out.println("AudioRecord.java: " + e);
                    }
                    // System.out.println("Stopped Recording");
                }
            };

            // start the recording audio thread and move on
            audioRecorderThread.start();
        } catch (Exception e) {
            System.out.println("AudioRecord.java: " + e);
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