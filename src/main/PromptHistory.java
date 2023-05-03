import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.*;

// Add Header Class

// Add Footer Class
class Footer extends JPanel {
  JButton newButton;
  JButton clearButton;

  Color backgroundColor = new Color(50, 205, 50);
  Border emptyBorder = BorderFactory.createEmptyBorder();

  Footer() {
    this.setPreferredSize(new Dimension(400, 60));
    this.setBackground(backgroundColor);
    // TODO: Set the layout of the footer to a GridLayout with 1 row and 2 columns
    GridLayout layout = new GridLayout(1, 2);
    this.setLayout(layout);

    newButton = new JButton("New"); // add new button
    newButton.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
    this.add(newButton); // add to footer

    clearButton = new JButton("Clear All"); // clear button
    clearButton.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
    this.add(clearButton); // add to footer
  }

  public JButton getNewButton() {
    return newButton;
  }

  public JButton getClearButton() {
    return clearButton;
  }
}

class Prompt extends JPanel {

  private JTextArea queryField;
  private JTextArea answerField;

  Color pink = new Color(227, 179, 171);
  Color blue = new Color(171, 219, 227);

  Prompt(String query, String answer) {

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Format Query Field
    queryField = new JTextArea(query);
    queryField.setWrapStyleWord(true);
    queryField.setLineWrap(true);
    queryField.setEditable(false);
    queryField.setBackground(pink);
    queryField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    queryField.setFont(new Font("Arial", Font.PLAIN, 14));

    Border paddingBorder1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    Border compoundBorder1 = BorderFactory.createCompoundBorder(queryField.getBorder(), paddingBorder1);
    queryField.setBorder(compoundBorder1);

    // Format Answer Field
    answerField = new JTextArea(answer);
    answerField.setFont(new Font("Arial", Font.PLAIN, 12));
    answerField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    answerField.setLineWrap(true);
    answerField.setWrapStyleWord(true);
    answerField.setEditable(false);
    answerField.setPreferredSize(new Dimension(200, 50));
    answerField.setBackground(pink);

    Border paddingBorder2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    Border compoundBorder2 = BorderFactory.createCompoundBorder(answerField.getBorder(), paddingBorder2);
    answerField.setBorder(compoundBorder2);

    // Add text fields to prompt box
    this.add(queryField);
    this.add(answerField);

    // Format prompt box
    Border border = BorderFactory.createLineBorder(Color.BLACK);
    setBorder(BorderFactory.createCompoundBorder(border,
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    this.setBackground(blue);

    this.revalidate();
  }
}

class ScrollFrame extends JFrame {

  private GetPromptHistory history = new GetPromptHistory();

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

    scrollPane = new JScrollPane(contentPane);
    scrollPane.setPreferredSize(new Dimension(400, 400));

    setContentPane(scrollPane);

    pack();
    setVisible(true);
  }
}

class AppFrame extends JFrame {

  private ScrollFrame scrollFrame;
  private Footer footer;

  private JButton newButton;
  private JButton clearButton;

  public AppFrame() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("SayIt");
    setSize(400, 600);

    // Create a new ScrollFrame
    scrollFrame = new ScrollFrame();
    footer = new Footer();

    // Make the main part of the frame the scrollFrame
    this.add(scrollFrame.getContentPane(), BorderLayout.CENTER);
    // Add footer on bottom of the screen
    this.add(footer, BorderLayout.SOUTH);

    // Create listeners for new and clear buttons
    newButton = footer.getNewButton();
    clearButton = footer.getClearButton();
    addListeners();

    setVisible(true);
  }

  /**
   * Create functionality for the new and clear buttons
   */
  public void addListeners() {
    newButton.addActionListener(
        (ActionEvent e) -> {
          AudioRecord audio = new AudioRecord();
          audio.startRecording();
        });

    clearButton.addActionListener(
        (ActionEvent e) -> {
          // TBD iteration 2
        });
        
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
