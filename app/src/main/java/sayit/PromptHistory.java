package sayit;

import java.io.*;
import java.util.ArrayList;

public class PromptHistory {
    // Objects needed for PromptHistory
    IMediator mediator;
    private int size = 0;
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    // other miscellaneous variables
    String startSt = "#Start#";
    String endSt = "#End#";

    /**
     * Read text file and pull relevant info, store it
     * @param String of relative path to txt file
     */
    public void setupPromptHistory(String filepath) {
        try {
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lineLoop;
            String qLine = "";    // query line
            String aLine = "";   // answer line
            
            while (((lineLoop = bufferedReader.readLine()) != null)) {
                if (lineLoop.equals("#Start#")) {
                    qLine = bufferedReader.readLine();
                }
                else if (lineLoop.equals("#End#")) {
                    Prompt questionAndAnswer = new Prompt(qLine, aLine);
                    prompts.add(questionAndAnswer);
                    size++;
                    aLine = "";
                }
                else {
                    aLine += lineLoop + '\n';
                }
            }


            bufferedReader.close();
            fileReader.close();
          } 
          catch (IOException e){
            System.out.println(e);
          }
          System.out.println("# of prompts is: " + size);
    }

    /**
     * Return the prompts ArrayList
     * @return prompts
     */
    public ArrayList<Prompt> getHistoryArray() {
        return this.prompts;
    }

    /**
     * Return prompt object given index number
     * @param int index to prompt
     * @return Prompt at that index
     */
    public Prompt getPrompt(int promptNumber) {
        return this.prompts.get(promptNumber);
    }

    /**
     * Return query text given index number
     * @param int index to query of certain prompt
     * @return query of prompt at that index
     */
    public String getQuery(int promptNumber) {
        return this.prompts.get(promptNumber).getQuery();
    }

    /**
     * Return answer text given index number
     * @param int index to answer of certain prompt
     * @return answer of prompt at that index
     */
    public String getAnswer(int promptNumber) {
        return this.prompts.get(promptNumber).getAnswer();
    }

    /**
     * Return size of ArrayList
     * @return size of prompts ArrayList
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Add a prompt to the ArrayList
     * @param p Prompt to add
     */
    public void addPrompt(Prompt p) {
        prompts.add(p);
        size++;
        System.out.println("# of prompts is: " + prompts.size());
    }

    /**
     * remove a given prompt from prompts on given the prompt
     * @param p Prompt to delete
     */
    public void removePrompt(Prompt p) {
        prompts.remove(p);
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
     * Writes all prompts to txt file on closing of app
     * @param filePath path to file to write to
     */
    public void closeApp(String filePath) {
        try {
            FileOutputStream fout = new FileOutputStream(filePath);
            String qnA;
            byte[] array;
            for (int i = 0; i < size; i++) {
                fout.write(startSt.getBytes());
                fout.write('\n');

                qnA = getQuery(i);
                array = qnA.getBytes();
                fout.write(array);
                fout.write('\n');
                
                qnA = getAnswer(i);
                array = qnA.getBytes();
                fout.write(array);
                fout.write('\n');

                fout.write(endSt.getBytes());
                if (i != size-1) {
                    fout.write('\n');
                }
            }
            fout.close();
        } catch(IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Setter for Mediator
     * @param m Mediator object
     */
    public void setMediator(IMediator m) { mediator = m; }
}