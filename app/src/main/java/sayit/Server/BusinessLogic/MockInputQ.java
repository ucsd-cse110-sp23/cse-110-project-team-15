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
        // mockInputString = "new question haha fart?";
        // mockInputString = "send email to taylorvon525 at gmail dot com.";
        //mockInputString = "Create email to jill to meet";
        mockInputString = "setup email";
        //mockInputString = "delete prompt";
        //mockInputString = "clear all";
    }
}