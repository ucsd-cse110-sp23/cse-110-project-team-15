package sayit.Server;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;
import sayit.Server.Handlers.MockNewQuestionHandler;
import sayit.Server.Handlers.clearAllHandler;
import sayit.Server.Handlers.createEmailHandler;
import sayit.Server.Handlers.deletePromptHandler;
import sayit.Server.Handlers.indexHandler;
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
public class Server {
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
    public static void startServer() throws IOException, InterruptedException {
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

        // setup and start the server
        server.createContext("/index", new indexHandler(prompts));
        server.createContext("/load", new loadPromptsHandler(prompts, email));
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
}

