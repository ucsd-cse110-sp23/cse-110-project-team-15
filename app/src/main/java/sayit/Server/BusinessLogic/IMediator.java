package sayit.Server.BusinessLogic;

public interface IMediator {
    public IAudioRecord getAudioRecord();
    public PromptHistory getPromptHistory();
    public Input getInputQ();
    public Output getOutputA();
    public Prompt getPrompt();
}