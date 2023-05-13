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
import javax.print.attribute.standard.Media;
import javax.swing.*;

public class ScrollFrame extends JFrame {
  Mediator mediator;
  private final String FILE_PATH = "src/main/java/sayit/Test-files/test-1.txt";
  private PromptHistory history = new PromptHistory(FILE_PATH);
  private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
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
      prompts.add(prompt);
      contentPane.add(prompt);
    }

    // Display the contents and enable scrolling
    scrollPane = new JScrollPane(contentPane);
    scrollPane.setPreferredSize(new Dimension(400, 400));
    scrollPane.getVerticalScrollBar().setUnitIncrement(15);
    setContentPane(scrollPane);

    pack();
  }

  /**
   * Display new prompt and answer to content pane
   */
  public void addPrompt(String question, String answer) {
    Prompt prompt = new Prompt(question, answer);
    history.addPrompt(question, answer);
    contentPane.add(prompt);
  }

  /**
   * Empty prompts array in GetPromptHistory, and set its size to 0
   */
  public void clearAllPrompts() {
    history.clearPrompts();
    for (Component c : contentPane.getComponents()) {
      contentPane.remove(c);
    }
    contentPane.revalidate();
    contentPane.repaint();
  }

  public void removeSelectedPrompts() {
    for (Prompt c : prompts) {
      if (((Prompt) c).getState()) {
        contentPane.remove(c); // remove the component
      }
      contentPane.revalidate();
      contentPane.repaint();
    }
  }

  /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(Mediator m) { mediator = m; }
}
