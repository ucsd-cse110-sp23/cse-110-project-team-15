package sayit.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class

/*
 * Removes all DISPLAY variables so testing pases on GitHUb
 */
public class MockAutoLoginFrame{

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
