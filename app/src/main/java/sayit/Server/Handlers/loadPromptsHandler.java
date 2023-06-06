package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.net.*;
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

/**
 * In charge of reading and writing prompts to and from mongoDB for a specific email
 */
public class loadPromptsHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
    StringBuilder email;
    static final String MONGO_URI = "mongodb://quistian241:Gura%40241@ac-kumtned-shard-00-00.uikyhue.mongodb.net:27017,ac-kumtned-shard-00-01.uikyhue.mongodb.net:27017,ac-kumtned-shard-00-02.uikyhue.mongodb.net:27017/?ssl=true&replicaSet=atlas-fk9y1w-shard-0&authSource=admin&retryWrites=true&w=majority";

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts  ArrayList of prompts
     */
    public loadPromptsHandler(ArrayList<Prompt> prompts, StringBuilder email) {
        this.prompts = prompts;
        this.email = email;
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
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else {
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
     * Write all prompts in prompts into mongo at given email
     * @param httpExchange the request that the server receives
     * @return response saying whether or not the PUT succeeded
     * @throws IOException
     */
    private String handleGet(HttpExchange httpExchange) throws IOException {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase accDatabase = mongoClient.getDatabase("AccountData");
            MongoCollection<Document> usersDB = accDatabase.getCollection("Users");
            Document accUser = usersDB.find(eq("acc_email",email.toString())).first();
            if (accUser.get("acc_password", String.class) != null) {

                // if the email exists and password is correct, then write the prompts in collection into server prompts
                List<Document> susHist = new ArrayList<>();
                String type, ques, ans;
                for (int i = 0; i < prompts.size(); i++) {
                    type = prompts.get(i).getCommand();
                    ques = prompts.get(i).getQuery();
                    ans = prompts.get(i).getReponse();
                    susHist.add(new Document("Type", type).append("Top", ques).append("Bottom", ans));
                }
                Bson filter = eq("acc_email", email.toString());
                Bson updateOperation = set("promptH", susHist);
                UpdateResult updateResult = usersDB.updateOne(filter, updateOperation);
                // System.out.println(usersDB.find(filter).first().toJson(prettyPrint));
                // System.out.println(updateResult);
            } 
        }

        // return that all prompts were written to mongo
        String response = "All Prompts were written to Mango";
        System.out.println("loadPH: " + response);
        return response;
    }

    /**
     * If the "autoLogin" query is passed, then handle autoLogin procedure with mongo (adding ip and email)
     * Otherwise, read in an email and password, check if they're valid, and handle login (fills in prompts)
     * @param httpExchange the request that the server receives
     * @return String response that says either "Automatic Login", "No Automatic Login", "Valid Login", or "Invalid Login"
     * @throws IOException
     */
    private String handlePut(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            String subQuery = query.substring(query.indexOf("=") + 1);
            if (subQuery.equals("autoLogin")) {
                /*
                 * if the subQuery == autoLogin, then login if the IP is stored in database and Automatic Login
                 * else the IP doesn't exist in the database, so return No Automatic Login 
                 */
                
                /*
                 * store the email associated with that IP into StringBuilder email
                 * do Cristian's reading mongo thing to fill prompts
                 */
                response = "Automatic Login";
                response = "No Automatic Login";
            } 
        } else {
            /* setup reading from some input file */
            InputStream inStream = httpExchange.getRequestBody();
            Scanner scanner = new Scanner(inStream);

            /* get email from first line of file */
            String inputEmail = scanner.nextLine();
            /* get password from second line of file */
            String inputPassword = scanner.nextLine();
            scanner.close();
            
            // Take data from mang and make/put that into the prompt array
            // func. is meant to replace what happens below me
            try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
                MongoDatabase accDatabase = mongoClient.getDatabase("AccountData");
                MongoCollection<Document> usersDB = accDatabase.getCollection("Users");

                // find a list of documents and iterate throw it using an iterator.
                // want to make the new inputs be user's email
                Document accUser = usersDB.find(eq("acc_email", inputEmail)).first();

                /* if the email doesn't exist in the database, return "Invlaid Login"
                 * else if the password does not equals the database password, return "Invalid Login"
                 * else, read into prompts from the database at that email
                 */
                if (accUser == null) {
                    response = "Invalid Login";
                } else if (!inputPassword.equals(accUser.get("acc_password", String.class))) {
                    response = "Invalid Login";
                } else {
                    // set email to the inputEmail
                    email.setLength(0);
                    email.append(inputEmail);
                    // gets us the promptH doc array
                    List<Document> promptHist = (List<Document>) accUser.get("promptH");
                    Document temp;
                    String type;
                    String qLine;
                    String aLine;
                    // iterate through and add the prompts to UI (essentially)
                    for (Object prompt : promptHist) {
                        temp = (Document) prompt;
                        type = (String)temp.get("Type"); // should add a prompt constructor to make use of type
                        qLine = (String)temp.get("Top");
                        aLine = temp.get("Bottom").toString();

                        Prompt questionAndAnswer = new Prompt(type, qLine, aLine, null);
                        prompts.add(questionAndAnswer); // uncomment when the actual testing is ready for this format
                    }
                    response = "Valid Login";
                }
            }
        }
        System.out.println("loadPH: " + response);
        return response;
    }

    /**
     * Read in an email and password, check if the email already exists, and handle creation of account (add to mongo)
     * @param httpExchange the request that the server receives
     * @return response saying either "Email already used" or "Account Created"
     * @throws IOException
     */
    private String handlePost(HttpExchange httpExchange) throws IOException {
        String response;
        /* setup reading from some input file */
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);

        /* get email from first line of file */
        String inputEmail = scanner.nextLine();
        /* get password from second line of file */
        String inputPassword = scanner.nextLine();
        scanner.close();

        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase accDatabase = mongoClient.getDatabase("AccountData");
            MongoCollection<Document> usersDB = accDatabase.getCollection("Users");
            
            /*
             * check if the email already exists:
             * if it does, return "Email already used"
             * if it doesn't,
             *  add that email and password as a new entry/account into mongo
             *  store the email into the StringBuilder email
             *  return "Account Created"
             */
            Document accUser = usersDB.find(eq("acc_email", inputEmail)).first();
            if (accUser != null) {
                response = "Email already used";
            } else {
                accUser = new Document("_id", new ObjectId());
                accUser.append("acc_email", inputEmail)
                        .append("acc_password", inputPassword)
                        .append("promptH", new ArrayList<>())
                        .append("maybe", "add send email info here? or in another collection");

                usersDB.insertOne(accUser);
                
                // set email to the inputEmail
                email.setLength(0);
                email.append(inputEmail);
                response = "Account Created";
            }
        }
        System.out.println("loadPH: " + response);
        return response;
    }
}