package sayit.Server.BusinessLogic;

import java.io.IOException;

public class MockOutputA implements IOutput {
    String mockPrompt = "";
    String mockAnswer = "This is the mock answer!!! :)";

    public MockOutputA() {}

    public void makeAnswer(String question) throws IOException, InterruptedException {
        this.mockPrompt = question;
        mockAnswer = "This is the mock answer!!! :)";
    }

    public String getAnswer() {
        return this.mockAnswer;
    }
}