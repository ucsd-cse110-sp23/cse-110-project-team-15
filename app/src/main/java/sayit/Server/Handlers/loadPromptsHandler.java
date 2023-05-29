package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * In charge of getting a prompt at a given index, and recording prompts into preserve.txt
 */
public class loadPromptsHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
    String filePath;

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts ArrayList of prompts
     * @param filePath String of path to file to be written to
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
        // path to preserve.txt
        // String filePath = "src/main/java/sayit/Server/Handlers/preserve.txt";

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