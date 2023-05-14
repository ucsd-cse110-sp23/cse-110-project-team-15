package sayit;

import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject; 

public class OutputA {
    Mediator mediator;
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    private static final String FILE_PATH_OUTPUT = "src/main/java/sayit/Test-files/OutputA-test.txt";
    private static final String FILE_PATH_INPUT = "src/main/java/sayit/Test-files/InputQ-test.txt";
    private String mockGeneratedA = "This is the mock answer!";
   
    public String getOutputA() {
        return mockGeneratedA;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String prompt = "";

        try {
            FileReader fileReader = new FileReader(FILE_PATH_INPUT);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((prompt = bufferedReader.readLine()) != null) {

            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        /* 
        int maxTokens = 100; 
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
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
  
        try {
            FileWriter fileWriter = new FileWriter("Test-files/OutputA-test.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(generatedText);
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        */

        String mockGeneratedText = "This is the mock answer!";
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH_OUTPUT);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(mockGeneratedText);
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        


    }

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(Mediator m) { mediator = m; }
 
}