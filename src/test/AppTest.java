//package main;
import SayIt.*;
import org.junit.jupiter.api.Test;

import SayIt.GetPromptHistory;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppTest {
    private GetPromptHistory myHistory;
    private GetPromptHistory newLineHistory;

    @Test
    void test() {
        assertEquals(0,0);
    }
    @BeforeEach
    void setup(){
        myHistory = new GetPromptHistory("src/main/Test-files/test-1.txt"); //Use my six line test file
        newLineHistory = new GetPromptHistory("src/main/Test-files/test-1.txt");
    }

}
