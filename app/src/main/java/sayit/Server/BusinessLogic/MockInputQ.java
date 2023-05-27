package sayit.Server.BusinessLogic;

import java.io.IOException;

public class MockInputQ implements Input {
    IMediator mediator;
    private static String mockInputString = "";
    public MockInputQ() {

        mockInputString = "This is the mock question?";
    }

    public String getTranscription() {
        return mockInputString;
    }

    public void InputTranscription() throws IOException {
        mockInputString = "This is the mock question?";
    }

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(IMediator m) { mediator = m; }
}