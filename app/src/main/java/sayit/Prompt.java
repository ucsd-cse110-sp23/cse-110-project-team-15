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

public class Prompt extends JPanel {
    Mediator mediator;
    // Get Icon
    // Load the icon image from a file
    ImageIcon icon = new ImageIcon("src/resources/delete.png");
    ImageIcon deleteSelectIcon = new ImageIcon("src/resources/deleteSelected.png");

    // Scale the icon image to fit the button
    Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    Image scaledDeleteSelect = deleteSelectIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    ImageIcon deleteSelectScaled = new ImageIcon(scaledDeleteSelect);
    ImageIcon scaledIcon = new ImageIcon(scaledImage);

    private boolean selected;
    private JTextArea queryField;
    private JTextArea answerField;
    JButton deleteButton;

    Color pink = new Color(227, 179, 171);
    Color blue = new Color(171, 219, 227);

    Prompt(String query, String answer) {

        this.selected = false;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Format Query Field as a textbox
        queryField = new JTextArea(query);
        queryField.setBackground(pink);
        queryField.setFont(new Font("Arial", Font.PLAIN, 14));
        queryField.setWrapStyleWord(true);
        queryField.setLineWrap(true);
        queryField.setEditable(false);

        // Make it so that there is some space between the text and the border line
        Border paddingBorder1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border compoundBorder1 = BorderFactory.createCompoundBorder(queryField.getBorder(), paddingBorder1);
        queryField.setBorder(compoundBorder1);

        // To store the delete button and query text
        Border queryPanelBorder = BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK);
        JPanel queryPanel = new JPanel(new BorderLayout());
        queryPanel.add(queryField, BorderLayout.CENTER);
        queryPanel.setBorder(queryPanelBorder);

        // Make delete button
        deleteButton = new JButton(scaledIcon);
        deleteButton.setPreferredSize(new Dimension(20, 20));
        deleteButton.setIcon(scaledIcon);
        deleteButton.setBorder(BorderFactory.createEmptyBorder());
        deleteButton.setFocusPainted(false);

        deleteButton.addActionListener(e -> {
            if (selected) {
                deleteButton.setIcon(scaledIcon);
                this.selected = false;
                revalidate();
            } else {
                deleteButton.setIcon(deleteSelectScaled);
                this.selected = true;
                revalidate();
            }
        });

        // Create a new JPanel to hold the delete button and set its preferred size
        JPanel deletePanel = new JPanel();
        deletePanel.setPreferredSize(new Dimension(25, 25));
        deletePanel.add(deleteButton);
        deletePanel.setBackground(pink);
        deletePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        queryPanel.add(deletePanel, BorderLayout.EAST);
        queryPanel.setBackground(pink);

        // Format Answer Field as a text box
        answerField = new JTextArea(answer);
        answerField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        answerField.setBackground(pink);
        answerField.setFont(new Font("Arial", Font.PLAIN, 12));
        answerField.setWrapStyleWord(true);
        answerField.setLineWrap(true);
        answerField.setEditable(false);
        answerField.setPreferredSize(new Dimension(200, 50));

        // Make it so that there is some space between the text and the border line
        Border paddingBorder2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border compoundBorder2 = BorderFactory.createCompoundBorder(answerField.getBorder(), paddingBorder2);
        answerField.setBorder(compoundBorder2);

        // Add text fields to prompt box
        this.add(queryPanel, BorderLayout.NORTH);
        this.add(answerField, BorderLayout.CENTER);

        // Format prompt box with some padding
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        this.setBackground(blue);

        this.revalidate();
    }

    /**
     * Getter for query of prompt
     * @return String query of prompt
     */
    public String getQuery() {
        return this.queryField.getText();
    }

    /**
     * Getter for answer of prompt
     * @return String answer of prompt
     */
    public String getAnswer() {
        return this.answerField.getText();
    }

    public boolean getState() {
        return this.selected;
    }

    public JButton getDelete() {
        return deleteButton;
    }

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(Mediator m) { mediator = m; }
}
