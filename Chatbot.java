package events.chatbotcsa;

import java.util.*;
import java.util.Scanner;
//import org.json.simple.JSONArray;
//import org.json.simple.parser.*;

// Majority of code has been taken from the AP CSA Runestone Website (Laurie White)
public class Chatbot
{

   /**
    * Get a default greeting
    * @return a greeting
    */

   private static Hashtable<String, String> responseDict = new Hashtable<String,String>();
   private static Hashtable<String, String> keywordDict = new Hashtable<String,String>();
   private static String[] topicsList = {"topic1", "topic2", "topic3"};
   private static boolean topicMode = false;
   private static int topicIndex = 0; // Index of the topicsList
   private static int factIndex = 0; // Index of the facts

   private static int triviaIndex = 0;
   private static boolean triviaMode = false;
   private static String[] triviaList = {"trivia1", "trivia2", "trivia3"};

    public Chatbot() {
        responseDict.put("greeting", "Hello there!~Hey there!~Deez Nutz!");
        responseDict.put("random","Hmmmm~Interesting, tell me more.");
        responseDict.put("clarify","What does that mean?~Speak more clearly, noob.");
        responseDict.put("transform[want]","Why do you want ~I also want ");
        responseDict.put("transform[need]","You don't need ~Why do you need ~I do not need ");
        responseDict.put("reason","yes");

        // Random topics list
        responseDict.put("topic1","Want to talk about carbon hybridization?~[carbon hybridization fact 1]. Want to hear another fact?~[carbon hybridization fact 2]");
        responseDict.put("topic2","Want to talk about orbitals?~[orbitals fact 1]. Want to hear another fact?~[orbitals fact 2]");
        responseDict.put("topic2","Want to talk about water?~[water fact 1]. Want to hear another fact?~[water fact 2]");
        responseDict.put("rejection","Ok!~Okay!~Aww it's a fun topic to talk about...~Alright!~Ok then!");

        // Trivia questions list
        responseDict.put("trivia1", "a|What is the name of the first orbital?\na) s\nb) p \nc) f");
        responseDict.put("trivia2", "b|What is the name of the second orbital?\na) s\nb) p \nc) f");
        responseDict.put("trivia3", "b|What is the name of the fourth orbital?\na) s\nb) p \nc) f");

        keywordDict.put("greeting","Hello~Hi~What's Up~Hey");
        keywordDict.put("transform[want]","I want~I wish");
        keywordDict.put("transform[need]","I need~I require");
        keywordDict.put("reason","why");
        keywordDict.put("tellmore","yes~ye~yea~yeah~ya~ye~yuh~yup~yep~sure~cool~alright~fine~okay~ok~bruh");
        //keywordDict.put("rejection","no~nah~nu~nope~im good~its fine~im fine~ill pass~stop");
        keywordDict.put("triviaanswer","a~b~c");

    }
    public static String getMessage(String raw){
        String[] data = raw.split("~");
        String intent = data[0];
        String responses = "";
        String[] responseList = {};
        if (topicMode == false && triviaMode == false) {
          responses = responseDict.get(intent);
          responseList = responses.split("~");
        }
        // Response to a trivia question answer
        if (triviaMode == true) {
          responses = responseDict.get(triviaList[triviaIndex]);
          if (intent.contains("triviaanswer")) {
            if (data[1].equals(responses.substring(0, 1))) {
              triviaMode = false;
              return "Your answer is correct!";
            }
          }
          triviaMode = false;
          return "Incorrect! The answer was " + responses.substring(0, 1) + "!";
        }
        // Stops going through random topic
        if (!(intent.contains("tellmore")) && topicMode == true) {
          responses = responseDict.get("rejection");
          responseList = responses.split("~");
          topicMode = false;
          factIndex = 0;
          return responseList[(int) (Math.random() * responseList.length)];
        }
        // Continues random topic
        if (intent.contains("tellmore") && topicMode == true) {
          responses = responseDict.get(topicsList[topicIndex]);
          responseList = responses.split("~");
          int tempIndex = factIndex;
          if (factIndex + 1 == responseList.length) {
            topicMode = false;
            factIndex = -1; // Resets index so it is ready for next random topic
          }
          factIndex++;
          return responseList[tempIndex];
        }
        if (intent.contains("transform")){
            String keyword = data[1];
            String responseTemp = responseList[(int) (Math.random() * responseList.length)]; 
            if (responseTemp.toLowerCase().contains("you")) keyword = findAndReplace(keyword, "you", "me", 10);
            return responseTemp+keyword;
        }

        int specialRandomizer = (int) (Math.random() * 5); // Randomizer may go for random topic or trivia
        // RANDOM CONVERSATION
        if (intent.contains("random") && specialRandomizer == 1) {
          topicMode = true;
          topicIndex = (int) (Math.random() * (topicsList.length - 1));
          responses = responseDict.get(topicsList[topicIndex]);
          responseList = responses.split("~");
          factIndex++;
          return responseList[factIndex - 1];
        }
        // TRIVIA QUESTION
        if (intent.contains("random") && specialRandomizer == 2) {
          triviaMode = true;
          triviaIndex = (int) (Math.random() * (triviaList.length - 1));
          responses = responseDict.get(triviaList[triviaIndex]);
          return "Random trivia question!\n\n" + responses.substring(2);
        }

        // Default response to intent
        return responseList[(int) (Math.random() * responseList.length)];
    }
    public static String getIntent(String sentence){
        String cleanSentence = cleanStatement(sentence);
        Set<String> keys = keywordDict.keySet();
        for (String key : keys){
            String keywords = keywordDict.get(key);
            String[] keywordList = keywords.split("~");
            for (String keyword : keywordList){
                if (cleanSentence.contains(keyword.toLowerCase())){
                    if(key.contains("transform")){
                        return key+"~"+keyword(sentence,keyword);
                    }
                    if(key.contains("tellmore") && topicMode == false) {continue;}
                    if(key.contains("rejection") && topicMode == false) {continue;}
                    if(key.contains("triviaanswer") && triviaMode == false) {continue;}
                    if(key.contains("triviaanswer") && triviaMode == true) {
                      return key + "~" + sentence;
                    }
                    return key;
                }
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
         cleanSentence += sentence.substring(index, index + 1).toLowerCase();
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
   /*private String transformIWantToStatement(String statement)
   {
      statement = cleanStatement(statement);
      int pos = findKeyword (statement, "I want to", 0);
      String restOfStatement = statement.substring(pos + 9).trim();
      return "What would it mean to " + restOfStatement + "?";
   }*/

   /**  ADD CODE HERE!
    * Take a statement with "I want <something>." and transform it into
    * Would you really be happy if you had <something>?
    * @param statement the user statement, assumed to contain "I want"
    * @return the transformed statement
    *//*
   private String transformIWantStatement(String statement)
   {
      // ADD CODE HERE

      return "Would you really be happy if you had ...";
   }*/
    
   private static String keyword(String statement, String fragment){
       if (statement.contains(fragment)){
            String word = "";
            int start = statement.indexOf(fragment) + fragment.length()+1;
            int end = statement.substring(start,statement.length()).indexOf(".");
            if (end == -1) end = statement.substring(start,statement.length()).length();
            word = statement.substring(start, start+end);
            return word;
        }
        else return "urmom";
   }

   private static String findAndReplace(String sentence, String find, String replace, int repeat){
        String statement = sentence + " ";
        for (int i = 0; i < repeat; i++){
            String temp = "";
            if (statement.contains(find)){
                temp += statement.substring(0,statement.indexOf(find));
                temp += replace;
                temp += statement.substring(statement.indexOf(find)+statement.substring(statement.indexOf(find),statement.length()).indexOf(" "),statement.length());
                statement = temp;
            }
        }
       return statement;
   }
      /**
    * Take a statement with "you <something> me" and transform it into
    * "What makes you think that I <something> you?"
    * @param statement the user statement, assumed to contain "you" followed by "me"
    * @return the transformed statement
    *//*
   private String transformYouMeStatement(String statement)
   {
      statement = cleanStatement(statement);
      int posOfYou = findKeyword (statement, "you", 0);
      int posOfMe = findKeyword (statement, "me", posOfYou + 3);

      String restOfStatement = statement.substring(posOfYou + 3, posOfMe).trim();
      return "What makes you think that I " + restOfStatement + " you?";
   }*/

  /**  ADD THIS
    * Take a statement with "I <something> you" and transform it into
    * "Why do you <something> me?"
    * @param statement the user statement, assumed to contain "I" followed by something "you"
    * @return the transformed statement
    *//*
   private String transformIMeStatement(String statement)
   {
     // ADD CODE HERE
     return "Why do you...";
   }*/

   /**
    * Pick a default response to use if nothing else fits.
    * @return a non-committal string
    */
   
       /*public static void main(String[] args)
       {  
            Scanner in = new Scanner(System.in);
            chatbot chatbot = new chatbot();
            String statement = "";
        
            // Conversation begins
            System.out.println(chatbot.getMessage("greeting"));
            System.out.println(chatbot.getMessage("clarify"));
            System.out.println(chatbot.getMessage("random"));
            statement = in.nextLine();
            while (!(statement.equals("|quit"))){
                System.out.println("Response: " + chatbot.getMessage(chatbot.getIntent(statement)));
                statement = in.nextLine();
                
            }
            in.close();

       }*/

}
