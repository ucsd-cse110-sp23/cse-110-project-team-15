package sayit;
//package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;
import sayit.UI.MockAutoLoginFrame;

import java.io.*;
import java.net.*;
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

public class US4tests {
    private final String FILE_EMPTY_PATH = "src/test/java/sayit/Test-files/empty.txt";
    private final String indexURL = "http://localhost:8100/index";
    private final String loadPURL = "http://localhost:8100/load";
    private final String startURL = "http://localhost:8100/start";
    private final String newQURL = "http://localhost:8100/newQuestion";
    private final String clearAURL = "http://localhost:8100/clearAll";
    private final String deletePURL = "http://localhost:8100/deletePrompt";

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
    void acceptAutoLoginTest() throws IOException, InterruptedException {
        // create URL (with query) to the server and create the connection
        URL url = new URL(loadPURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // request the PUT method on the server
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);

        // write the email and password to the file
        String email = "test";
        String password = "test";
        OutputStreamWriter out = new OutputStreamWriter(
            conn.getOutputStream()
        );
        out.write(email + "\n");
        out.write(password);
        out.flush();
        out.close();

        // check valid login
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        String response = in.readLine();
        in.close();
        assertEquals("Valid Login", response);

        // check that autoFile is created with correct login credentials
        MockAutoLoginFrame myAutoFrame = new MockAutoLoginFrame(email, password);
        myAutoFrame.storeLoginCredentials(email, password);
        File autoFile = new File("src/main/java/sayit/UI/AutoFolder/AutoLog.txt");
        assertTrue(autoFile.exists());
        BufferedReader reader = new BufferedReader(new FileReader(autoFile));
        String writtenEmail = reader.readLine();
        String writtenPassword = reader.readLine();
        reader.close();
        assertEquals(email, writtenEmail);
        assertEquals(password, writtenPassword);

        //delete autoFile for test repeatability
        autoFile.delete();
    }

    @Test
    void autoLoginTest() throws IOException, InterruptedException {
        // create autoLogin file
        MockAutoLoginFrame myAutoFrame = new MockAutoLoginFrame("test", "test");
        myAutoFrame.storeLoginCredentials("test", "test");
        File autoFile = new File("src/main/java/sayit/UI/AutoFolder/AutoLog.txt");

        // create URL (with query) to the server and create the connection
        URL url = new URL(loadPURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // request the PUT method on the server
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);

        // get email and password from autoFile
        BufferedReader reader = new BufferedReader(new FileReader(autoFile));
        String email = reader.readLine();
        String password = reader.readLine();
        reader.close();

        //write email and password to server
        OutputStreamWriter out = new OutputStreamWriter(
            conn.getOutputStream()
        );
        out.write(email + "\n");
        out.write(password);
        out.flush();
        out.close();

        // check valid login
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        String response = in.readLine();
        in.close();
        assertEquals("Valid Login", response);

        //delete autoFile for test repeatability
        autoFile.delete();
    }

    @Test
    void declineAutoLoginTest() throws IOException, InterruptedException {
        // create URL (with query) to the server and create the connection
        URL url = new URL(loadPURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // request the PUT method on the server
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);

        // write the email and password to the file
        String email = "test";
        String password = "test";
        OutputStreamWriter out = new OutputStreamWriter(
            conn.getOutputStream()
        );
        out.write(email + "\n");
        out.write(password);
        out.flush();
        out.close();

        // check valid login
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        String response = in.readLine();
        in.close();
        assertEquals("Valid Login", response);

        // check that autoFile does not exist
        File autoFile = new File("src/main/java/sayit/UI/AutoFolder/AutoLog.txt");
        assertFalse(autoFile.exists());
    }
}
