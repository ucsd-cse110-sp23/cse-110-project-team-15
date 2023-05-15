package sayit;

import java.io.IOException;

public interface Output {
    String getAnswer();
    public void makeAnswer(String question) throws IOException, InterruptedException;
    void setMediator(IMediator m);
}
