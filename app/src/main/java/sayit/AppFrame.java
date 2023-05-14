package sayit;

import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;

/**
 * Put JPanels on AppFrame and make listeners for buttons
 */
public class AppFrame extends JFrame {
    // Objects needed to update AppFrame
    private Mediator mediator;
    private InputQ input;
    private OutputA output;
    private PromptHistory promptHistory;
    
    // put all JPanels here
    private ScrollFrame scrollFrame;
    private Footer footer;

    // put all buttons used in app here
    private JButton newButton;
    private JButton clearButton;
    private JButton clearSelectedButton;

    // other miscellaneous variables
    private boolean isRecording = false;
    Color black = new Color(0, 0, 0);
    Color red = new Color(255, 0, 0);
    Color pink = new Color(227, 179, 171);

    /**
     * Call all other necessary classes and setup AppFrame
     */
    public void setupAppFrame(String filePath) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SayIt");
        setSize(400, 600);

        // Set footer to footer in mediator
        scrollFrame = mediator.getScrollFrame();
        footer = mediator.getFooter();

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

        // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
              e.getWindow().dispose();
              System.out.println("JFrame Closed!\nYOUR MOM!");
              try {
                promptHistory = mediator.getPromptHistory();
                FileOutputStream fout = new FileOutputStream(filePath);
                String qnA;
                byte[] array;
                int size = promptHistory.getSize();
                for (int i = 0; i < size; i++) {
                    qnA = promptHistory.getQuery(i);
                    array = qnA.getBytes();
                    fout.write(array);

                    fout.write('\n');
                    
                    qnA = promptHistory.getAnswer(i);
                    array = qnA.getBytes();
                    fout.write(array);

                    if (i != size-1) {
                        fout.write('\n');
                    }
                }
                fout.close();
              } catch(IOException ex) {
                System.out.println(ex);
              }
            }
        });
    }

    /**
     * Create functionality for when the new and clear buttons are pressed
     */
    public void addListeners() {
        // start or stop recording when new button is pressed
        AudioRecord audio = mediator.getAudioRecord();
        newButton.addActionListener(
            (ActionEvent e) -> {
                if (!isRecording) {
                    newButton.setText("Stop Recording");
                    newButton.setForeground(red);
                    audio.startRecording();
                } else {
                    newButton.setText("New Question");
                    newButton.setForeground(black);
                    audio.stopRecording();
                    updatePrompts();
                }
                isRecording = !isRecording;
            });

        // delete all prompts in prompt history when clear button is pressed
        clearButton.addActionListener(
            (ActionEvent e) -> {
                scrollFrame.clearAllPrompts();
            });

        clearSelectedButton.addActionListener((ActionEvent e) -> {
            scrollFrame.removeSelectedPrompts();
            repaint();
        });
    }

    /**
     * Add new prompt from new question to prompt history and scroll frame
     */
    public void updatePrompts() {
        // create prompt
        input = mediator.getInputQ();
        output = mediator.getOutputA();
        promptHistory = mediator.getPromptHistory();
        Prompt prompt = new Prompt(input.getInputQ(), output.getOutputA());

        // add the Q and A to promptHistory
        promptHistory.addPrompt(prompt);

        // add the Q and A to scrollFrame
        scrollFrame.addPrompt(prompt);
    }

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(Mediator m) { mediator = m; }
}