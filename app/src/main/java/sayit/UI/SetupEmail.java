package sayit.UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class SetupEmail extends JFrame {
    
    private JPanel contentPane;
    private JPanel gridPane;
    private JPanel buttonPanel;

    Color pink = new Color(227, 179, 171);
    Color blue = new Color(171, 219, 227);

    SetupEmail() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Setup Email");

        contentPane = new JPanel();
        gridPane = new JPanel();
        Border titleBorder = new LineBorder(Color.BLACK, 2);
        Border fieldBorder = BorderFactory.createCompoundBorder(
            new LineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(0, 0, 0, 0));

        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(pink);
        contentPane.setPreferredSize(new Dimension(600, 600));

        JLabel titleLabel = new JLabel("Enter Info");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(titleBorder);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        
        contentPane.add(titleLabel, BorderLayout.NORTH);

        gridPane.setLayout(new GridLayout(7, 1, 10, 10));
        gridPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // Add padding
        gridPane.setBackground(pink);
        gridPane.setPreferredSize(new Dimension(500, 500));

        this.setLayout(new BorderLayout());

        // Create the text fields
        JTextField firstName = new JTextField(20); 
        JTextField lastName = new JTextField(20);
        JTextField displayName = new JTextField(20);
        JTextField emailAddress = new JTextField(20);
        JTextField emailPassword = new JTextField(20);
        JTextField SMTPHost = new JTextField(20);
        JTextField TLSPort = new JTextField(20);

        // Set font size
        Font textFieldFont = new Font("Arial", Font.PLAIN, 10);
        firstName.setFont(textFieldFont);
        lastName.setFont(textFieldFont);
        displayName.setFont(textFieldFont);
        emailAddress.setFont(textFieldFont);
        emailPassword.setFont(textFieldFont);
        SMTPHost.setFont(textFieldFont);
        TLSPort.setFont(textFieldFont);

        // Create labels
        JLabel nameLabel = new JLabel("First Name:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel displayNameLabel = new JLabel("Display Name:");
        JLabel emailAddressLabel = new JLabel("Email Address:");
        JLabel emailPasswordLabel = new JLabel("Email Password:");
        JLabel SMTPHostLabel = new JLabel("SMTP Host:");
        JLabel TLSPortLabel = new JLabel("TLS Port:");

        // Add the text fields and labels to the panel
        gridPane.add(nameLabel);
        gridPane.add(firstName);
        gridPane.add(lastNameLabel);
        gridPane.add(lastName);
        gridPane.add(displayNameLabel);
        gridPane.add(displayName);
        gridPane.add(emailAddressLabel);
        gridPane.add(emailAddress);
        gridPane.add(emailPasswordLabel);
        gridPane.add(emailPassword);
        gridPane.add(SMTPHostLabel);
        gridPane.add(SMTPHost);
        gridPane.add(TLSPortLabel);
        gridPane.add(TLSPort);

        contentPane.add(gridPane, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(blue);
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), 
                            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Create the buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.setPreferredSize(new Dimension(100, 50));
        cancelButton.setPreferredSize(new Dimension(100, 50));

        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(Color.GREEN);

        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(Color.RED);

        // Add action listener to the save button
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the AppFrame class and display a "Saved" message
                AppFrame appFrame = new AppFrame();
                dispose();
                JOptionPane.showMessageDialog(appFrame, "Saved");
            }
        });

        // Add action listener to the cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the AppFrame class and display a "Canceled" message
                AppFrame appFrame = new AppFrame();
                JOptionPane.showMessageDialog(appFrame, "Canceled");
                dispose();
            }
        });

        // Add the buttons to the panel
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
