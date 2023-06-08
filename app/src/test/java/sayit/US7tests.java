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

public class US7tests {
    private final String FILE_EMPTY_PATH = "src/test/java/sayit/Test-files/empty.txt";
    private final String indexURL = "http://localhost:8100/index";
    private final String loadPURL = "http://localhost:8100/load";
    private final String startURL = "http://localhost:8100/start";
    private final String newQURL = "http://localhost:8100/newQuestion";
    private final String clearAURL = "http://localhost:8100/clearAll";
    private final String deletePURL = "http://localhost:8100/deletePrompt";
    private final String createEURL = "http://localhost:8100/createEmail";

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
    void clearAllTest() throws IOException, InterruptedException {
        // create URL (with query) to the server and create the connection
        URL url = new URL(newQURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // request the PUT method on the server
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);

        // write the email and password to the file
        String question = "this is a question";
        OutputStreamWriter out = new OutputStreamWriter(
            conn.getOutputStream()
        );
        out.write(question);
        out.flush();
        out.close();

        // check valid login
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        String response = in.readLine();
        in.close();
        assertEquals("This is the mock answer!!! :)", response);
        

        // create URL (with query) to the server and create the connection
        url = new URL(clearAURL);
        conn = (HttpURLConnection) url.openConnection();

        // request the PUT method on the server
        conn.setRequestMethod("DELETE");

        // to run handler
        in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        response = in.readLine();
        in.close();
        assertEquals("Cleared all prompts", response);


        // create URL (with query) to the server and create the connection
        url = new URL(indexURL);
        conn = (HttpURLConnection) url.openConnection();

        // request the PUT method on the server
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        String query = String.valueOf(0);
        url = new URL(indexURL + "?=" + query);
        conn = (HttpURLConnection) url.openConnection();

        // request the GET method on the server
        conn.setRequestMethod("GET");

        // print the response for testing purposes
        in = new BufferedReader(
            new InputStreamReader(conn.getInputStream()));
        String lineLoop;
        response = "";
        while ((lineLoop = in.readLine()) != null) {
            response += lineLoop + "\n";
        }
        response = response.trim();

        assertEquals("-1", response);
    }
}
