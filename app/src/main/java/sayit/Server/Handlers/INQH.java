package sayit.Server.Handlers;

import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;

public interface INQH {
    void handle(HttpExchange httpExchange) throws IOException;
    String handlePut(HttpExchange httpExchange) throws IOException, InterruptedException;
    String handlePost(HttpExchange httpExchange) throws IOException;
}
