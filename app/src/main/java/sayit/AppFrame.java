package sayit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;
import javax.swing.*; 

/**
 * Put JPanels on AppFrame and make listeners for buttons
 */
public class AppFrame extends JFrame {
    Mediator mediator;
    
    // put all JPanels here
    // private ScrollFrame scrollFrame;
    // private Footer footer;

    // put all buttons used in app here
    private JButton newButton;
    private JButton clearButton;
    private JButton clearSelectedButton;

    // other miscellaneous variables
    private boolean isRecording = false;
    Color black = new Color(0, 0, 0);
    Color red = new Color(255, 0, 0);
    Color pink = new Color(227, 179, 171);

    public AppFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SayIt");
        setSize(400, 600);

        // Create a new ScrollFrame
        mediator.getScrollFrame();
        // Create a new Footer
        footer = new Footer();

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
        // start or stop recording when new button is pressed
        AudioRecord audio = new AudioRecord();
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
                        updateScrollFrame();
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

    public void updateScrollFrame() {
        InputQ input = new InputQ();
        String question = input.getInputQ();
        OutputA output = new OutputA();
        String answer = output.getOutputA();
        scrollFrame.addPrompt(question, answer);
    }

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(Mediator m) { mediator = m; }
}
