/*
 * Tutorial followed: https://youtu.be/WSyTrdjKeqQ
 */

 import java.awt.*;
 import java.awt.event.*;
 import java.io.*;
 import javax.sound.sampled.*;
 import javax.swing.*;
 
 public class AudioRecord {
     private boolean isRecording; // tell whether the app is already recording or not
     
     AudioRecord() {
         isRecording = false;
     }
 
     /**
      * Start recording and create audio file with audio
      */
     public void startRecording() {
         try {  
             if (isRecording) {
                 // if the app is already recording, then stop it
                 stopRecording();
                 return;
             } else {
                 // otherwise start recording
                 isRecording = true;
                 AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
 
                 // handle info regarding the data line
                 DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                 if (!AudioSystem.isLineSupported(dataInfo)) {
                     System.out.println("Not Supported");
                 }
 
                 // ready mic to start recording
                 TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(dataInfo);
                 targetLine.open();
 
                 // press to start recording
                 JOptionPane.showMessageDialog(null, "Press to Start Recording");
                 targetLine.start();
 
                 // multi-thread to allow other user input while recording
                 Thread audioRecorderThread = new Thread() {
                     @Override 
                     // record and create file called question.wav
                     public void run() {
                         AudioInputStream recordingStream = new AudioInputStream(targetLine);
                         File outputFile = new File("Test-files/question.wav");
                         try {
                             AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                         } catch (IOException e) {
                             System.out.println(e);
                         }
                         System.out.println("Stopped Recording");
                     }
                 };
                 audioRecorderThread.start();
 
                 // stop recording when 
                 JOptionPane.showMessageDialog(null, "Press to Stop Recording");
                 targetLine.stop();
                 targetLine.close();
             }
         } catch (Exception e) {
             System.out.println(e);
         }
     }
 
     public void stopRecording() {
         try {
             isRecording = false;   
         } catch (Exception e) {
             System.out.println(e);
         }
     }
 
     public static void main(String[] args) {
         AudioRecord obj = new AudioRecord();
         obj.startRecording();
     }
 }
 