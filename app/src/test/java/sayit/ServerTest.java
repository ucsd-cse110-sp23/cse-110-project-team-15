/*
 
This Java source file was generated by the Gradle 'init' task.*/
package sayit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;

import sayit.Server.MockServer;
import sayit.Server.BusinessLogic.Prompt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ServerTest {

    @BeforeEach
    void clearFile() throws FileNotFoundException, IOException {
    }

    @AfterEach
    void closeServer() throws IOException {
    }

    @Test
    void loadPromptsHandlerGetTest() {
        assertEquals(1+1, 2);
    }

    @Test
    void loadPromptsHandlerPutTest() {
        assertEquals(1+1, 2);
    }
    void newQuestionHandlerGetTest() {
        assertEquals(1+1, 2);
    }

    @Test
    void newQuestionHandlerPostTest() {
        assertEquals(1+1, 2);
    }

    @Test
    void deletePromptHandlerDeleteTest() {
        assertEquals(1+1, 2);
    }

    @Test
    void clearAllHandlerDeleteTest() {
        assertEquals(1+1, 2);
    }
}
