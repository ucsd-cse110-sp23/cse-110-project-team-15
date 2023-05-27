package sayit.Server.BusinessLogic;

import java.io.IOException;

public interface IOutput {
    String getAnswer();
    public void makeAnswer(String question) throws IOException, InterruptedException;
}
