/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// <editor-fold defaultstate="collapsed" desc="comment">
package documentcomparer;// </editor-fold>

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author amjad
 */
public class Comparer {

    public void compare(Document doc1, Document doc2)
    {
        // Calculating similarities
        
        doc1.setSimilarity(calculateSimilarity(doc1.getSentences(), doc2.getSentences()));
        doc2.setSimilarity(calculateSimilarity(doc2.getSentences(), doc1.getSentences()));
        
        if(doc1.getSimilarity() == 0.0 || doc2.getSimilarity() == 0.0)
        {
            doc1.setSimilarity(0.0);
            doc2.setSimilarity(0.0);
        }
        if(doc1.getSimilarity() - doc2.getSimilarity() != 0)
        {
            double value = (doc1.getSimilarity() + doc2.getSimilarity()) / 2;
            doc1.setSimilarity(value);
            doc2.setSimilarity(value);
        }
        

        //Calculate Differences
        doc1.setDifference(doc2.getSentences().size() - doc1.getSentences().size());
        doc2.setDifference(doc1.getSentences().size() - doc2.getSentences().size());

        // Calculating Topic
        doc1.setTopic(calculateTitle(doc1.getSentences()));
        doc2.setTopic(calculateTitle(doc2.getSentences()));

        //Calculate Attitude

        doc1.setAttitude(this.calculateAttitude(doc1.getSentences()));
        doc2.setAttitude(this.calculateAttitude(doc2.getSentences()));

    }

    private double calculateSimilarity(ArrayList doc1, ArrayList doc2)
    {
        int same = 0;
        for(int i=0; i<doc1.size(); i++)
        {
            for(int j=0; j<doc2.size(); j++)
            {
                if(this.compareSentence((String) doc1.get(i), (String) doc2.get(j)))
                {
                    same += 1;
                    break;
                }
            }
        }
        
       // JOptionPane.showMessageDialog(null, doc1.size());
        return (double) same/doc1.size() * 100;
    }

    private boolean compareSentence(String s1, String s2)
    {
        if(s1.equalsIgnoreCase(s2))
            return true;
        int same = 0;
        String s1tok [] = s1.split(" "), s2tok [] = s2.split(" ");
        for(int i=0; i<s1tok.length; i++)
        {
            if(s1tok[i].equalsIgnoreCase("is")           || s1tok[i].equalsIgnoreCase("am") || s1tok[i].equalsIgnoreCase("are")
                    || s1tok[i].equalsIgnoreCase("a")    || s1tok[i].equalsIgnoreCase("an")
                    || s1tok[i].equalsIgnoreCase("the")  || s1tok[i].equalsIgnoreCase("having")
                    || s1tok[i].equalsIgnoreCase("have") || s1tok[i].equalsIgnoreCase("has")
                    || s1tok[i].equalsIgnoreCase("was")  || s1tok[i].equalsIgnoreCase("were")
                    || s1tok[i].equalsIgnoreCase("it")   || s1tok[i].equalsIgnoreCase("its")
                    || s1tok[i].equalsIgnoreCase("of")   || s1tok[i].equalsIgnoreCase("on")
                    || s1tok[i].equalsIgnoreCase("in"))
                continue;
            
            for(int j=0; j<s2tok.length; j++)
            {
                if(s1tok[i].equalsIgnoreCase(s2tok[j]))
                    same += 1;
            }
        }

        int  n=0 ;
        for(int i=0; i<s2tok.length; i++)
        {
            if(s2tok[i].equalsIgnoreCase("is")           || s2tok[i].equalsIgnoreCase("am") || s2tok[i].equalsIgnoreCase("are")
                    || s2tok[i].equalsIgnoreCase("a")    || s2tok[i].equalsIgnoreCase("an")
                    || s2tok[i].equalsIgnoreCase("the")  || s2tok[i].equalsIgnoreCase("having")
                    || s2tok[i].equalsIgnoreCase("have") || s2tok[i].equalsIgnoreCase("has")
                    || s2tok[i].equalsIgnoreCase("was")  || s2tok[i].equalsIgnoreCase("were")
                    || s2tok[i].equalsIgnoreCase("it")   || s2tok[i].equalsIgnoreCase("its")
                    || s2tok[i].equalsIgnoreCase("of")   || s2tok[i].equalsIgnoreCase("on")
                    || s2tok[i].equalsIgnoreCase("in"))
                n++;
        }

        
        //JOptionPane.showMessageDialog(null,"value one is " + (s2tok.length - same) + " value two is " + n);
        if ((s2tok.length - same >= 0) && ((s2tok.length - same) <= (n)))
            return true;
        return false;
    }

