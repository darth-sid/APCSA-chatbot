// Majority of code has been taken from the AP CSA Runestone Website (Laurie White)
public class chatbot
{

   /**
    * Get a default greeting
    * @return a greeting
    */
   public String getGreeting()
   {
      return "Hello, let's talk.";
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
    * Gives a response to a user statement
    *
    * @param statement
    *            the user statement
    * @return a response based on the rules given
    */
   public String getResponse(String statement)
   {
      String response = "";
      if (statement.length() == 0)
      {
         response = "Say something, please.";
      }

      else if (findKeyword(statement, "no") >= 0)
      {
         response = "Why so negative?";
      }
      else if (findKeyword(statement, "mother") >= 0
               || findKeyword(statement, "father") >= 0
               || findKeyword(statement, "sister") >= 0
               || findKeyword(statement, "brother") >= 0)
      {
         response = "Tell me more about your family.";
      }

      // Responses which require transformations
      else if (findKeyword(statement, "I want to", 0) >= 0)
      {
         response = transformIWantToStatement(statement);
      }

      // ADD Responses which require transformations!


      else
      {
         // Look for a two word (you <something> me)
         // pattern
         int pos = findKeyword(statement, "you", 0);

         if (pos >= 0
             && findKeyword(statement, "me", pos) >= 0)
         {
            response = transformYouMeStatement(statement);
         }
         else
         {
            response = getRandomResponse();
         }
      }
      return response;
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
    * Search for one word in phrase.  The search is not case sensitive.
    * This method will check that the given goal is not a substring of a longer string
    * (so, for example, "I know" does not contain "no").
    * @param statement the string to search
    * @param goal the string to search for
    * @param startPos the character of the string to begin the search at
    * @return the index of the first occurrence of goal in statement or -1 if it's not found
    */
   private int findKeyword(String statement, String goal, int startPos)
   {
      String phrase = statement.trim();
      //  The only change to incorporate the startPos is in the line below
      int pos = phrase.toLowerCase().indexOf(goal.toLowerCase(), startPos);

      //  Refinement--make sure the goal isn't part of a word
      while (pos >= 0)
      {
         //  Find the string of length 1 before and after the word
         String before = " ", after = " ";
         if (pos > 0)
         {
            before = phrase.substring(pos - 1, pos).toLowerCase();
         }
         if (pos + goal.length() < phrase.length())
         {
            after = phrase.substring(pos + goal.length(), pos + goal.length() + 1).toLowerCase();
         }

         //  If before and after aren't letters, we have found the word
         if (((before.compareTo ("a") < 0 ) || (before.compareTo("z") > 0))  //  before is not a letter
         && ((after.compareTo ("a") < 0 ) || (after.compareTo("z") > 0)))
         {
            return pos;
         }

         //  The last position didn't work, so let's find the next, if there is one.
         pos = phrase.indexOf(goal.toLowerCase(), pos + 1);

      }

      return -1;
   }

   /**
    * Search for one word in phrase.  The search is not case sensitive.
    * This method will check that the given goal is not a substring of a longer string
    * (so, for example, "I know" does not contain "no").  The search begins at the beginning of the string.
    * @param statement the string to search
    * @param goal the string to search for
    * @return the index of the first occurrence of goal in statement or -1 if it's not found
    */
   private int findKeyword(String statement, String goal)
   {
      return findKeyword (statement, goal, 0);
   }

   /**
    * Pick a default response to use if nothing else fits.
    * @return a non-committal string
    */
   private String getRandomResponse()
   {
      final int NUMBER_OF_RESPONSES = 4;
      double r = Math.random();
      int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
      String response = "";

      if (whichResponse == 0)
      {
         response = "Interesting, tell me more.";
      }
      else if (whichResponse == 1)
      {
         response = "Hmmm.";
      }
      else if (whichResponse == 2)
      {
         response = "Do you really think so?";
      }
      else if (whichResponse == 3)
      {
         response = "You don't say.";
      }

      return response;
   }

       public static void main(String[] args)
       {
             chatbot chatbot = new chatbot();
             String statement = "I want to find Nemo.";
             System.out.println("Statement: " + statement);
             System.out.println("Response: " + chatbot.getResponse(statement));
       }

}
