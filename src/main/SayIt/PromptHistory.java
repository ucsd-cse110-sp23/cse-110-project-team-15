package SayIt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.*; 

// Add Header Class

// Add Footer Class

class Prompt extends JPanel {
    
    private JTextArea queryField;
    private JTextArea answerField;
    JButton deleteButton;

    Color pink = new Color(227, 179, 171);
    Color blue = new Color(171, 219, 227);

    Prompt(String query, String answer) {

      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

      // Format Query Field as a textbox
      queryField = new JTextArea(query);
      queryField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      queryField.setBackground(pink);
      queryField.setFont(new Font("Arial", Font.PLAIN, 14));
      queryField.setWrapStyleWord(true);
      queryField.setLineWrap(true);
      queryField.setEditable(false);

      // Make it so that there is some space between the text and the border line
      Border paddingBorder1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
      Border compoundBorder1 = BorderFactory.createCompoundBorder(queryField.getBorder(), paddingBorder1);
      queryField.setBorder(compoundBorder1);

      // Format Answer Field as a text box
      answerField = new JTextArea(answer);
      answerField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      answerField.setBackground(pink);
      answerField.setFont(new Font("Arial", Font.PLAIN, 12));
      answerField.setWrapStyleWord(true);
      answerField.setLineWrap(true);
      answerField.setEditable(false);
      answerField.setPreferredSize(new Dimension(200,50));

      // Make it so that there is some space between the text and the border line
      Border paddingBorder2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
      Border compoundBorder2 = BorderFactory.createCompoundBorder(answerField.getBorder(), paddingBorder2);
      answerField.setBorder(compoundBorder2);

      deleteButton = new JButton("Delete");
      deleteButton.setPreferredSize(new Dimension(80, 20));
      deleteButton.setBorder(BorderFactory.createEmptyBorder());
      deleteButton.setFocusPainted(false);
      
      // Add text fields to prompt box
      this.add(deleteButton, BorderLayout.EAST);
      this.add(queryField);
      this.add(answerField);

      // Format prompt box with some padding
      Border border = BorderFactory.createLineBorder(Color.BLACK);
      setBorder(BorderFactory.createCompoundBorder(border, 
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
      this.setBackground(blue);

      this.revalidate();
    }
}

class ScrollFrame extends JFrame {

    private GetPromptHistory history = new GetPromptHistory("src/main/Test-files/test-1.txt");
    private JPanel contentPane;
    private JScrollPane scrollPane;
  
    public ScrollFrame() {
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Prompt History");
      
      // Create the content pane
      contentPane = new JPanel();
      contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
      contentPane.setBackground(Color.BLUE);
      
      // Add multiple prompt objects to the content pane
      for (int i = 0; i < history.getSize(); i++) {
          Prompt prompt = new Prompt(history.getQuery(i), history.getAnswer(i));
          contentPane.add(prompt);
      }

      // Display the contents and enable scrolling
      scrollPane = new JScrollPane(contentPane);
      scrollPane.setPreferredSize(new Dimension(400, 400));
      scrollPane.getVerticalScrollBar().setUnitIncrement(15);
      setContentPane(scrollPane);

      pack();
      setVisible(true);
    }
  }

  class AppFrame extends JFrame {

    private ScrollFrame scrollFrame;

    public AppFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SayIt");
        setSize(400, 600);

        // Create a new ScrollFrame
        scrollFrame = new ScrollFrame();

        // Make the main part of the frame the scrollFrame
        this.add(scrollFrame.getContentPane(), BorderLayout.CENTER);

        setVisible(true);
    }
}

  public class PromptHistory {

    public static void main(String args[]) {
      SwingUtilities.invokeLater(() -> {
        new AppFrame();
      });
    }
  }
  
  @interface override {
  }