    private String calculateTitle(ArrayList doc)
    {
        WordList wordList = new WordList();

        // For document 1
        for(int i=0; i<doc.size(); i++)
        {
            String temp [] = ((String) doc.get(i)).split(" ");
            for(int j=0; j<temp.length; j++)
            {
                if(temp[j].equalsIgnoreCase("is")            || temp[j].equalsIgnoreCase("am") || temp[j].equalsIgnoreCase("are")
                    || temp[j].equalsIgnoreCase("a")         || temp[j].equalsIgnoreCase("an")
                    || temp[j].equalsIgnoreCase("the")       || temp[j].equalsIgnoreCase("having")
                    || temp[j].equalsIgnoreCase("have")      || temp[j].equalsIgnoreCase("has")
                    || temp[j].equalsIgnoreCase("was")       || temp[j].equalsIgnoreCase("were")
                    || temp[j].equalsIgnoreCase("it")        || temp[j].equalsIgnoreCase("its")
                    || temp[j].equalsIgnoreCase("of")        || temp[j].equalsIgnoreCase("on")
                    || temp[j].equalsIgnoreCase("I")         || temp[j].equalsIgnoreCase("we")
                    || temp[j].equalsIgnoreCase("off")       || temp[j].equalsIgnoreCase("he")
                    || temp[j].equalsIgnoreCase("they")      || temp[j].equalsIgnoreCase("you")
                    || temp[j].equalsIgnoreCase("your")      || temp[j].equalsIgnoreCase("yours")
                    || temp[j].equalsIgnoreCase("she")       || temp[j].equalsIgnoreCase("shall")
                    || temp[j].equalsIgnoreCase("will")      || temp[j].equalsIgnoreCase("those")
                    || temp[j].equalsIgnoreCase("them")      || temp[j].equalsIgnoreCase("there")
                    || temp[j].equalsIgnoreCase("where")     || temp[j].equalsIgnoreCase("here")
                    || temp[j].equalsIgnoreCase("good")      || temp[j].equalsIgnoreCase("bad")
                    || temp[j].equalsIgnoreCase("excellent") || temp[j].equalsIgnoreCase("worse")
                    || temp[j].equalsIgnoreCase("worst")     || temp[j].equalsIgnoreCase("then")
                    || temp[j].equalsIgnoreCase("in")        || temp[j].equalsIgnoreCase("so")
                    || temp[j].equalsIgnoreCase("my")        || temp[j].equalsIgnoreCase("our")
                    || temp[j].equalsIgnoreCase("own")       || temp[j].equalsIgnoreCase("mine")
                    || (temp[j].equalsIgnoreCase("1")        || temp[j].equalsIgnoreCase("2"))
                    || temp[j].equalsIgnoreCase("3")       || temp[j].equalsIgnoreCase("4")
                    || temp[j].equalsIgnoreCase("5")       || temp[j].equalsIgnoreCase("6")
                    || temp[j].equalsIgnoreCase("8")       || temp[j].equalsIgnoreCase("7")
                    || temp[j].equalsIgnoreCase("9")       || temp[j].equalsIgnoreCase("0")
                    || (temp[j].equalsIgnoreCase("-1")        || temp[j].equalsIgnoreCase("-2"))
                    || temp[j].equalsIgnoreCase("-3")       || temp[j].equalsIgnoreCase("-4")
                    || temp[j].equalsIgnoreCase("-5")       || temp[j].equalsIgnoreCase("-6")
                    || temp[j].equalsIgnoreCase("-8")       || temp[j].equalsIgnoreCase("-7")
                    || temp[j].equalsIgnoreCase("-9")       || temp[j].equalsIgnoreCase("0")
                    || temp[j].equalsIgnoreCase(" ")        || temp[j].equalsIgnoreCase("and")
                    || temp[j].equalsIgnoreCase("or")       || temp[j].equalsIgnoreCase("not")
                    || temp[j].equalsIgnoreCase("none")     || temp[j].equalsIgnoreCase("very")
                    || temp[j].equalsIgnoreCase("much"))
                {
                    
                    continue;
                }
                else
                {
                    if (temp[j].equalsIgnoreCase(" "))
                        break;
                    else
                    {
                        Word obj = new Word (temp[j]);
                        //System.out.println(temp[j]);
                        wordList.add(obj);
                    }
                }
            }
        }

       // Assigning frequency to each word

       for(int i=0; i<wordList.getWordList().size(); i++)
       {
           int frequency = 0;
           for(int j=0; j<wordList.getWordList().size(); j++)
           {
               if (wordList.getWord(i).getWord().equals(wordList.getWord(j).getWord()))
               {
                   if (wordList.getWord(i).getWord().equals(" "))
                       break;
                   else
                   {
                        //System.out.println("i = " + wordList.getWord(i).getWord() + "\t j = " +wordList.getWord(j).getWord());
                        frequency++;
                   }
               }
           }

           wordList.getWord(i).setFrequency(frequency);
       }

       // Sorting Word List in desending order
      // wordList.sortList();
      String title = wordList.getMaxFrequencyWord() + ",  " + wordList.getMaxFrequencyWord() + ",  " + wordList.getMaxFrequencyWord();
       /*for(int i=0; i<wordList.getWordList().size(); i++)
       {
           System.out.println("Word = " + wordList.getWord(i).getWord() + "\t Frequency is " + wordList.getWord(i).getFrequency());
       }*/

      
       return title;
    }

    private String calculateAttitude(ArrayList doc)
    {
        TagText obj = new TagText();
        int positive = 0 , negative = 0, neutral = 0;
        for(int i=0; i<doc.size(); i++)
        {
            
            try
            {
                int num = obj.Main((String) doc.get(i));
                if (num == 0)
                {
                    neutral++;
                }
                else if (num < 0)
                    negative ++;
                else
                    positive ++;
            }
            catch (IOException ex)
            {
                Logger.getLogger(Comparer.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(Comparer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Positive = " + positive);
            System.out.println("Negative = " + negative);
            System.out.println("Neutral = " + neutral);
        }
        System.out.println("Positive = " + positive);
        System.out.println("Negative = " + negative);
        System.out.println("Neutral = " + neutral);
        if(positive == negative && positive > neutral)
            return "NEUTRAL";
        if((positive >= negative) && (positive >= neutral))
                return "POSITIVE";
        if((positive <= negative) && (negative >= neutral))
                return "NEGATIVE";
        if(((neutral >= negative) && (positive <= neutral)))
                return "NEUTRAL";
        
        return "Unable to Conclude!";
    }


}
