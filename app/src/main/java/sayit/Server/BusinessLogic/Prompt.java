package sayit.Server.BusinessLogic;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.*;

public class Prompt extends JPanel {
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

    /**
     * Default constructor
     * @param query
     * @param answer
     */
    public Prompt(String query, String answer) {

        this.selected = false;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Format Query Field as a textbox
        queryField = new JTextArea(query);
        queryField.setBackground(pink);
        queryField.setFont(new Font("Arial", Font.PLAIN, 14));
        queryField.setWrapStyleWord(true);
        queryField.setLineWrap(true);
        queryField.setEditable(false);
        queryField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              if (answerField.isVisible()) {
                answerField.setVisible(false);
                revalidate();
              } else {
                answerField.setVisible(true);
                revalidate();
              }
            }
          });

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

        answerField.setVisible(false);
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

    /**
     * Getter for state of prompt
     * @return whether the prompt is selected or not
     */
    public boolean getState() {
        return this.selected;
    }

    /**
     * Getter for delete button of prompt
     * @return deleteButton instance
     */
    public JButton getDelete() {
        return deleteButton;
    }
}