package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.IOutput;
import sayit.Server.BusinessLogic.OutputA;
import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.net.URI;
import java.util.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class setupEmailHandler implements HttpHandler {
    StringBuilder email;
    StringBuilder firstName;
    StringBuilder lastName;
    StringBuilder displayName;
    StringBuilder fromEmail;
    StringBuilder fromPassword;
    StringBuilder SMTPHost;
    StringBuilder TLSPort;

    static final String MONGO_URI = "mongodb://quistian241:Gura%40241@ac-kumtned-shard-00-00.uikyhue.mongodb.net:27017,ac-kumtned-shard-00-01.uikyhue.mongodb.net:27017,ac-kumtned-shard-00-02.uikyhue.mongodb.net:27017/?ssl=true&replicaSet=atlas-fk9y1w-shard-0&authSource=admin&retryWrites=true&w=majority";

    /**
     * Default constructor that initializes ArrayList prompts and output
     * @param prompts ArrayList of prompts
     * @throws InterruptedException
     * @throws IOException
     */
    public setupEmailHandler(StringBuilder email, StringBuilder firstName, StringBuilder lastName, StringBuilder displayName, StringBuilder fromEmail, StringBuilder fromPassword, StringBuilder SMTPHost, StringBuilder TLSPort) throws IOException, InterruptedException {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.SMTPHost = SMTPHost;
        this.TLSPort = TLSPort;
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
            System.out.println("setupEmailHandler.java: An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        /* setup reading from some input file */
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);

        /* get email from first line of file */
        String fn = scanner.nextLine();
        /* get password from second line of file */
        String ln = scanner.nextLine();
        /* get password from second line of file */
        String dn = scanner.nextLine();
        /* get password from second line of file */
        String fe = scanner.nextLine();
        /* get password from second line of file */
        String fp = scanner.nextLine();
        /* get password from second line of file */
        String smtp = scanner.nextLine();
        /* get password from second line of file */
        String tlsp = scanner.nextLine();
        scanner.close();
        
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase accDatabase = mongoClient.getDatabase("AccountData");
            MongoCollection<Document> usersDB = accDatabase.getCollection("Users");
            Document accUser = usersDB.find(eq("acc_email",email.toString())).first();
            if (accUser.get("acc_password", String.class) != null) {
                // if the email exists and password is correct, then write the setupEmail info in collection into server prompts                
                Bson filter = eq("acc_email", email.toString());
                Bson updateOperation = set("firstName", fn);
                usersDB.updateOne(filter, updateOperation);
                
                updateOperation = set("lastName", ln);
                usersDB.updateOne(filter, updateOperation);
                
                updateOperation = set("displayName", dn);
                usersDB.updateOne(filter, updateOperation);
                
                updateOperation = set("fromEmail", fe);
                usersDB.updateOne(filter, updateOperation);
                
                updateOperation = set("fromPassword", fp);
                usersDB.updateOne(filter, updateOperation);

                updateOperation = set("SMTPHost", smtp);
                usersDB.updateOne(filter, updateOperation);

                updateOperation = set("TLSPort", tlsp);
                usersDB.updateOne(filter, updateOperation);
            } 
        }

        // return that all prompts were written to mongo
        String response = "All setup strings were written to Mango";
        System.out.println("setupEH: " + response);
        return response;
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery(); // ?=fn
        
        if (query != null) {
            String subQuery = query.substring(query.indexOf("=") + 1); // fn
            if (subQuery.equals("fn")) {
                response = firstName.toString();
            } else if (subQuery.equals("ln")) {
                response = lastName.toString();
            } else if (subQuery.equals("dn")) {
                response = displayName.toString();
            } else if (subQuery.equals("fe")) {
                response = fromEmail.toString();
            } else if (subQuery.equals("fp")) {
                response = fromPassword.toString();
            } else if (subQuery.equals("smtp")) {
                response = SMTPHost.toString();
            } else if (subQuery.equals("tlsp")) {
                response = TLSPort.toString();
            }
        }
        System.out.println("setupEH: " + response);
        return response;
    }
}