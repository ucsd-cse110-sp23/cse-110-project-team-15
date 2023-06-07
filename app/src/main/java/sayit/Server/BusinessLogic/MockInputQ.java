package sayit.Server.BusinessLogic;

import java.io.IOException;

public class MockInputQ implements IInput {
    private static String mockInputString = "";
    public MockInputQ() {
        mockInputString = "This is the mock question?";
    }

    public String getTranscription() {
        return mockInputString;
    }

    public void InputTranscription() throws IOException {
        //mockInputString = "new question haha fart?";
        //mockInputString = "create email to jill to fard";
        mockInputString = "setup email";
        //mockInputString = "delete prompt";
        //mockInputString = "clear all";
    }
}