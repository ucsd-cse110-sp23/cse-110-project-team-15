package sayit.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountFrame extends JFrame {

    private JPasswordField passwordField;

    public CreateAccountFrame() {
        setTitle("SayIt CreateAccount");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JLabel passwordLabel = new JLabel("Verify Password:");
        passwordField = new JPasswordField();
        JButton createAccountButton = new JButton("Create Account");

        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(createAccountButton); // Empty label for layout purposes

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                if (password.equals("password")) {
                    JOptionPane.showMessageDialog(CreateAccountFrame.this, "Account Created!");
                    dispose();
                    new AutoLoginFrame();
                } else {
                    JOptionPane.showMessageDialog(CreateAccountFrame.this, "Password does not match. Please try again.");
                    dispose();
                    new Login();
                }
            }
        });

        add(panel);
        setVisible(true);
    }
}