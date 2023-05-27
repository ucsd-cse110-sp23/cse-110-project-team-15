package sayit.Server.BusinessLogic;

/**
 * Mock the AudioRecord class for testing purposes
 */
public class MockAudioRecord implements IAudioRecord{
    // Objects needed for MockAudioRecord
    IMediator mediator;
    
    /**
     * Start recording and create audio file with input audio
     */
    public void startRecording() {}
    
    /** 
     * Stop recording and finalize audio file 
     */
    public void stopRecording() {}

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(IMediator m) { mediator = m; }
}