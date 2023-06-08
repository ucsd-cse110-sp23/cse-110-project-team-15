package sayit.UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

import com.sun.mail.imap.protocol.ID;

import sayit.Server.BusinessLogic.Prompt;

public class SetupEmail extends JFrame {
    
    private JPanel contentPane;
    private JPanel gridPane;
    private JPanel buttonPanel;

    Color pink = new Color(227, 179, 171);
    Color blue = new Color(171, 219, 227);

    private final String setupURL = "http://localhost:8100/setupEmail";

    SetupEmail(StringBuilder IFirstName, StringBuilder ILastName, StringBuilder IDisplayName, StringBuilder IEmailAddress, StringBuilder IEmailPassword, StringBuilder ISMTPHost, StringBuilder ITLSPort, StringBuilder emailPrompt) {
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
        if (!IFirstName.toString().equals("null")) { firstName.setText(IFirstName.toString()); }
        JTextField lastName = new JTextField(20);
        if (!ILastName.toString().equals("null")) { lastName.setText(ILastName.toString()); }
        JTextField displayName = new JTextField(20);
        if (!IDisplayName.toString().equals("null")) { displayName.setText(IDisplayName.toString()); }
        JTextField emailAddress = new JTextField(20);
        if (!IEmailAddress.toString().equals("null")) { emailAddress.setText(IEmailAddress.toString()); }
        JTextField emailPassword = new JTextField(20);
        if (!IEmailPassword.toString().equals("null")) { emailPassword.setText(IEmailPassword.toString()); }
        JTextField SMTPHost = new JTextField(20);
        if (!ISMTPHost.toString().equals("null")) { SMTPHost.setText(ISMTPHost.toString()); }
        JTextField TLSPort = new JTextField(20);
        if (!ITLSPort.toString().equals("null")) { TLSPort.setText(ITLSPort.toString()); }

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
                // save tne data to strings, open the AppFrame class, and display a "Saved" message
                IFirstName.setLength(0);
                IFirstName.append(firstName.getText().trim());

                ILastName.setLength(0);
                ILastName.append(lastName.getText().trim());

                IDisplayName.setLength(0);
                IDisplayName.append(displayName.getText().trim());

                IEmailAddress.setLength(0);
                IEmailAddress.append(emailAddress.getText().trim());

                IEmailPassword.setLength(0);
                IEmailPassword.append(emailPassword.getText().trim());

                ISMTPHost.setLength(0);
                ISMTPHost.append(SMTPHost.getText().trim());

                ITLSPort.setLength(0);
                ITLSPort.append(TLSPort.getText().trim());
                // save all datq  \t database
                // IDisplayName.toString()

                try {
                    // create URL (with query) to the server and create the connection
                    URL url = new URL(setupURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    
                    // request the PUT method on the server
                    conn.setRequestMethod("PUT");
                    conn.setDoOutput(true);
        
                    // write the question to the file
                    OutputStreamWriter out = new OutputStreamWriter(
                        conn.getOutputStream()
                    );
                    out.write(IFirstName.toString() + "\n");
                    out.write(ILastName.toString() + "\n");
                    out.write(IDisplayName.toString() + "\n");
                    out.write(IEmailAddress.toString() + "\n");
                    out.write(IEmailPassword.toString() + "\n");
                    out.write(ISMTPHost.toString() + "\n");
                    out.write(ITLSPort.toString());
                    out.flush();
                    out.close();
        
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                    );
                    in.readLine();
                    in.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("AppFrame: " + ex);
                }

                JOptionPane.showMessageDialog(null, "Saved");
                dispose();
            }
        });

        // Add action listener to the cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the AppFrame class and display a "Canceled" message
                //AppFrame appFrame = new AppFrame();
                JOptionPane.showMessageDialog(null, "Canceled");
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