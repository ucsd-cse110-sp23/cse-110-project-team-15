package sayit;

import java.io.IOException;

public class MockOutputA implements Output {

    IMediator mediator;
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
    
    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(IMediator m) { mediator = m; }
}