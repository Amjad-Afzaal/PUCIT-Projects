/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package documentcomparer;

import java.util.ArrayList;

/**
 *
 * @author amjad
 */
public class WordList {

    ArrayList wordList;

    public WordList() {

        this.wordList = new ArrayList ();
    }

    


    public ArrayList getWordList() {
        return wordList;
    }

    public void setWordList(ArrayList wordList) {
        this.wordList = wordList;
    }

    public void add(Word obj)
    {
        this.wordList.add(obj);
    }

    public Word getWord(int index)
    {
        return (Word) this.wordList.get(index);
    }

    public String getMaxFrequencyWord()
    {
        int index = 0;
        Word max = ( (Word)this.wordList.get(0));
        
        for(int i=1; i<this.wordList.size(); i++)
        {
            if (((Word)this.wordList.get(i)).getWord().equals(" "))
            {
                this.wordList.remove(i);
                System.out.println("Yes");
            }
            if (max.getFrequency() < ((Word)this.wordList.get(i)).getFrequency())
            {
                max = ((Word) this.wordList.get(i));
                index = i;
            }
        }


        this.wordList.remove(index);
        return max.getWord();

    }

}
