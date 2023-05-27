package sayit.Server.BusinessLogic;

/**
 * Mock the Mediator class for testing purposes
 */
public class MockMediator implements IMediator {
    // Objects to all classes used in app
    private IAudioRecord audioRecord;
    private PromptHistory promptHistory;
    private Input inputQ;
    private Output outputA;
    private Prompt prompt;

    // other miscellaneous variables
    public final String FILE_PATH = "src/main/java/sayit/Test-files/prompts.txt";

    // default constructor
    public MockMediator() {
        /* Create new Prompt */
        prompt = new Prompt("", "");
        prompt.setMediator(this);
        
        /* Create new PromptHistory */
        promptHistory = new PromptHistory();
        promptHistory.setMediator(this);
        promptHistory.setupPromptHistory(FILE_PATH);

        /* Create new AudioRecord */
        audioRecord = new MockAudioRecord();
        audioRecord.setMediator(this);

        /* Create new MockInputQ */
        inputQ = new MockInputQ();
        inputQ.setMediator(this);

        /* Create new MockOutputA */
        outputA = new MockOutputA();
        outputA.setMediator(this);
    }

    /**
     * Getter for AudioRecord
     * @return audioRecord object
     */
    public IAudioRecord getAudioRecord() { return audioRecord; }

    /**
     * Getter for PromptHistory
     * @return promptHistory object
     */
    public PromptHistory getPromptHistory() { return promptHistory; }

    /**
     * Getter for InputQ
     * @return inputQ object
     */
    public Input getInputQ() { return inputQ; }

    /**
     * Getter for OutputA
     * @return outputA object
     */
    public Output getOutputA() { return outputA; }

    /**
     * Getter for Prompt
     * @return prompt object
     */
    public Prompt getPrompt() { return prompt; }
}
