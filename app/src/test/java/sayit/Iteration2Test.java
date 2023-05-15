package sayit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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
public class Iteration2Test {
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
    void setup(){
        myHistory = new PromptHistory(); //Use my six line test file
        myHistory.setupPromptHistory(FILE_TEST3_PATH);
        newLineHistory = new PromptHistory();
        newLineHistory.setupPromptHistory(FILE_EMPTY_PATH);
        newLineHistory.clearPrompts();

        inputQ = new MockInputQ();
        outputA = new MockOutputA();
    }

    @Test
    void testClearAllAddFull() throws IOException, InterruptedException {
        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p1 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        myHistory.addPrompt(p1);

        // make sure ArrayList is not empty
        assertNotEquals(0, myHistory.getSize());
        
        // make sure that the clearAll clears the ArrayList
        myHistory.clearPrompts();
        assertEquals(0, myHistory.getSize());

        // make sure that the clearAll keeps ArrayList size 0
        myHistory.clearPrompts();
        assertEquals(0, myHistory.getSize());

        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p2 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        myHistory.addPrompt(p2);

        // make sure that prompt is contained in prompts and size incremented
        assertEquals(1, myHistory.getSize());
        assertEquals("This is the mock question?", myHistory.getQuery(0));
        assertEquals("This is the mock answer!!! :)", myHistory.getAnswer(0));

        
    }

    @Test
    void testClearAllAddEmpty() throws IOException, InterruptedException {
        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p1 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        newLineHistory.addPrompt(p1);

        // make sure ArrayList is not empty
        assertNotEquals(0, newLineHistory.getSize());
        
        // make sure that the clearAll clears the ArrayList
        newLineHistory.clearPrompts();
        assertEquals(0, newLineHistory.getSize());

        // make sure that the clearAll keeps ArrayList size 0
        newLineHistory.clearPrompts();
        assertEquals(0, newLineHistory.getSize());

        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p2 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        newLineHistory.addPrompt(p2);

        // make sure that prompt is contained in prompts and size incremented
        assertEquals(1, newLineHistory.getSize());
        assertEquals("This is the mock question?", newLineHistory.getQuery(0));
        assertEquals("This is the mock answer!!! :)", newLineHistory.getAnswer(0));
    }

    @Test
    void testRemoveSelectedAddFull() throws IOException, InterruptedException {
        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p1 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        myHistory.addPrompt(p1);
        
        // get the prompts array and initial size it
        ArrayList<Prompt> prompts = myHistory.getHistoryArray();
        int size = prompts.size();

        assertEquals(size, 4);
        
        // remove 1 from front
        Prompt p = prompts.get(0);
        prompts.remove(0);
        size--;
        myHistory.removePrompt(p);
        assertEquals(size, myHistory.getSize());

        // remove from somwhere in middle
        p = prompts.get(size/2);
        prompts.remove(size/2);
        size--;
        myHistory.removePrompt(p);
        assertEquals(size, myHistory.getSize());

        // remove 1 from end
        for (int i = size - 1; i < size; i++) {
            p = prompts.get(i);
            prompts.remove(i);
            size--;
            myHistory.removePrompt(p);
        }
        assertEquals(size, myHistory.getSize());

        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p2 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        myHistory.addPrompt(p2);
        size++;

        // make sure that prompt is contained in prompts and size incremented
        assertEquals(size, myHistory.getSize());
        assertEquals("This is the mock question?", myHistory.getQuery(size-1));
        assertEquals("This is the mock answer!!! :)", myHistory.getAnswer(size-1));
    }

    @Test
    void testRemoveSelectedAddEmpty() throws IOException, InterruptedException {
        // make and add prompt to prompts 4 times
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p1 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        newLineHistory.addPrompt(p1);

        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p2 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        newLineHistory.addPrompt(p2);

        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p3 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        newLineHistory.addPrompt(p3);

        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p4 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        newLineHistory.addPrompt(p4);
        
        // get the prompts array and initial size it
        ArrayList<Prompt> prompts = newLineHistory.getHistoryArray();
        int size = prompts.size();

        assertEquals(size, 4);
        
        // remove 1 from front
        Prompt p = prompts.get(0);
        prompts.remove(0);
        size--;
        newLineHistory.removePrompt(p);
        assertEquals(size, newLineHistory.getSize());

        // remove from somwhere in middle
        p = prompts.get(size/2);
        prompts.remove(size/2);
        size--;
        newLineHistory.removePrompt(p);
        assertEquals(size, newLineHistory.getSize());

        // remove 1 from end
        for (int i = size - 1; i < size; i++) {
            p = prompts.get(i);
            prompts.remove(i);
            size--;
            newLineHistory.removePrompt(p);
        }
        assertEquals(size, newLineHistory.getSize());

        // make and add prompt to prompts
        outputA.makeAnswer(inputQ.getTranscription());
        Prompt p5 = new Prompt(inputQ.getTranscription(), outputA.getAnswer());
        newLineHistory.addPrompt(p5);
        size++;

        // make sure that prompt is contained in prompts and size incremented
        assertEquals(size, newLineHistory.getSize());
        assertEquals("This is the mock question?", newLineHistory.getQuery(size-1));
        assertEquals("This is the mock answer!!! :)", newLineHistory.getAnswer(size-1));
    }
}