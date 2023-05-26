package sayit;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Responsible for starting the server
 */
public class Server {
    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    private static HttpServer server;
    
    /**
     * Starts the server at the given port and hostname
     * @throws IOException
     */
    public static void startServer() throws IOException {
        // create a thread of pool to handle requests
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        // create an ArrayList to store prompts
        ArrayList<Prompt> prompts = new ArrayList<Prompt>();

        // create a server
        server = HttpServer.create(
        new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
            0
        );

        // setup and start the server
        server.createContext("/", new RequestHandler(prompts));
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

