package sayit;

public class MockInputQ implements Input {
    private static String mockInputString = "";
    public MockInputQ() {

        mockInputString = "This is the mock question?";
    }

    public String getTranscription() {
        return mockInputString;
    }
}