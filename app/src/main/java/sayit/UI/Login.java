package sayit.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("SayIt Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel emailLabel = new JLabel("email:");
        JLabel passwordLabel = new JLabel("Password:");
        emailField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(createAccountButton); // Empty label for layout purposes\
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                if (email.equals("admin") && password.equals("password")) {
                    JOptionPane.showMessageDialog(Login.this, "Login successful!");
                    dispose();
                    new AutoLoginFrame();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Invalid username or password. Please try again.");
                }
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CreateAccountFrame();
            }
        });

        add(panel);
        setVisible(true);
    }
}