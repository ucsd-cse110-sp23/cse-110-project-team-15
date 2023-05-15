package sayit;

import java.io.*;
import java.net.*;
import java.net.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class OutputA {
    private IMediator mediator;
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    private static final String FILE_PATH_OUTPUT = "Test-files/OutputA-test.txt";
    private static final String FILE_PATH_INPUT = "Test-files/InputQ-test.txt";
    private  int maxTokens = 100;
   
    private String prompt = "";
    private String answer = "";

    public OutputA(String question) throws IOException, InterruptedException {
        this.prompt = question;
        this.answer = AnswerOutput();
    }

    public OutputA(){}
    
    public String AnswerOutput () throws IOException, InterruptedException {

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", this.prompt);
        requestBody.put("max_tokens", this.maxTokens);
        requestBody.put("temperature", 1.0);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

        HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
        );

        String responseBody = response.body();
        

        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");

        return generatedText;
    }

    public String getAnswer() {
        return this.answer;
    }
 
    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(IMediator m) { mediator = m; }
}