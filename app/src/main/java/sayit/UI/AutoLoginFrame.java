package sayit.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoLoginFrame extends JFrame {

    private JPasswordField passwordField;

    public AutoLoginFrame(String inputEmail) {
        setTitle("Set AutoLogin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Do you want to automatically login from this device?");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        panel.add(label, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        yesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                /*
                 * For iteration 2:
                 * - request loadPH handlePut() with query "autoLogin" to add IP and its associated email to mango
                 * - input the IP and the email in the request
                 */
                new AppFrame();
            }
        });

        noButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AppFrame();
            }
        });

        add(panel);
        setVisible(true);
    }
}