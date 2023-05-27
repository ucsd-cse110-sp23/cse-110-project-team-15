package sayit.UI;

import java.awt.*;
import javax.swing.*;

/**
 * Panel for footer of app that will contain buttons for recording and clearing
 * prompts
 */
public class Footer extends JPanel {
    private JButton clearSelectedButton;
    private JButton newButton;
    private JButton clearButton;
  
    Color backgroundColor = new Color(50, 205, 50);
  
    /**
     * Default constructor that sets up Footer panel in app
     */
    Footer() {
      this.setPreferredSize(new Dimension(400, 60));
      this.setBackground(backgroundColor);
      // Set the layout of the footer to a GridLayout with 1 row and 2 columns
      GridLayout layout = new GridLayout(1, 2);
      this.setLayout(layout);
  
      // add new question button to footer
      newButton = new JButton("New Question");
      newButton.setFont(new Font("Sans-serif", Font.ITALIC, 20));
      this.add(newButton);
  
      // add clear button to footer
      clearButton = new JButton("Clear All");
      clearButton.setFont(new Font("Sans-serif", Font.ITALIC, 20));
      this.add(clearButton);
  
      // add clear selected button to footer
      clearSelectedButton = new JButton("Clear Selected");
      clearSelectedButton.setFont(new Font("Sans-serif", Font.ITALIC, 20));
      this.add(clearSelectedButton);
    }
  
    /**
     * Getter for newButton
     * @return JButton of the newButton button in Footer
     */
    public JButton getNewButton() {
      return newButton;
    }
  
    /**
     * Getter for clearButton
     * @return JButton of the clearButton button in Footer
     */
    public JButton getClearButton() {
      return clearButton;
    }
  
    /**
     * Getter for clearSelectedButton
     * @return JButton of the clearButton button in Footer
     */
    public JButton getClearSelectedButton() {
      return clearSelectedButton;
    }
  }
