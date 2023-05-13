package sayit;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PromptHistory {
    Mediator mediator;
    private int size = 0;
    //private final String filepath = "src/main/Test-files/test-1.txt";
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    // Read text file and pull relevant info, store it
    public void setupPromptHistory(String filepath) {
        try {
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;    // query line
            String line2;   // answer line
            
            while (((line = bufferedReader.readLine()) != null)
                    && (line2 = bufferedReader.readLine()) != null ) {
              Prompt questionAndAnswer = new Prompt(line, line2);
              prompts.add(questionAndAnswer);
              size++;
            }
            bufferedReader.close();
            fileReader.close();
          } 
          catch (IOException e){
            System.out.println(e);
          }
          System.out.println("# of prompts is: " + size);
    }

    public ArrayList<Prompt> getHistoryArray() {
        return this.prompts;
    }

    /*
     * Return prompt object given index number
     */
    public Prompt getPrompt(int promptNumber) {
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
        prompts.add(new Prompt(query, answer));
        size++;
        System.out.println("# of prompts is: " + prompts.size());
    }

    /**
     * remove a given prompt from prompts on given index
     * @param index
     */
    public void removePrompt(int index) {
        System.out.println("Deleting prompt: " + (index + 1));
        prompts.remove(index);
        size--;
        System.out.println("# of prompts is: " + prompts.size());
    }

    /**
     * Clear all prompts in the ArrayList and set size to 0
     */
    public void clearPrompts() {
        prompts.clear();
        size = 0;
    }

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(Mediator m) { mediator = m; }
}
