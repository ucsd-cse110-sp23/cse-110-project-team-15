package sayit.Server;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;
import sayit.Server.Handlers.MockNewQuestionHandler;
import sayit.Server.Handlers.MockStartHandler;
import sayit.Server.Handlers.clearAllHandler;
import sayit.Server.Handlers.createEmailHandler;
import sayit.Server.Handlers.deletePromptHandler;
import sayit.Server.Handlers.devHandler;
import sayit.Server.Handlers.loadPromptsHandler;
import sayit.Server.Handlers.newQuestionHandler;
import sayit.Server.Handlers.sendEmailHandler;
import sayit.Server.Handlers.startHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Responsible for starting the server
 */
public class MockServer {
    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    private static HttpServer server;
    
    // Store username and passwords
    private static Map<String, String> userCredentials = new HashMap<String, String>();

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
        // create a StringBuilder (to pass by reference) to store the email
        StringBuilder email = new StringBuilder();

        // create a server
        server = HttpServer.create(
        new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
            0
        );

        // restore prompts preserved from previous session
        restore(prompts, filePath);

        // setup and start the server
        server.createContext("/dev", new devHandler(prompts));
        server.createContext("/load", new loadPromptsHandler(prompts, email));
        server.createContext("/start", new MockStartHandler());
        server.createContext("/newQuestion", new MockNewQuestionHandler(prompts));
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
     */
    public static void restore(ArrayList<Prompt> prompts, String filePath) {
        // path to preserve.txt
        // String filePath = "src/main/java/sayit/Server/Handlers/preserve.txt";
        
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

