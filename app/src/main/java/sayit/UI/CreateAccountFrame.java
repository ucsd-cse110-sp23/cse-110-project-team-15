package sayit.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateAccountFrame extends JFrame {

    private JPasswordField passwordField;
    private final String loadPURL = "http://localhost:8100/load";
    String inputEmail;
    String inputPassword;

    public CreateAccountFrame(String iEmail, String iPassword) {
        setTitle("SayIt CreateAccount");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        this.inputEmail = iEmail;
        this.inputPassword = iPassword;

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
                if (password.equals(inputPassword)) {
                    JOptionPane.showMessageDialog(CreateAccountFrame.this, "Account Created!");
                    dispose();

                    /* 
                     * request loadPH handlePost() (no query), to add an account to mongo
                     * input the email and password in the request
                     * if the response says "Email already used", say that then return back to the Login page
                     * else if the response says "Account Created", move on to AutoLoginFrame
                     */

                    try {
                        // create URL (with query) to the server and create the connection
                        URL url = new URL(loadPURL);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        
                        // request the PUT method on the server
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
            
                        // write the question to the file
                        OutputStreamWriter out = new OutputStreamWriter(
                            conn.getOutputStream()
                        );
                        inputEmail = inputEmail.trim();
                        inputPassword = password.trim();
                        out.write(inputEmail + "\n");
                        out.write(inputPassword);
                        out.flush();
                        out.close();
            
                        // read the answer from file
                        BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                        );
                        String response = in.readLine();
                        in.close();

                        if (response.equals("Email already used")) {
                            JOptionPane.showMessageDialog(CreateAccountFrame.this, "Email already used");
                            dispose();
                            new Login();
                        } else if (response.equals("Account Created")) {
                            new AutoLoginFrame(inputEmail);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("AppFrame: " + ex);
                    }
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