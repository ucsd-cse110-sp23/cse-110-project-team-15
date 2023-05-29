package sayit.Server.BusinessLogic;

import java.io.IOException;

public interface IInput {
    void InputTranscription() throws IOException;
    String getTranscription();
}