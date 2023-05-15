package sayit;

public class MockInputQ extends InputQ {
    IMediator mediator;
    private static String mockInputString = "";
    public MockInputQ() {

        super(mockInputString);
        mockInputString = "Mock Transcription Query: This is the mock queru!";
    }
    public String getTranscription() {
        return mockInputString;
    }

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(IMediator m) { mediator = m; }
}
