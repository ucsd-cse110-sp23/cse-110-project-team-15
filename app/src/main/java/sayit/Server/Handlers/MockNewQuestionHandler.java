package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * In charge of consulting WhisperAPI, as well as adding prompts manually into ArrayList prompts
 */
public class MockNewQuestionHandler implements HttpHandler, INQH {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
    IOutput output;

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts ArrayList of prompts
     * @throws InterruptedException
     * @throws IOException
     */
    public MockNewQuestionHandler(ArrayList<Prompt> prompts) throws IOException, InterruptedException {
        this.prompts = prompts;
        output = new MockOutputA();
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
            else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("newQuestionHandler.java: An erroneous request");
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
     * With the inputted question, get the ChatGPT response to it
     * @param httpExchange the request that the server receives
     * @return String response containing to the inputted question answer
     * @throws IOException
     * @throws InterruptedException
     */
    public String handleGet(HttpExchange httpExchange) throws IOException, InterruptedException {
        String response = "Invalid GET request";

        /* read question from request (file), input into whisper, and return ChatGPT response to question */
        /* setup reading from some input file */
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        
        /* get question from first line of file */
        String question = scanner.nextLine();
        scanner.close();

        /* get answer for the question */
        output.makeAnswer(question);
        String answer = output.getAnswer();

        /* add the prompt to prompts */
        Prompt prompt = new Prompt(question, answer);
        prompts.add(prompt);

        /* set response to answer */
        response = answer;
        System.out.println("newQH: " + response);
        return response;
    }

    /**
     * Add a prompt to ArrayList prompts
     * @param httpExchange the request that the server receives
     * @return response saying whether or not the POST succeeded
     * @throws IOException
     */
    public String handlePost(HttpExchange httpExchange) throws IOException {
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
        String response = "Posted entry:" + "\nQuestion: " + question + "\nAnswer: " + answer;
        System.out.println("newQH: " + response);
        scanner.close();

        return response;
    }
}