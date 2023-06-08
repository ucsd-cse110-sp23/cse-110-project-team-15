package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.Prompt;
import sayit.Server.BusinessLogic.Emails.TLSEmail;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Responsible for sending an email (subject and its contents) to some inputted email
 */
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
            if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("sendEmailHandler.java: An erroneous request");
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
     * Send an email depending on input from request
     * @param httpExchange the request that the server receives
     * @return response saying whether or not the POST succeeded
     * @throws IOException
     */
    private String handlePost(HttpExchange httpExchange) throws IOException {
        /* setup reading from some input file */
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        
        /* set command to "Send Email" */
        String command = "Send Email";

        /* get fromEmail from first line of file */
        String fromEmail = scanner.nextLine();
        /* get password from second line of file */
        String password = scanner.nextLine();
        /* get toEmail from third line of file */
        String toEmail = scanner.nextLine();
        /* get SMTPHost from fourth line of file */
        String SMTPHost = scanner.nextLine();
        /* get TLSPort from fifth line of file */
        String TLSPort = scanner.nextLine();
        /* get subject from sixth line of file */
        String subject = scanner.nextLine();
        /* get body from the rest line of file */
        String body = "";
        while (scanner.hasNext()) { body += scanner.nextLine() + "\n"; }
        body = body.trim();
        scanner.close();
        
        /* 
         * if the email is successfully sent, set response to "Email Successfully Sent"
         * otherwise, set response to "Error with Email"
         */
        String response;
        TLSEmail em = new TLSEmail();
        if(em.TLSSendEmail(fromEmail, password, toEmail, SMTPHost, TLSPort, subject, body)) {
            response = "Email Successfully Sent";
        } else {
            response = "Error with SMTP Host";
        }

        /* add the prompt to prompts */
        Prompt prompt = new Prompt(command, subject, response, null);
        prompts.add(prompt);

        // return that email was successfully/unsuccessfully sent
        System.out.println(response);
        return response;
    }
}