package sayit.Server.BusinessLogic;

public interface IAudioRecord {
    void startRecording();
    void stopRecording();
    void setMediator(IMediator m);
}