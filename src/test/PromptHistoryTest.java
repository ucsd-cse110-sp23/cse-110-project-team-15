import SayIt.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PromptHistoryTest {
    private GetPromptHistory myHistory;
    private GetPromptHistory newLineHistory;

    @BeforeEach
    void setup(){
        myHistory = new GetPromptHistory("src/test/test-3.txt"); //Use my six line test file
        newLineHistory = new GetPromptHistory("src/test/empty.txt");
    }

    @Test
    void testNonEmpty(){
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
        assertEquals(myHistory.getSize(), 3);
    }

    @Test
    void testKindaEmpty(){
        //Pair 1:
        assertEquals(newLineHistory.getQuery(0), ""); //I think thats how the interaction would work if not then ""
        assertEquals(newLineHistory.getAnswer(0), "");
        
        // Size Comparison
        assertEquals(newLineHistory.getSize(), 1);
    }
}
