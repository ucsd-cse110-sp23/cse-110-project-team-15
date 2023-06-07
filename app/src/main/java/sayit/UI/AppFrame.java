package sayit.UI;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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
    private JButton startButton;

    // other miscellaneous variables
    boolean isRecording;
    Color black = new Color(0, 0, 0);
    Color red = new Color(255, 0, 0);
    Color pink = new Color(227, 179, 171);
    private final String indexURL = "http://localhost:8100/index";
    private final String loadPURL = "http://localhost:8100/load";
    private final String startURL = "http://localhost:8100/start";
    private final String newQURL = "http://localhost:8100/newQuestion";
    private final String clearAURL = "http://localhost:8100/clearAll";
    private final String deletePURL = "http://localhost:8100/deletePrompt";
    private final String createEURL = "http://localhost:8100/createEmail";

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

        // fill scrollFrame with the prompts from server
        restore();

        // Make the main part of the frame the scrollFrame
        this.add(scrollFrame.getContentPane(), BorderLayout.CENTER);
        // Add footer on bottom of the screen
        this.add(footer, BorderLayout.SOUTH);
        this.setBackground(pink);

        setVisible(true);

        // make functionality for start button
        startButton = footer.getStartButton();
        addListeners();
    }

    /**
     * Create functionality for when the start button is pressed
     */
    public void addListeners() {
        /* add functionality for start button */
        /* if not recording, request server to begin creating the wav file on its side */
        /* if is recording, request server to finalize wav file, input it into whisper, save the Q&A into its prompts, then return Q&A back in response string */
        startButton.addActionListener((ActionEvent e) -> {
            if (!isRecording) {
                try {
                    // Some UI things
                    startButton.setText("Stop Recording");
                    startButton.setForeground(red);

                    // create URL (with query) to the server and create the connection
                    String query = "Start";
                    URL url = new URL(startURL + "?=" + query);
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
                    startButton.setText("Start");
                    startButton.setForeground(black);
                    
                    // create URL (with query) to the server and create the connection
                    String query = "Stop";
                    URL url = new URL(startURL + "?=" + query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    
                    // request the GET method on the server
                    conn.setRequestMethod("GET");
    
                    // read the question/prompt and store it into response
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                    );
                    
                    String lineLoop;
                    String response = "";
                    while ((lineLoop = in.readLine()) != null) {
                        response += lineLoop;
                    }
                    response = response.trim();
                    
                    /* parse response and containing the prompt and call the correct function depending on its contents */
                    String command  = response.toLowerCase();
                    String[] words = command.split(" ");

                    if (words.length >= 2 && words[0].contains("new") && words[1].contains("question")) {
                        System.out.println(response.substring(response.indexOf(" ", 4) + 1).trim());
                        newQuestion(response.substring(response.indexOf(" ", 4) + 1).trim());
                    } else if (words.length >= 2 && words[0].contains("clear") && words[1].contains("all")) {
                        clearAll();
                    } else if (words.length >= 2 && words[0].contains("delete") && words[1].contains("prompt")) {
                        deletePrompt();
                    } else if (words.length >= 2 && words[0].contains("create") && words[1].contains("email")) {
                        createEmail(response.substring(response.indexOf(" ", 7) + 1).trim());
                    }
                    // else do something with non-valid transcription from Whisper API

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("AppFrame: " + ex);
                }
            }
            isRecording = !isRecording;
        });

        // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                e.getWindow().dispose();
                System.out.println("JFrame Closed!\nYOUR MOM!");

                /* request server to write all the prompts into mongo */
                try {
                    // create URL to the server and create the connection
                    URL url = new URL(loadPURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // request the GET method on the server and tell it to save the prompts to database
                    conn.setRequestMethod("GET");

                    // won't call the handler correctly unless I do this?
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                    in.readLine();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("AppFrame: " + ex);
                }
            }
        });
    }

    public void createEmail(String subject) {
        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(createEURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // request the PUT method on the server
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            // write the question to the file
            OutputStreamWriter out = new OutputStreamWriter(
                conn.getOutputStream()
            );
            subject = subject.trim();
            out.write(subject);
            out.flush();
            out.close();

            // read the answer from file
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            String lineLoop;
            String response = "";
            while ((lineLoop = in.readLine()) != null) {
                response += lineLoop;
            }
            response = response.trim();
            in.close();

            // add the command, question, and response (answer) to the scrollFrame
            String command = "Create Email";
            Prompt prompt = new Prompt(command, subject, response);
            scrollFrame.addPrompt(prompt);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("AppFrame: " + ex);
        }
    }
    /**
     * Make a request to newQH (input the question) to add the new prompt to its prompts, then add the response prompt Q&A to scrollFrame
     * @param question String of the question to be inputted
     */
    public void newQuestion(String question) {
        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(newQURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // request the PUT method on the server
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            // write the question to the file
            OutputStreamWriter out = new OutputStreamWriter(
                conn.getOutputStream()
            );
            question = question.trim();
            out.write(question);
            out.flush();
            out.close();

            // read the answer from file
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            String lineLoop;
            String response = "";
            while ((lineLoop = in.readLine()) != null) {
                response += lineLoop;
            }
            response = response.trim();
            in.close();

            // add the command, question, and response (answer) to the scrollFrame
            String command = "New Question";
            Prompt prompt = new Prompt(command, question, response);
            scrollFrame.addPrompt(prompt);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("AppFrame: " + ex);
        }
    }

    /**
     * Make a request to clearAH to clear all its prompts, and clear all prompts in scrollframe
     */
    public void clearAll() {
        // delete all prompts in prompt history when clear button is pressed
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
                new InputStreamReader(conn.getInputStream()));
            in.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("AppFrame: " + ex);
        }
    }

    /**
     * Make a request to deletePH to delete its prompts at certain indices, and delete selected prompts in scrollframe
     */
    public void deletePrompt() {
        /* delete selected prompts on scrollFrame */
        ArrayList<Integer> indices = scrollFrame.removeSelectedPrompts();
        repaint();

        /* request server to delete its prompts at these indices */
        try {
            for (int i : indices) {
                // create URL (with query) to the server and create the connection
                String query = String.valueOf(i);
                URL url = new URL(deletePURL + "?=" + query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // request the DELETE method on the server
                conn.setRequestMethod("DELETE");

                // won't call the handler correctly unless I do this?
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                in.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("AppFrame: " + ex);
        }
    }

    /**
     * Fill scrollFrame with the prompts from server
     */
    public void restore() {
        /* loop to request server for each of its prompts */
        try {
            int i = 0;
            String response;
            while (true) {
                // create URL (with query) to the server and create the connection
                String query = String.valueOf(i);
                URL url = new URL(indexURL + "?=" + query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // request the GET method on the server
                conn.setRequestMethod("GET");

                // print the response for testing purposes
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
                String lineLoop;
                response = "";
                while ((lineLoop = in.readLine()) != null) {
                    response += lineLoop;
                }
                response = response.trim();
                System.out.println("GET response: " + response);

                // check if the reponse is -1 (reached end of prompts on server)
                if (response.equals("-1")) { break; }

                // parse the response and store the question and answer in the prompts locally
                String command = response.substring(0, response.indexOf("/C\\"));
                String question = response.substring(response.indexOf("/C\\") + 3, response.indexOf("/D\\"));
                String answer = response.substring(response.indexOf("/D\\") + 3);
                Prompt prompt = new Prompt(command, question, answer);
                scrollFrame.addPrompt(prompt);

                in.close();
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("AppFrame: " + ex);
        }
    }
}