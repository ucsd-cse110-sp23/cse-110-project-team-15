package sayit;

public class MockOutputA extends OutputA {

    IMediator mediator;
    String mockPrompt = "";
    String mockAnswer = "";

    public MockOutputA(){}

    public MockOutputA(String question) {

        this.mockPrompt = question;
        this.mockAnswer = AnswerOutput();
    }
    public String AnswerOutput() {
        String answer = "This is the mock answer!";
        return answer;
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