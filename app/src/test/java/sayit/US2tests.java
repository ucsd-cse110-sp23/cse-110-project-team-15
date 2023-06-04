package sayit;
//package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;

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

public class US2tests {
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
    void existingUserCorrectPasswordTest() throws IOException, InterruptedException {        
        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(loadPURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // request the PUT method on the server
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            // write the email and password to the file
            String email = "notempty@ucsd.edu";
            String password = "notempty";
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
            assertEquals("Valid Login", response);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }

    @Test
    void nonExistingUserTest() throws IOException, InterruptedException {        
        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(loadPURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // request the PUT method on the server
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            // write the email and password to the file
            String email = "idontexist@ucsd.edu";
            String password = "idontexist";
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
            assertEquals("Invalid Login", response);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }

    @Test
    void existingUserIncorrectPasswordTest() throws IOException, InterruptedException {        
        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(loadPURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // request the PUT method on the server
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            // write the email and password to the file
            String email = "notempty@ucsd.edu";
            String password = "wrongpassword";
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
            assertEquals("Invalid Login", response);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }
}

