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

public class US1tests {
    private final String FILE_EMPTY_PATH = "src/test/java/sayit/Test-files/empty.txt";
    private final String devURL = "http://localhost:8100/dev";
    private final String loadPURL = "http://localhost:8100/load";
    private final String startURL = "http://localhost:8100/start";
    private final String newQURL = "http://localhost:8100/newQuestion";
    private final String clearAURL = "http://localhost:8100/clearAll";
    private final String deletePURL = "http://localhost:8100/deletePrompt";

    @BeforeEach
    void startServer() throws IOException, InterruptedException {
        // start the server and fill its prompts with this file
        MockServer.startServer(FILE_EMPTY_PATH);
    }
    @AfterEach
    void closeServer() throws IOException {
        MockServer.stopServer();
    }

    @Test
    void notEmptyPromptHTest() throws IOException, InterruptedException {        
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

            //reset response for reuse
            response = "";
            try {
                int i = 0;
                //String response;
                while (true) {
                    // create URL (with query) to the server and create the connection
                    String query = String.valueOf(i);
                    url = new URL(devURL + "?=" + query);
                    conn = (HttpURLConnection) url.openConnection();
    
                    // request the GET method on the server
                    conn.setRequestMethod("GET");
    
                    // print the response for testing purposes
                    in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    response = in.readLine();
                    System.out.println("GET response: " + response);
    
                    // check if the reponse is -1 (reached end of prompts on server)
                    if (response.equals("-1")) { break; }
    
                    // parse the response and store the question and answer in the prompts locally
                    String question = response.substring(0, response.indexOf("/D\\"));
                    String answer = response.substring(response.indexOf("/D\\") + 3);
                    String expectedQ = String.format("Question %d?", i);
                    String expectedA = String.format("Answer %d.", i);
                    assertEquals(expectedQ, question);
                    assertEquals(expectedA, answer);
    
                    in.close();
                    i++;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("AppFrame: " + ex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }

    @Test
    void emptyPromptHTest() throws IOException, InterruptedException {        
        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(loadPURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // request the PUT method on the server
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            // write the email and password to the file
            String email = "empty@ucsd.edu";
            String password = "empty";
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

            //reset response for reuse
            response = "";
            try {
                int i = 0;
                //String response;
                while (true) {
                    // create URL (with query) to the server and create the connection
                    String query = String.valueOf(i);
                    url = new URL(devURL + "?=" + query);
                    conn = (HttpURLConnection) url.openConnection();
    
                    // request the GET method on the server
                    conn.setRequestMethod("GET");
    
                    // print the response for testing purposes
                    in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    response = in.readLine();
                    System.out.println("GET response: " + response);

                    assertEquals("-1", response);
    
                    // check if the reponse is -1 (reached end of prompts on server)
                    if (response.equals("-1")) { break; }
    
                    in.close();
                    i++;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("AppFrame: " + ex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }
}

