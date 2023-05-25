package sayit;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RequestHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts
     */
    public RequestHandler(ArrayList<Prompt> prompts) {
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
            } 
            // not sure when put/update will be needed, so I commented it out for now
            // else if (method.equals("PUT")) {
            //     response = handlePut(httpExchange);
            // } 
            else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
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

            /* if question was found, return its answer, else return not found */
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
        while (((answer += scanner.nextLine()) != null)) {}
        
        // Store question and answer in prompts
        prompts.add(new Prompt(question, answer));

        // return that prompt was added/posted
        String response = "Posted entry {" + question + ", " + answer + "}";
        System.out.println(response);
        scanner.close();

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

    /**
     * Delete a prompt from ArrayList prompts
     * @param httpExchange the request that the server receives
     * @return response saying whether or not the DELETE succeeded
     * @throws IOException
     */
    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid Delete request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String question = query.substring(query.indexOf("=") + 1);
            question = question.replaceAll("%20", " ");
            String answer = null;
            
            /* Search through prompts, find question that matches query, set reponse to answer */
            int i = 0;
            for (Prompt p: prompts) {
                if (p.getQuery().equals(question)) {
                    answer = p.getAnswer();
                    break;
                }
                i++;
            }

            /* if question was found, delete it and its answer, else return not found */
            if (answer != null) {
                prompts.remove(i);
                response = "Deleted entry {" + question + ", " + answer + "}";
                System.out.println("Deleted entry {" + question + ", " + answer + "}");
            } else {
                response = "No data found for " + question;
            }
        }
        return response;
    }
}