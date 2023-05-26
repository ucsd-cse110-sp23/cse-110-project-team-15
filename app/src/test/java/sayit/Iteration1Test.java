package sayit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * empty
 * load
 * add
 * delete
 * clear all
 */
public class Iteration1Test {
    private PromptHistory myHistory;
    private PromptHistory newLineHistory;
    private final String FILE_TEST3_PATH = "src/test/java/sayit/Test-files/test-3.txt";
    private final String FILE_EMPTY_PATH = "src/test/java/sayit/Test-files/empty.txt";

    private Input inputQ;
    private Output outputA;
    private static final String FILE_PATH_INPUT = "src/test/java/sayit/Test-files/InputQ-test.txt";
    private static final String FILE_PATH_OUTPUT = "src/test/java/sayit/Test-files/OutputA-test.txt";
    private String mockQuestion = "";

    @BeforeEach
    void setup() throws IOException{
        myHistory = new PromptHistory(); //Use my six line test file
        myHistory.setupPromptHistory(FILE_TEST3_PATH);
        newLineHistory = new PromptHistory();
        newLineHistory.setupPromptHistory(FILE_EMPTY_PATH);
        newLineHistory.clearPrompts();

        inputQ = new MockInputQ();
        outputA = new MockOutputA();

        Server.startServer();
    }

    @AfterEach
    void closeServer() throws IOException {
        Server.stopServer();
    }

    @Test
    void testNewQuestionFull() throws IOException, InterruptedException {
        //Pair 1:
        assertEquals(myHistory.getQuery(0), "What's the best ice cream flavor");
        assertEquals(myHistory.getAnswer(0), "Obviously it's cookie's and cream");
        
        //Pair 2
        assertEquals(myHistory.getQuery(1), "Cats or dogs");
        assertEquals(myHistory.getAnswer(1), "Dogs are more easily manipulatable so I'd choose them");
        
        //Pair 3
        assertEquals(myHistory.getQuery(2), "Would you go out on a date with me");
        assertEquals(myHistory.getAnswer(2),
             "Unfortunaley I can not, but I would reccommend going outside and touching some grass");

        // Size comparison
        int size = myHistory.getSize();
        assertEquals(size, 3);

        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p1 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        myHistory.addPrompt(p1);
        size++;

        // make sure that prompt is contained in prompts and size incremented
        assertEquals(size, myHistory.getSize());
        assertEquals("This is the mock question?", myHistory.getQuery(size - 1));
        assertEquals("This is the mock answer!!! :)", myHistory.getAnswer(size - 1));
    }

    @Test
    void testNewQuestionEmpty() throws IOException, InterruptedException {
        // Size comparison
        int size = newLineHistory.getSize();
        assertEquals(size, 0);

        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p1 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        newLineHistory.addPrompt(p1);
        size++;

        // make sure that prompt is contained in prompts and size incremented
        assertEquals(size, newLineHistory.getSize());
        assertEquals("This is the mock question?", newLineHistory.getQuery(size - 1));
        assertEquals("This is the mock answer!!! :)", newLineHistory.getAnswer(size - 1));
    }
}
