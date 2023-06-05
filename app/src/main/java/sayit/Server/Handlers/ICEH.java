package sayit.Server.Handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public interface ICEH {
    void handle(HttpExchange httpExchange) throws IOException;
    String handlePut(HttpExchange httpExchange) throws IOException, InterruptedException;
}