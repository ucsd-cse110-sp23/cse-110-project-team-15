package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.IOutput;
import sayit.Server.BusinessLogic.OutputA;
import sayit.Server.BusinessLogic.Prompt;

import java.io.*;
import java.util.*;

/**
 * In charge of creating an email by consulting ChatGPT to get the contents for an email subject
 */
public class createEmailHandler implements HttpHandler, ICEH {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
    IOutput output;

    /**
     * Default constructor that initializes ArrayList prompts and output
     * @param prompts ArrayList of prompts
     */
    public createEmailHandler(ArrayList<Prompt> prompts) {
        this.prompts = prompts;
        output = new OutputA();
    }

    /**
     * Receives request and appropriately calls either GET, POST, PUT, or DELETE
     * @param httpExchange the request that the server receives
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("createEmailHandler.java: An erroneous request");
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
     * With the inputted email subject, get the ChatGPT response (email contents) for it
     * @param httpExchange the request that the server receives
     * @return String response containing to the inputted email subject's contents
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public String handlePut(HttpExchange httpExchange) throws IOException, InterruptedException {
        String response = "Invalid PUT request";

        /* read email subject from request (file), input into whisper, and return ChatGPT response to subject */
        /* setup reading from some input file */
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        
        /* set command to "Create Email" */
        String command = "Create Email";

        /* get subject from first line of file */
        String subject = scanner.nextLine();
        scanner.close();

        /* get email contents for the subject */
        output.makeAnswer(subject);
        String content = output.getAnswer();
        // add display name and best regards line here

        /* add the prompt to prompts */
        Prompt prompt = new Prompt(command, subject, content, null);
        prompts.add(prompt);

        /* set response to answer */
        response = content;
        System.out.println("createEH: " + response);
        return response;
    }
}