package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * In charge of consulting WhisperAPI, as well as adding prompts manually into ArrayList prompts
 */
public class newQuestionHandler implements HttpHandler {
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
    IAudioRecord audio;
    IInput input;
    IOutput output;

    /**
     * Default constructor that initializes ArrayList prompts
     * @param prompts
     */
    public newQuestionHandler(ArrayList<Prompt> prompts) {
        this.prompts = prompts;
        /* mocked */
        audio = new MockAudioRecord();
        input = new MockInputQ();
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
     * Start or stop recording and get ChatGPT response
     * @param httpExchange the request that the server receives
     * @return String response saying that it's recording, or containing question + /D\ + answer --> (/D\ is the delimeter)
     * @throws IOException
     * @throws InterruptedException
     */
    private String handleGet(HttpExchange httpExchange) throws IOException, InterruptedException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            String recordStatus = query.substring(query.indexOf("=") + 1);

            /* if the recordStatus says "Start", start recording and return success response */
            if (recordStatus.equals("Start")) {
                audio.startRecording();
                response = "Recording Successfully";
                System.out.println("newQH: " + response);
            }

            /* if the recordStatus says "Stop", stop recording, input into whisper, and return response of Q&A */
            if (recordStatus.equals("Stop")) {
                audio.stopRecording();
                input.InputTranscription();
                output.makeAnswer(input.getTranscription());
                
                String question = input.getTranscription();
                String answer = output.getAnswer();

                Prompt prompt = new Prompt(question, answer);
                prompts.add(prompt);

                /* set response to question + /D\ + answer --> (/D\ is the delimeter) */
                response = question + "/D\\" + answer;
                System.out.println("newQH: " + response);
            }
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
        String response = "Posted entry:" + "\nQuestion: " + question + "\nAnswer: " + answer;
        System.out.println("newQH: " + response);
        scanner.close();

        return response;
    }
}