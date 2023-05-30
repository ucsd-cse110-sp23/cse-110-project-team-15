package sayit.Server.Handlers;

import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;

public interface ISH {
    void handle(HttpExchange httpExchange) throws IOException;
    String handleGet(HttpExchange httpExchange) throws IOException, InterruptedException;
}
