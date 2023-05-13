package sayit;
q
/**
 * Class that acts as a central hub for all instances of other classes in app
 * Purpose is so that instances used in app (i.e. prompts array, scrollFrame panel, appFrame panel, etc) are in one place
 * All other classes come through mediator (except for Prompt class) to get instance of another class
 */
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
    public final String FILE_PATH = "src/main/java/sayit/Test-files/test-1.txt";

    // default constructor
    Mediator() {
        /* Create new Prompt */
        prompt = new Prompt("", "");
        prompt.setMediator(this);
        
        /* Create new PromptHistory */
        promptHistory = new PromptHistory();
        promptHistory.setMediator(this);
        promptHistory.setupPromptHistory(FILE_PATH);

        /* Create new ScrollFrame */
        scrollFrame = new ScrollFrame();
        scrollFrame.setMediator(this);
        scrollFrame.setupScrollFrame();
        
        /* Create new Footer */
        footer = new Footer();
        footer.setMediator(this);

        /* Create new AudioRecord */
        audioRecord = new AudioRecord();
        audioRecord.setMediator(this);

        /* Create new InputQ */
        inputQ = new InputQ();
        inputQ.setMediator(this);

        /* Create new OutputQ */
        outputA = new OutputA();
        outputA.setMediator(this);

        /* Create new AppFrame */
        appFrame = new AppFrame();
        appFrame.setMediator(this);
        appFrame.setupAppFrame();
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