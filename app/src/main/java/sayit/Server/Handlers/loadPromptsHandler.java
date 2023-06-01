package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.net.*;
import java.util.*;

/////////////////////////////////////////////////
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
import static java.util.Arrays.*;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
/////////////////////////////////////////////////

/**
 * In charge of getting a prompt at a given index, and recording prompts into preserve.txt
 */
public class loadPromptsHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
    String filePath;

    // Mango Stuff
    static String uri = "mongodb://quistian241:Gura%40241@ac-kumtned-shard-00-00.uikyhue.mongodb.net:27017,ac-kumtned-shard-00-01.uikyhue.mongodb.net:27017,ac-kumtned-shard-00-02.uikyhue.mongodb.net:27017/?ssl=true&replicaSet=atlas-fk9y1w-shard-0&authSource=admin&retryWrites=true&w=majority";
    JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts
     */
    public loadPromptsHandler(ArrayList<Prompt> prompts, String filePath) {
        this.prompts = prompts;
        this.filePath = filePath;
    }

    /**
     * Receives request and appropriately calls either GET, POST, PUT, or DELETE
     * @param httpExchange the request that the server receives
     */
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            }
            else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("loadPromptsHandler.java: An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    /**
     * Get the question and answer at the corresponding index in prompts
     * @param httpExchange the request that the server receives
     * @return String response containing either all of prompts or question + /D\ + answer, otherwise -1 --> (/D\ is the delimeter)
     * @throws IOException
     */
    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        
        if (query != null) {
            int index = Integer.parseInt(query.substring(query.indexOf("=") + 1));
            /* if the index is larger than array, just return -1 */
            if (index >= prompts.size()) { return "-1"; }

            String question = null;
            String answer = null;

            /* Store the question and answer at given index into response */
            question = prompts.get(index).getQuery();
            answer = prompts.get(index).getAnswer();

            /* set response to question + /D\ + answer, otherwise -1 --> (/D\ is the delimeter) */
            response = question + "/D\\" + answer;
            System.out.println("Prompt at index " + index + " is:\nQuestion:" + question + "\nAnswer:" + answer);
        }
        return response;
    }

    /**
     * Write all prompts in prompts into filePath
     * @param httpExchange the request that the server receives
     * @return response saying whether or not the POST succeeded
     * @throws IOException
     */
    private String handlePut(HttpExchange httpExchange) throws IOException {
        // place prompts onto the mangoDB
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accDatabase = mongoClient.getDatabase("AccountData");
            MongoCollection<Document> usersDB = accDatabase.getCollection("Users");
            MongoCollection<Document> ipDB = accDatabase.getCollection("IPs");
            List<Document> susHist = new ArrayList<>();
            String type, ques, ans;
            for (int i = 0; i < prompts.size(); i++) {
                type = "QnA";
                ques = prompts.get(i).getQuery();
                ans = prompts.get(i).getAnswer();
                susHist.add(new Document("Type", type).append("Top", ques).append("Bottom", ans));
            }
            Bson filter = eq("acc_email", "emptyHist1800@gmail.com");
            Bson updateOperation = set("promptH", susHist);
            usersDB.updateOne(filter, updateOperation);
        }
        

        // write to filePath
        final String startSt = "#Start#";
        final String endSt = "#End#";
        FileOutputStream fout = new FileOutputStream(filePath);
        String qnA;
        byte[] array;
        for (int i = 0; i < prompts.size(); i++) {
            fout.write(startSt.getBytes());
            fout.write('\n');

            qnA = prompts.get(i).getQuery().trim();
            array = qnA.getBytes();
            fout.write(array);
            fout.write('\n');
            
            qnA = prompts.get(i).getAnswer().trim();
            array = qnA.getBytes();
            fout.write(array);
            fout.write('\n');

            fout.write(endSt.getBytes());
            if (i != prompts.size() - 1) {
                fout.write('\n');
            }
        }
        fout.close();
        
        // return that all prompts were written to filePath
        String response = "All Prompts were written to " + filePath;
        System.out.println("loadPH: " + response);
        return response;
    }
}