/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package sayit;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputandOutputTest {

    private Input inputQ;
    private Output outputA;
    private static final String FILE_PATH_INPUT = "src/test/java/sayit/Test-files/InputQ-test.txt";
    private static final String FILE_PATH_OUTPUT = "src/test/java/sayit/Test-files/OutputA-test.txt";
    private String mockQuestion = "";

    @BeforeEach
    void setup(){
         inputQ = new MockInputQ();
         outputA = new MockOutputA(inputQ.getTranscription());
     }

    @Test
    void test_input() {

        assertEquals("This is the mock question?", inputQ.getTranscription());
      
    }

    @Test
    void test_output() {
        assertEquals("This is the mock answer!!! :)", outputA.getAnswer());
    }
    
}