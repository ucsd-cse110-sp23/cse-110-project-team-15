package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.net.*;
import java.util.*;

public class sendEmailHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts
     */
    public sendEmailHandler(ArrayList<Prompt> prompts) {
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
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } 
            // not sure when put/update will be needed, so I commented it out for now
            // else if (method.equals("PUT")) {
            //     response = handlePut(httpExchange);
            // } 
            else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("RequestHandler.java: An erroneous request");
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
     * @return String response containing question + /D\ + answer, otherwise -1 --> (/D\ is the delimeter)
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
     * Add a prompt to ArrayList prompts
     * @param httpExchange the request that the server receives
     * @return response saying whether or not the POST succeeded
     * @throws IOException
     */
    private String handlePost(HttpExchange httpExchange) throws IOException {
        /* setup reading from some input file */
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        
        /* get question from first line of file */
        String question = scanner.nextLine();
        /* get answer from rest of the files contents */
        String answer = "";
        while (scanner.hasNextLine()) { answer += scanner.nextLine(); }
        
        // Store question and answer in prompts
        prompts.add(new Prompt(question, answer));

        // return that prompt was added/posted
        String response = "Posted entry {" + question + ", " + answer + "}";
        System.out.println(response);
        scanner.close();

        return response;
    }

    /**
     * Delete a prompt from ArrayList prompts
     * @param httpExchange the request that the server receives
     * @return String response saying whether or not the DELETE succeeded (-1 if not succeeded)
     * @throws IOException
     */
    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid Delete request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        
        if (query != null) {
            int index = Integer.parseInt(query.substring(query.indexOf("=") + 1));
            
            /* special case if query of -1 is passed, clear the prompts array */
            if (index == -1) { 
                prompts.clear();
                return "Cleared all entries"; 
            }
            
            /* if the index is larger than array, just return -1 */
            if (index >= prompts.size()) { return "-1"; }
            
            String question = null;
            String answer = null;

            /* Store the question and answer at given index and delete */
            question = prompts.get(index).getQuery();
            answer = prompts.get(index).getAnswer();
            prompts.remove(index);

            /* set response to the deleted question and answer */
            response = "Deleted entry {" + question + ", " + answer + "}";
            System.out.println("Deleted entry {" + question + ", " + answer + "}");
        }
        return response;
    }

    /**
     * Update a prompt in ArrayList prompts (I don't know when this would be needed, so I commented it out)
     * @param httpExchange the request that the server receives
     * @return response saying whether or not the POST succeeded
     * @throws IOException
     */
    // private String handlePut(HttpExchange httpExchange) throws IOException {
    //     InputStream inStream = httpExchange.getRequestBody();
    //     Scanner scanner = new Scanner(inStream);
    //     String postData = scanner.nextLine();
    //     String language = postData.substring(
    //             0,
    //             postData.indexOf(",")), year = postData.substring(postData.indexOf(",") + 1);

    //     // Store data in hashmap

    //     String response;
    //     if (data.containsKey(language)) {
    //         String prevyear = data.get(language);
    //         data.put(language, year);
    //         response = "Updated entry {" + language + ", " + year + "}" + " (previous year:" + prevyear + ")";
    //     } else {
    //         data.put(language, year);
    //         response = "Posted entry {" + language + ", " + year + "}";
    //     }
    //     System.out.println(response);
    //     scanner.close();

    //     return response;
    // }
}