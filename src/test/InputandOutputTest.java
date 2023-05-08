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

         inputQ = new InputQ();
         outputA = new OutputA();
     }

    @Test
    void test_input() {

        String prompt = "";
        String parent = "";
        
        try {
            FileReader fileReader = new FileReader("src/main/Test-files/InputQ-test.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader); 

            while ((parent = bufferedReader.readLine()) != null) {
                prompt = parent;
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    
        assertEquals("Mock Transcription Query: This is the mock query!", prompt);
    }

    @Test
    void test_output() {

        String answer = "";
        String parent = "";

        try {
            FileReader fileReader = new FileReader("src/main/Test-files/OutputA-test.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader); 

            while ((parent = bufferedReader.readLine()) != null) {
                answer = parent;  
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    
        assertEquals("This is the mock answer!", answer);

    }
    
}