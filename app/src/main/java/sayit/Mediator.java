package sayit;

public class Mediator {
    // objects to all classes used in app
    private AppFrame appFrame;
    private AudioRecord audioRecord;
    private Footer footer;
    private PromptHistory promptHistory;
    private InputQ inputQ;
    private OutputA outputA;
    private Prompt prompt;
    private ScrollFrame scrollFrame;

    // other miscellaneous variables
    public String filePath = "src/main/java/sayit/Test-files/test-1.txt";

    // default constructor
    Mediator() {
        /* Create new AppFrame */
        appFrame = new AppFrame();
        appFrame.setMediator(this);

        /* Create new AudioRecord */
        audioRecord = new AudioRecord();
        audioRecord.setMediator(this);

        /* Create new Footer */
        footer = new Footer();
        footer.setMediator(this);

        /* Create new PromptHistory */
        promptHistory = new PromptHistory(filePath);
        promptHistory.setMediator(this);

        /* Create new InputQ */
        inputQ = new InputQ();
        inputQ.setMediator(this);

        /* Create new OutputQ */
        outputA = new OutputA();
        outputA.setMediator(this);
        
        /* Create new Prompt */
        prompt = new Prompt("", "");
        prompt.setMediator(this);

        /* Create new ScrollFrame */
        scrollFrame = new ScrollFrame();
        scrollFrame.setMediator(this);
    }

    /**
     * Getter for AppFrame
     * @return appFrame object
     */
    public AppFrame getAppFrame() { return appFrame; }

    /**
     * Getter for AudioRecord
     * @return audioRecord object
     */
    public AudioRecord getAudioRecord() { return audioRecord; }

    /**
     * Getter for Footer
     * @return footer object
     */
    public Footer getFooter() { return footer; }

    /**
     * Getter for PromptHistory
     * @return promptHistory object
     */
    public PromptHistory getPromptHistory() { return promptHistory; }

    /**
     * Getter for InputQ
     * @return inputQ object
     */
    public InputQ getInputQ() { return inputQ; }

    /**
     * Getter for OutputA
     * @return outputA object
     */
    public OutputA getOutputA() { return outputA; }

    /**
     * Getter for Prompt
     * @return prompt object
     */
    public Prompt getPrompt() { return prompt; }

    /**
     * Getter for ScrollFrame
     * @return scrollFrame object
     */
    public ScrollFrame getScrollFrame() { return scrollFrame; }
}