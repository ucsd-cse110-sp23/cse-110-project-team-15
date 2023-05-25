package sayit;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class PromptHistory {
    // Objects needed for PromptHistory
    IMediator mediator;
    private int size = 0;
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    // other miscellaneous variables
    private final String startSt = "#Start#";
    private final String endSt = "#End#";
    private final String URL = "http://localhost:8100/";

    /**
     * New: Load the prompts from server using GET
     * Old: Read text file and pull relevant info, store it
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
                    qLine = qLine.trim();
                    aLine = aLine.trim();
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
     * Add a prompt to the ArrayList and do same on same on server using POST
     * @param p Prompt to add
     * @throws IOException
     */
    public void addPrompt(Prompt p) {
        /* add to prompts locally */
        prompts.add(p);
        size++;
        System.out.println("# of prompts is: " + prompts.size());

        /* add to prompts on server */
        try {
            String question = p.getQuery();
            String answer = p.getAnswer();
            
            // create URL to the server and create the connection
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // request the POST method on the server
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // write the question and answer to file that server can read and update its promtps array
            OutputStreamWriter out = new OutputStreamWriter(
                conn.getOutputStream()
            );
            out.write(question + "\n" + answer);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PromptHistory.java: " + e);
        }
    }

    /**
     * remove a given prompt from prompts on given the prompt and do same on same on server using DELETE
     * @param p Prompt to delete
     */
    public void removePrompt(Prompt p) {
        prompts.remove(p);
        size--;
        System.out.println("# of prompts is: " + prompts.size());
    }

    /**
     * Clear all prompts in the ArrayList, set size to 0, and do same on same on server using DELETE
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
                qnA = qnA.trim();
                array = qnA.getBytes();
                fout.write(array);
                fout.write('\n');

                qnA = getAnswer(i);
                qnA = qnA.trim();
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