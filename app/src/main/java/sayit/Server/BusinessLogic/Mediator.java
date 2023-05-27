package sayit.Server.BusinessLogic;
import java.io.*;

/**
 * Class that acts as a central hub for all instances of other classes in app
 * Purpose is so that instances used in app (i.e. prompts array, scrollFrame panel, appFrame panel, etc) are in one place
 * All other classes come through mediator (except for Prompt class) to get instance of another class
 */
class Mediator implements IMediator {
    // Objects to all classes used in app
    private IAudioRecord audioRecord;
    private PromptHistory promptHistory;
    private Input inputQ;
    private Output outputA;
    private Prompt prompt;

    // other miscellaneous variables
    public final String FILE_PATH = "src/main/java/sayit/Test-files/prompts.txt";

    // default constructor
    public Mediator() throws IOException, InterruptedException {
        /* Create new Prompt */
        prompt = new Prompt("", "");
        prompt.setMediator(this);
        
        /* Create new PromptHistory */
        promptHistory = new PromptHistory();
        promptHistory.setMediator(this);
        promptHistory.setupPromptHistory(FILE_PATH);

        /* Create new AudioRecord */
        audioRecord = new AudioRecord();
        audioRecord.setMediator(this);

        /* Create new InputQ */
        inputQ = new InputQ();
        inputQ.setMediator(this);

        /* Create new OutputA */
        outputA = new OutputA();
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