package sayit;

import java.io.*;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.*;

public class InputQ {

    IMediator mediator;
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";
    private static final String FILE_PATH = "Test-files/question.wav";
    private static final String FILE_PATH_INPUT = "Test-files/InputQ-test.txt";
    //private static String myInputQ = "Mock Transcription Query: This is the mock query!";
    private String audioInput = "";
    private static String questionString = "";


    public InputQ() throws IOException, InterruptedException {
        this.audioInput = FILE_PATH;
        InputTranscription();
    }
    public InputQ(String audString) {
        this.audioInput = audString;
    }
    private static void writeParameterToOutputStream(
        OutputStream outputStream,
        String parameterName,
        String parameterValue,
        String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
            (
                "Content-Disposition: form-data; name =\"" + parameterName + "\"\r\n\r\n"
            ).getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    //recieves audio: for writing the audio file to the output stream
    private static void writeFileToOutputStream(
        OutputStream outputStream,
        File file,
        String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
            (
                "Content-Disposition: form-data; name =\"file\"; filename=\"" + 
                file.getName() + "\"\r\n"
            ).getBytes()
        );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }
    

    //responsible for reading the response body from the connection's input stream 
    //and building a string that contains the response data
    
    private static void handleSuccessResponse(HttpURLConnection connection)
        throws IOException, JSONException{
            BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject responseJson = new JSONObject(response.toString());
            String genratedText = responseJson.getString("text");
            questionString = genratedText; 

        }
        
    // response code is not successful
    private static void handleErrorResponse(HttpURLConnection connection) 
    throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader (
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
        questionString = errorResult;
    }

    public String getInputQ() {
        return audioInput;

    }
    public String getTranscription() {
        return questionString;
    }
    public void InputTranscription () throws IOException {

        File file = new File(FILE_PATH);
        
        URL url = new URL(API_ENDPOINT);
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=" + boundary
        );
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        OutputStream outputStream = connection.getOutputStream();

        writeParameterToOutputStream(outputStream, "model",MODEL, boundary);

        writeFileToOutputStream(outputStream, file, boundary);

        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
          
        
        if (responseCode == HttpURLConnection.HTTP_OK) {

            handleSuccessResponse(connection);
        } else {
            handleErrorResponse(connection);

        }
        connection.disconnect();
        
    } 

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(IMediator m) { mediator = m; }
}