package sayit.Server;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;
import sayit.Server.Handlers.MockNewQuestionHandler;
import sayit.Server.Handlers.clearAllHandler;
import sayit.Server.Handlers.createEmailHandler;
import sayit.Server.Handlers.deletePromptHandler;
import sayit.Server.Handlers.loadPromptsHandler;
import sayit.Server.Handlers.newQuestionHandler;
import sayit.Server.Handlers.sendEmailHandler;
import sayit.Server.Handlers.startHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.*;

import com.mongodb.client.*;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

/**
 * Responsible for starting the server
 */
public class Server {
    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    private static HttpServer server;

    //Mango Stuff
    static String uri = "mongodb://quistian241:Gura%40241@ac-kumtned-shard-00-00.uikyhue.mongodb.net:27017,ac-kumtned-shard-00-01.uikyhue.mongodb.net:27017,ac-kumtned-shard-00-02.uikyhue.mongodb.net:27017/?ssl=true&replicaSet=atlas-fk9y1w-shard-0&authSource=admin&retryWrites=true&w=majority";
    
    /**
     * Starts the server at the given port and hostname
     * @throws IOException
     * @throws InterruptedException
     */
    public static void startServer(String filePath) throws IOException, InterruptedException {
        // create a thread of pool to handle requests
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        // create an ArrayList to store prompts
        ArrayList<Prompt> prompts = new ArrayList<Prompt>();

        // create a server
        server = HttpServer.create(
        new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
            0
        );

        // restore prompts preserved from previous session
        restore(prompts, filePath);

        // setup and start the server
        server.createContext("/load", new loadPromptsHandler(prompts, filePath));
        server.createContext("/start", new startHandler());
        server.createContext("/newQuestion", new newQuestionHandler(prompts));
        server.createContext("/clearAll", new clearAllHandler(prompts));
        server.createContext("/deletePrompt", new deletePromptHandler(prompts));
        server.createContext("/createEmail", new createEmailHandler(prompts));
        server.createContext("/sendEmail", new sendEmailHandler(prompts));
        server.setExecutor(threadPoolExecutor);
        server.start();
    }

    /**
     * Forcefully stops the server at the given port and hostname
     * @throws IOException
     */
    public static void stopServer() throws IOException {
        server.stop(0);
    }

    /**
     * Fill prompts with the prompts from previous session (preserve.txt)
     * @param prompts 
     * @param userEmail a string for finding a specific user on the mongoDB
     */
    public static void restore(ArrayList<Prompt> prompts, String filePath) {
        // Take data from mang and make/put that into the prompt array
        // func. is meant to replace what happens below me
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accDatabase = mongoClient.getDatabase("AccountData");
            MongoCollection<Document> usersDB = accDatabase.getCollection("Users");

            // find a list of documents and iterate throw it using an iterator.
            // want to make the new inputs be user's email
            Document accUser = usersDB.find(eq("acc_email", "theRushiaIsReal@gmail.com")).first();
            // gets us the promptH doc array
            List<Document> promptHist = (List<Document>) accUser.get("promptH");
            Document temp;
            String qLine;
            String aLine;
            // iterate through and add the prompts to UI (essentially)
            for (Object prompt : promptHist) {
                temp = (Document) prompt;
                System.out.println("Type: " + (temp.get("Type"))); // should add a prompt constructor to make use of type
                System.out.println("Top: " + (qLine = temp.get("Top").toString()));
                System.out.println("Bottom: " + (aLine = temp.get("Bottom").toString()));
                Prompt questionAndAnswer = new Prompt(qLine, aLine);
                prompts.add(questionAndAnswer); // uncomment when the actual testing is ready for this format
            }
        } 
        
        // read from preserve.txt and fill prompts
        final String startSt = "#Start#";
        final String endSt = "#End#";
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lineLoop;
            String qLine = "";    // query line
            String aLine = "";   // answer line
            
            while (((lineLoop = bufferedReader.readLine()) != null)) {
                if (lineLoop.equals(startSt)) {
                    qLine = bufferedReader.readLine();
                } else if (lineLoop.equals(endSt)) {
                    qLine = qLine.trim();
                    aLine = aLine.trim();
                    Prompt questionAndAnswer = new Prompt(qLine, aLine);
                    prompts.add(questionAndAnswer);
                    aLine = "";
                } else {
                    aLine += lineLoop + '\n';
                }
            }
            bufferedReader.close();
            fileReader.close();
          } 
          catch (IOException e){
            System.out.println(e);
          }
    }
}

