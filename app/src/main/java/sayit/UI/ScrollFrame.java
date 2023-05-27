package sayit.UI;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import sayit.Server.BusinessLogic.Prompt;

public class ScrollFrame extends JFrame {
  // put all JPanels here
  private JPanel contentPane;
  private JScrollPane scrollPane;

  /**
   * Call all other necessary classes and setup ScrollFrame
   */
  ScrollFrame() {

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Prompt History");

    // Create the content pane
    contentPane = new JPanel();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    contentPane.setBackground(Color.BLUE);

    // // Add multiple prompt objects to the content pane
    // history = mediator.getPromptHistory();
    // for (Prompt prompt: history.getHistoryArray()) { contentPane.add(prompt); }

    // Display the contents and enable scrolling
    scrollPane = new JScrollPane(contentPane);
    scrollPane.setPreferredSize(new Dimension(400, 400));
    scrollPane.getVerticalScrollBar().setUnitIncrement(15);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(15);
    setContentPane(scrollPane);

    pack();
  }

  /**
   * Display new prompt and answer to content pane
   */
  public void addPrompt(Prompt p) {
    contentPane.add(p);
    contentPane.revalidate();
    contentPane.repaint();
  }

  /**
   * Empty prompts array in GetPromptHistory, and set its size to 0
   */
  public void clearAllPrompts() {
    for (Component c : contentPane.getComponents()) {
      contentPane.remove(c);
    }
    contentPane.revalidate();
    contentPane.repaint();
  }

  /**
   * Remove selected prompts from scroll pane
   */
  public ArrayList<Integer> removeSelectedPrompts() {
    ArrayList<Integer> indices = new ArrayList<>();
    int i = 0;
    for (Component c: contentPane.getComponents()) {
      if (c instanceof Prompt) {
        if (((Prompt)c).getState()) {
          contentPane.remove(c); // remove the component
          indices.add(i);
          i--;
        }
      }
      contentPane.revalidate();
      contentPane.repaint();
      i++;
    }
    return indices;
  }
}
