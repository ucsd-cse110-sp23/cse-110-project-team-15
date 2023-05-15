package sayit;

public class MockOutputA implements Output {

    IMediator mediator;
    String mockPrompt = "";
    String mockAnswer = "This is the mock answer!!!cwasont :)";

    public MockOutputA(String question) {
        this.mockPrompt = question;
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