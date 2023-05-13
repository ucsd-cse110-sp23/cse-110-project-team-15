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
  private PromptHistory history;
  private JPanel contentPane;
  private JScrollPane scrollPane;

  public void setupScrollFrame() {

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Prompt History");

    // Create the content pane
    contentPane = new JPanel();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    contentPane.setBackground(Color.BLUE);

    // Add multiple prompt objects to the content pane
    history = mediator.getPromptHistory();
    for (Prompt prompt: history.getHistoryArray()) { contentPane.add(prompt); }

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
    contentPane.add(new Prompt(question, answer));
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
    for (Prompt p: history.getHistoryArray()) {
      if (((Prompt) p).getState()) {
        contentPane.remove(p); // remove the component
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
