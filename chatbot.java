import java.io.*;
import java.util.*;
import java.util.Scanner;
//import org.json.simple.JSONArray;
//import org.json.simple.parser.*;

// Majority of code has been taken from the AP CSA Runestone Website (Laurie White)
public class chatbot
{

   /**
    * Get a default greeting
    * @return a greeting
    */

   private static Hashtable<String, String> responseDict = new Hashtable<String,String>();
   private static Hashtable<String, String> keywordDict = new Hashtable<String,String>();
    public chatbot() {
        responseDict.put("greeting", "Hello there!~Hey there!~Deez Nutz!");
        responseDict.put("random","Hmmmm~Interesting, tell me more.");
        responseDict.put("clarify","What does that mean?~Speak more clearly, noob.");

        keywordDict.put("greeting","Hello~Hi~What's Up~Hey");
    }
    public static String getMessage(String intent){
        String responses = responseDict.get(intent);
        String[] responseList = responses.split("~");
        return responseList[(int) (Math.random() * responseList.length)];
    }
    public static String getIntent(String sentence){
        String cleanSentence = cleanStatement(sentence);
        Set<String> keys = keywordDict.keySet();
        for (String key : keys){
            String keywords = keywordDict.get(key);
            String[] keywordList = keywords.split("~");
            for (String keyword : keywordList){
                if (cleanSentence.contains(keyword)) return key;
            }
        }
        return "random";
    }

   // goes through statement and cleans out punctuation
   public static String cleanStatement(String sentence)
   {
    sentence = sentence.trim();
    String cleanSentence = "";

     for(int index = 0; index < sentence.length(); index++)
     {
       if (Character.isAlphabetic(sentence.charAt(index)) 
          || sentence.charAt(index) == ' ') {
         cleanSentence += sentence.substring(index, index + 1);
       }
     }
     return cleanSentence;
   }
   /**
    * Take a statement with "I want to <something>." and transform it into
    * "What would it mean to <something>?"
    * @param statement the user statement, assumed to contain "I want to"
    * @return the transformed statement
    */
   private String transformIWantToStatement(String statement)
   {
      statement = cleanStatement(statement);
      int pos = findKeyword (statement, "I want to", 0);
      String restOfStatement = statement.substring(pos + 9).trim();
      return "What would it mean to " + restOfStatement + "?";
   }

   /**  ADD CODE HERE!
    * Take a statement with "I want <something>." and transform it into
    * Would you really be happy if you had <something>?
    * @param statement the user statement, assumed to contain "I want"
    * @return the transformed statement
    */
   private String transformIWantStatement(String statement)
   {
      // ADD CODE HERE

      return "Would you really be happy if you had ...";
   }

   /**
    * Take a statement with "you <something> me" and transform it into
    * "What makes you think that I <something> you?"
    * @param statement the user statement, assumed to contain "you" followed by "me"
    * @return the transformed statement
    */
   private String transformYouMeStatement(String statement)
   {
      statement = cleanStatement(statement);
      int posOfYou = findKeyword (statement, "you", 0);
      int posOfMe = findKeyword (statement, "me", posOfYou + 3);

      String restOfStatement = statement.substring(posOfYou + 3, posOfMe).trim();
      return "What makes you think that I " + restOfStatement + " you?";
   }

  /**  ADD THIS
    * Take a statement with "I <something> you" and transform it into
    * "Why do you <something> me?"
    * @param statement the user statement, assumed to contain "I" followed by something "you"
    * @return the transformed statement
    */
   private String transformIMeStatement(String statement)
   {
     // ADD CODE HERE
     return "Why do you...";
   }

   /**
    * Pick a default response to use if nothing else fits.
    * @return a non-committal string
    */
   
       public static void main(String[] args)
       {  
            Scanner in = new Scanner(System.in);
            //boolean conversing = true;
            chatbot chatbot = new chatbot();
            String statement = "";
        
            // Conversation begins
            System.out.println(chatbot.getMessage("greeting"));
            System.out.println(chatbot.getMessage("clarify"));
            System.out.println(chatbot.getMessage("random"));
            statement = in.nextLine();
            while (!(statement.equals("|quit"))){
                // System.out.println(statement);
                System.out.println("Response: " + chatbot.getMessage(getIntent(statement)));
                statement = in.nextLine();
                
            }
            in.close();

       }

}
