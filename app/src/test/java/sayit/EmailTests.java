package sayit;
//package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;
import sayit.UI.MockAutoLoginFrame;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import sayit.Server.MockServer;
import sayit.Server.BusinessLogic.Prompt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

public class EmailTests {
    private final String FILE_EMPTY_PATH = "src/test/java/sayit/Test-files/empty.txt";
    private final String indexURL = "http://localhost:8100/index";
    private final String loadPURL = "http://localhost:8100/load";
    private final String startURL = "http://localhost:8100/start";
    private final String newQURL = "http://localhost:8100/newQuestion";
    private final String clearAURL = "http://localhost:8100/clearAll";
    private final String deletePURL = "http://localhost:8100/deletePrompt";
    private final String createEURL = "http://localhost:8100/createEmail";
    private final String sendEURL = "http://localhost:8100/sendEmail";
    private final String setupURL = "http://localhost:8100/setupEmail";

    @BeforeEach
    void startServer() throws IOException, InterruptedException {
        // start the server and fill its prompts with this file
        MockServer.startServer();
    }
    @AfterEach
    void closeServer() throws IOException {
        MockServer.stopServer();
    }

    @Test
    void sendEmailTest() throws IOException, InterruptedException {
        String toEmail = "kgromero@ucsd.edu";
        String emailAddress = "vrtaylor@ucsd.edu";
        String emailPassword = "uzxbcwadxswbtsxg";
        StringBuilder SMTPHost = new StringBuilder("smtp.gmail.com");
        StringBuilder TLSPort = new StringBuilder("587");
        StringBuilder emailPrompt = new StringBuilder("Test prompt");

        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(sendEURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // request the PUT method on the server
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            /* get the subject and body from emailPrompt */
            String ep = "Test prompt";
            String subject = "Test subject";
            String body = "Test body";

            // write the question to the file
            OutputStreamWriter out = new OutputStreamWriter(
                conn.getOutputStream()
            );
            toEmail = toEmail.trim();
            out.write(emailAddress.toString() + "\n");
            out.write(emailPassword.toString() + "\n");
            out.write(toEmail + "\n");
            out.write(SMTPHost.toString() + "\n");
            out.write(TLSPort.toString() + "\n");
            out.write(subject + "\n");
            out.write(body);
            out.flush();
            out.close();

            // read the answer from file
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            String lineLoop;
            String response = "";
            while ((lineLoop = in.readLine()) != null) {
                response += lineLoop + "\n";
            }
            response = response.trim();
            in.close();

            // check reponse
            assertEquals("Email Successfully Sent", response);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("AppFrame: " + ex);
        }
    }
}
