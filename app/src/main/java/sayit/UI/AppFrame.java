package sayit.UI;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;

import sayit.Server.BusinessLogic.Prompt;

/**
 * Put JPanels on AppFrame and make listeners for buttons
 */
public class AppFrame extends JFrame {
    // put all JPanels here
    private ScrollFrame scrollFrame;
    private Footer footer;

    // put all buttons used in app here
    private JButton newButton;
    private JButton clearButton;
    private JButton clearSelectedButton;

    // other miscellaneous variables
    boolean isRecording;
    Color black = new Color(0, 0, 0);
    Color red = new Color(255, 0, 0);
    Color pink = new Color(227, 179, 171);
    public final String newQURL = "http://localhost:8100/newQuestion";
    public final String clearAURL = "http://localhost:8100/clearAll";
    public final String deletePURL = "http://localhost:8100/deletePrompt";

    /**
     * Call all other necessary classes and setup AppFrame
     */
    AppFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SayIt");
        setSize(400, 600);

        // Set scrollFrame, footer, and isRecording
        scrollFrame = new ScrollFrame();
        footer = new Footer();
        isRecording = false;

        // Make the main part of the frame the scrollFrame
        this.add(scrollFrame.getContentPane(), BorderLayout.CENTER);
        // Add footer on bottom of the screen
        this.add(footer, BorderLayout.SOUTH);
        this.setBackground(pink);

        setVisible(true);

        // make functionality for buttons
        newButton = footer.getNewButton();
        clearButton = footer.getClearButton();
        clearSelectedButton = footer.getClearSelectedButton();
        addListeners();
    }

    /**
     * Create functionality for when the new and clear buttons are pressed
     */
    public void addListeners() {
        newButton.addActionListener( 
            (ActionEvent e)  -> {
                /* if not recording, request server to begin creating the wav file on its side */
                /* if is recording, request server to finalize wav file, input it into whisper, save the Q&A into its prompts, then return Q&A back in response string */
                String response;
                if (!isRecording) {
                    try {
                        // Some UI things
                        newButton.setText("Stop Recording");
                        newButton.setForeground(red);
                        
                        // create URL (with query) to the server and create the connection
                        String query = "Start";
                        URL url = new URL(newQURL + "?=" + query);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        
                        // request the GET method on the server
                        conn.setRequestMethod("GET");

                        // won't call the handler correctly unless I do this?
                        BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                        );
                        in.readLine();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("AppFrame: " + ex);
                    }
                } else {
                    try {
                        // some UI things
                        newButton.setText("New Question");
                        newButton.setForeground(black);
                        
                        // create URL (with query) to the server and create the connection
                        String query = "Stop";
                        URL url = new URL(newQURL + "?=" + query);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        
                        // request the GET method on the server
                        conn.setRequestMethod("GET");

                        // parse and add Q&A from response to scrollFrame
                        BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                        );
                        response = in.readLine();
                        String question = response.substring(0,response.indexOf("/D\\"));
                        String answer = response.substring(response.indexOf("/D\\") + 3);
                        Prompt prompt = new Prompt(question, answer);
                        scrollFrame.addPrompt(prompt);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("AppFrame: " + ex);
                    }
                }
                isRecording = !isRecording;
            });

        // delete all prompts in prompt history when clear button is pressed
        clearButton.addActionListener(
            (ActionEvent e) -> {
                /* clear the scrollFrame */
                scrollFrame.clearAllPrompts();

                /* request server to clear all its prompts */
                try {
                    // create URL to the server and create the connection
                    URL url = new URL(clearAURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // request the DELETE method on the server
                    conn.setRequestMethod("DELETE");

                    // won't call the handler correctly unless I do this?
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                    );
                    in.readLine();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("AppFrame: " + ex);
                }
            });

        clearSelectedButton.addActionListener((ActionEvent e) -> {
            /* delete selected prompts on scrollFrame */
            ArrayList<Integer> indices = scrollFrame.removeSelectedPrompts();
            repaint();

            /* request server to delete its prompts at these indices */
            try {
                for (int i: indices) {
                    // create URL (with query) to the server and create the connection
                    String query = String.valueOf(i);
                    URL url = new URL(deletePURL + "?=" + query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // request the DELETE method on the server
                    conn.setRequestMethod("DELETE");

                    // won't call the handler correctly unless I do this?
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                    );
                    in.readLine();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("AppFrame: " + ex);
            }
        });
    }
}