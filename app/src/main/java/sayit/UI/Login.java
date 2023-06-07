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
import java.io.File;  // Import the File class
import java.io.FileReader;
import java.io.IOException;  // Import the IOException class to handle errors

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    String email;
    String password;
    private final String loadPURL = "http://localhost:8100/load";

    public Login() {
        /*
         * look for autoLoginFile
         * if file exists
         *      read file and set email & password to file contents
         *      execute below try/catch, skip autoLogin, go to appFrame
         * else
         *      execute below try/catch
         */
        try {
            File autoFile = new File("src/main/java/sayit/UI/AutoFolder/AutoLog.txt");
            if(autoFile.exists()) {
                String email;
                String password;

                BufferedReader reader = new BufferedReader(new FileReader(autoFile));
                email = reader.readLine();
                password = reader.readLine();
                reader.close();

                // create URL (without query) to the server and create the connection
                URL url = new URL(loadPURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                // request the PUT method on the server
                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);
    
                // write the email and password to the file
                OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream()
                );
                out.write(email + "\n");
                out.write(password);
                out.flush();
                out.close();
    
                // read the answer from file
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                );
                String response = in.readLine();
                in.close();

                if (response.equals("Valid Login")) {
                    JOptionPane.showMessageDialog(Login.this, "Login successful!");
                    dispose();
                    new AppFrame();      //skip since autoLoginFile exists
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Invalid username or password. Please try again.");
                }
                return;
            }
        } catch (IOException ex){
            System.out.println(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("AppFrame: " + ex);
        }
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

        /* 
         * For iteration 2:
         * - request loadPH handlePut() with query = "autoLogin"
         * - if the response says "Automatic Login", immediately go straight to AppFrame() (skip everything below this)
         * - otherwise if the response says "No Automatic Login", then execute code below
         */    

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                email = emailField.getText().trim();
                password = new String(passwordField.getPassword()).trim();
                
                /* 
                 * send request to loadPH handlePut() w/o query and input the email and password from user into it
                 * if its response is "Valid Login", then display "Login successful", and go to AutoLoginFrame
                 * else, display "Invalid username or password. Please try again."
                 */

                try {
                    // create URL (without query) to the server and create the connection
                    URL url = new URL(loadPURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    
                    // request the PUT method on the server
                    conn.setRequestMethod("PUT");
                    conn.setDoOutput(true);
        
                    // write the email and password to the file
                    OutputStreamWriter out = new OutputStreamWriter(
                        conn.getOutputStream()
                    );
                    out.write(email + "\n");
                    out.write(password);
                    out.flush();
                    out.close();
        
                    // read the answer from file
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                    );
                    String response = in.readLine();
                    in.close();

                    if (response.equals("Valid Login")) {
                        JOptionPane.showMessageDialog(Login.this, "Login successful!");
                        dispose();
                        new AutoLoginFrame(email, password);      //skip if autoLoginFile exists
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Invalid username or password. Please try again.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("AppFrame: " + ex);
                }
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                email = emailField.getText().trim();
                password = new String(passwordField.getPassword()).trim();
                new CreateAccountFrame(email, password);
            }
        });

        add(panel);
        setVisible(true);
    }
}