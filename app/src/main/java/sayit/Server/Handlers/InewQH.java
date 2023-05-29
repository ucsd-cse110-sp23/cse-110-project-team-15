package sayit.Server.Handlers;

import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;

public interface InewQH {
    void handle(HttpExchange httpExchange) throws IOException;
    String handleGet(HttpExchange httpExchange) throws IOException, InterruptedException;
    String handlePost(HttpExchange httpExchange) throws IOException;
}
