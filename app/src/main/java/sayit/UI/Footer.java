package sayit.UI;

import java.awt.*;
import javax.swing.*;

/**
 * Panel for footer of app that will contain buttons for recording and clearing prompts
 */
public class Footer extends JPanel {
    private JButton startButtion;
  
    Color backgroundColor = new Color(50, 205, 50);
  
    /**
     * Default constructor that sets up Footer panel in app
     */
    Footer() {
      this.setPreferredSize(new Dimension(400, 60));
      this.setBackground(backgroundColor);
      // Set the layout of the footer to a GridLayout with 1 row and 1 column
      GridLayout layout = new GridLayout(1, 1);
      this.setLayout(layout);
  
      // add new question button to footer
      startButtion = new JButton("Start");
      startButtion.setFont(new Font("Sans-serif", Font.ITALIC, 20));
      this.add(startButtion);
   }
 
    /**
     * Getter for startButton
     * @return JButton of the newButton button in Footer
     */
    public JButton getStartButton() {
      return startButtion;
    }
  }
