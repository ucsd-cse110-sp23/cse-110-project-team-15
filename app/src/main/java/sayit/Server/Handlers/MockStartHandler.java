package sayit.Server.Handlers;

import com.sun.net.httpserver.*;

import sayit.Server.BusinessLogic.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * In charge of consulting WhisperAPI and transcribing the question/prompt
 */
public class MockStartHandler implements HttpHandler, ISH {
    IAudioRecord audio;
    IInput input;

    /**
     * Default constructor
     * @throws InterruptedException
     * @throws IOException
     */
    public MockStartHandler() throws IOException, InterruptedException {
        audio = new MockAudioRecord();
        input = new MockInputQ();
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
     * @return String response saying that it's recording, or containing question
     * @throws IOException
     * @throws InterruptedException
     */
    public String handleGet(HttpExchange httpExchange) throws IOException, InterruptedException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            String recordStatus = query.substring(query.indexOf("=") + 1);

            /* if the recordStatus says "Start", start recording and return success response */
            if (recordStatus.equals("Start")) {
                audio.startRecording();
                response = "Recording Successfully";
                System.out.println("startH: " + response);
            }

            /* if the recordStatus says "Stop", stop recording, input into whisper, and return response of the question */
            else if (recordStatus.equals("Stop")) {
                /* stop recording and call inputQ to process the question.wav audio file */
                audio.stopRecording();
                input.InputTranscription();

                /* set response to the question */
                response = input.getTranscription();
                System.out.println("startH: " + response);
            }
        }
        return response;
    }
}