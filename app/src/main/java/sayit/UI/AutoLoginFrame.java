package sayit.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class

public class AutoLoginFrame extends JFrame {

    public AutoLoginFrame(String inputEmail, String inputPassword) {
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
                //storeLoginCredentials(inputEmail, inputPassword);
                try {
                    File autoFile = new File("src/main/java/sayit/UI/AutoFolder/AutoLog.txt");
                    autoFile.createNewFile();
                    FileWriter myWriter = new FileWriter(autoFile);
                    myWriter.write(inputEmail + "\n" + inputPassword);
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException ex){
                    System.out.println(ex);
                }
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

    /*
     * creates file with login credentials to be used for auto login in the future
     * @param inputEmail the user's email
     * @param inputPassword the user's pasword
     */
    public void storeLoginCredentials(String inputEmail, String inputPassword) {
        try {
            File autoFile = new File("src/main/java/sayit/UI/AutoFolder/AutoLog.txt");
            autoFile.createNewFile();
            FileWriter myWriter = new FileWriter(autoFile);
            myWriter.write(inputEmail + "\n" + inputPassword);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException ex){
            System.out.println(ex);
        }
    }
}