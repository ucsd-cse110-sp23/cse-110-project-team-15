package sayit.Server.BusinessLogic;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class PromptHistory {
    // Objects needed for PromptHistory
    IMediator mediator;
    private int size = 0;
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();

    // other miscellaneous variables
    private final String URL = "http://localhost:8100/";
    String startSt = "#Start#";
    String endSt = "#End#";

    /**
     * New: Load the prompts from server using GET
     * Old: Read text file and pull relevant info, store it
     * @param String of relative path to txt file
     */
    public void setupPromptHistory(String filepath) {
        try {
            int i = 0;
            String response;
            while (true) {
                // create URL (with query) to the server and create the connection
                String query = String.valueOf(i);
                URL url = new URL(URL + "?=" + query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // request the GET method on the server
                conn.setRequestMethod("GET");

                // print the response for testing purposes
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                );
                response = in.readLine();
                System.out.println("GET response: " + response);

                // check if the reponse is -1 (reached end of prompts on server)
                if (response.equals("-1")) { break; }

                // parse the response and store the question and answer in the prompts locally
                String question = response.substring(0,response.indexOf("/D\\"));
                String answer = response.substring(response.indexOf("/D\\") + 3);
                prompts.add(new Prompt(question,answer));

                in.close();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PromptHistory.java: " + e);
        }

        // read from filePath for testing purposes
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

            // print the response for testing purposes
            BufferedReader in = new BufferedReader(
              new InputStreamReader(conn.getInputStream())
            );
            String response = in.readLine();
            System.out.println("POST response: " + response);
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
        // save index for calling DELETE on server
        int index = prompts.indexOf(p);

        /* delete prompts locally */
        prompts.remove(p);
        size--;

        /* delete prompts on server */
        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(URL + "?=" + index);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // request the DELETE method on the server
            conn.setRequestMethod("DELETE");

            // print the response for testing purposes
            BufferedReader in = new BufferedReader(
              new InputStreamReader(conn.getInputStream())
            );
            String response = in.readLine();
            System.out.println("DELETE response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PromptHistory.java: " + e);
        }
    }

    /**
     * Clear all prompts in the ArrayList, set size to 0, and do same on same on server using DELETE
     */
    public void clearPrompts() {
        /* clear prompts locally */
        prompts.clear();
        size = 0;

        /* clear prompts on server */
        try {
            // create URL (with query) to the server and create the connection
            URL url = new URL(URL + "?=" + -1);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // request the DELETE method on the server
            conn.setRequestMethod("DELETE");

            // print the response for testing purposes
            BufferedReader in = new BufferedReader(
              new InputStreamReader(conn.getInputStream())
            );
            String response = in.readLine();
            System.out.println("DELETE response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PromptHistory.java: " + e);
        }
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