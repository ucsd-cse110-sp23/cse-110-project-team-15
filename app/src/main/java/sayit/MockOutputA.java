package sayit;

public class MockOutputA implements Output {

    String mockPrompt = "";
    String mockAnswer = "This is the mock answer!!! :)";

    public MockOutputA(String question) {
        this.mockPrompt = question;
    }

    public String getAnswer() {
        return this.mockAnswer;
    }
    
}