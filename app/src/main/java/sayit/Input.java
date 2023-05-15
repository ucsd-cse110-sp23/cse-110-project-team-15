package sayit;

import java.io.IOException;

public interface Input {
    void InputTranscription() throws IOException;
    String getTranscription();
    void setMediator(IMediator m);
}