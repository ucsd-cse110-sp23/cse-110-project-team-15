package sayit.Server.Handlers;

import com.sun.net.httpserver.*;
import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.util.*;

/**
 * In charge of clearing all prompts in ArrayList prompts
 */
public class clearAllHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts ArrayList of prompts
     */
    public clearAllHandler(ArrayList<Prompt> prompts) {
        this.prompts = prompts;
    }

    /**
     * Receives request and appropriately calls either GET, POST, PUT, or DELETE
     * @param httpExchange the request that the server receives
     */
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("clearAllHandler.java: An erroneous request" + method);
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
     * Clear all prompts from ArrayList prompts
     * @param httpExchange the request that the server receives
     * @return String response saying that all prompts were cleared
     * @throws IOException
     */
    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Cleared all prompts";
        prompts.clear();
        System.out.println("clearAH: " + response);
        return response;
    }
}