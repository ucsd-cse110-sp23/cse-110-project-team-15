import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GetPromptHistory {

    private int size = 0;
    private final String filepath = "C:/Users/ethan/Downloads/CSE 110/cse-110-project-team-15/Test-files/test-1.txt";
    private ArrayList<QandA> prompts = new ArrayList<QandA>();

    // Method read text file and pull relevant info, store it
    public GetPromptHistory() {
        try {
            FileReader fileReader = new FileReader(filepath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String line2;
            
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

    public QandA getPrompt(int promptNumber) {
        return this.prompts.get(promptNumber);
    }

    public String getQuery(int promptNumber) {
        return this.prompts.get(promptNumber).getQuery();
    }

    public String getAnswer(int promptNumber) {
      return this.prompts.get(promptNumber).getAnswer();
    }

    public int getSize() {
      return this.size;
    }

    public static void main(String args[]) {
        GetPromptHistory a = new GetPromptHistory();
        System.out.println(a.getQuery(0));
        System.out.print(a.getAnswer(0));
    }
}
