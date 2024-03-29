package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * In charge of getting a prompt at a given index
 */
public class indexHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts ArrayList of prompts
     */
    public indexHandler(ArrayList<Prompt> prompts) {
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
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("indexHandler.java: An erroneous request");
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
     * Get the command, query (question), and response (answer) at the corresponding index in prompts
     * @param httpExchange the request that the server receives
     * @return String response containing either command + /C\ question + /D\ + answer, otherwise -1 --> (/C\ and /D\ are delimeters)
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

            String command = null;
            String question = null;
            String answer = null;

            /* Store the command, question, and answer at given index into response */
            command = prompts.get(index).getCommand();
            question = prompts.get(index).getQuery();
            answer = prompts.get(index).getReponse();

            /* set response to command + /C\ question + /D\ + answer, otherwise -1 --> (/C\ and /D\ are delimeters) */
            response = command + "/C\\" + question + "/D\\" + answer;
            System.out.println("Prompt at index " + index + " is:\nCommand: " + command + "\nQuestion: " + question + "\nAnswer: " + answer);
        }
        return response;
    }
}