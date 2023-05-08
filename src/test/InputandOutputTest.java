//package main;
import SayIt.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

import SayIt.GetPromptHistory;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InputandOutputTest {

    private InputQ inputQ;
    private OutputA outputA;

    @BeforeEach
    void setup(){
       // myHistory = new GetPromptHistory("src/main/Test-files/test-1.txt"); //Use my six line test file
        //newLineHistory = new GetPromptHistory("src/main/Test-files/test-1.txt");
        inputQ = new InputQ();
    }

    @Test
    void test() {

        String prompt = "";

        try {
            FileReader fileReader = new FileReader("Test-files/InputQ-test.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((prompt = bufferedReader.readLine()) != null) {

            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        System.out.println("prompt: " + prompt);
        assertEquals("Mock Transcription Query: This is the mock query!", prompt);
    }
    
}
