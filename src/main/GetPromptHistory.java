package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GetPromptHistory {

    private int size = 0;
    private final String filepath = "src/main/Test-files/test-1.txt";
    private ArrayList<QandA> prompts = new ArrayList<QandA>();

    // Read text file and pull relevant info, store it
    public GetPromptHistory() {
        try {
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;    // query line
            String line2;   // answer line
            
            while (((line = bufferedReader.readLine()) != null)
                    && (line2 = bufferedReader.readLine()) != null ) {
              QandA questionAndAnswer = new QandA(line, line2);
              prompts.add(questionAndAnswer);
              size++;
            }
            bufferedReader.close();
            fileReader.close();
          } 
          catch (IOException e){
            System.out.println(e);
          }
    }

    /*
     * Return prompt object given index number
     */
    public QandA getPrompt(int promptNumber) {
        return this.prompts.get(promptNumber);
    }

    /*
     * Return query text given index number
     */
    public String getQuery(int promptNumber) {
        return this.prompts.get(promptNumber).getQuery();
    }

    /*
     * Return answer text given index number
     */
    public String getAnswer(int promptNumber) {
        return this.prompts.get(promptNumber).getAnswer();
    }

    /*
     * Return size of ArrayList
     */
    public int getSize() {
        return this.size;
    }

    /*
     * Add a prompt to the ArrayList
     */
    public void addPrompt(String query, String answer) {
        prompts.add(new QandA(query, answer));
    }

    public static void main(String args[]) {
        GetPromptHistory a = new GetPromptHistory();
        System.out.println(a.getQuery(0));
        System.out.print(a.getAnswer(0));
    }
}
