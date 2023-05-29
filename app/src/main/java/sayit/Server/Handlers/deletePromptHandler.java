package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * In charge of delete a prompt in ArrayList prompts at a given index
 */
public class deletePromptHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts ArrayList of prompts
     */
    public deletePromptHandler(ArrayList<Prompt> prompts) {
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
            System.out.println("deletePromptHandler.java: An erroneous request" + method);
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
     * Delete prompt from ArrayList prompts at given index
     * @param httpExchange the request that the server receives
     * @return String response saying what prompt was deleted at what index
     * @throws IOException
     */
    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid Delete request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        
        if (query != null) {
            int index = Integer.parseInt(query.substring(query.indexOf("=") + 1));
            
            String question = null;
            String answer = null;

            /* Store the question and answer at given index and delete */
            question = prompts.get(index).getQuery();
            answer = prompts.get(index).getAnswer();
            prompts.remove(index);

            /* set response to the deleted question and answer */
            response = "Deleted at index: " + index + "\nQuestion: " + question + "\nAnswer: " + answer;
            System.out.println("deletePH: " + response);
        }
        return response;
    }
}