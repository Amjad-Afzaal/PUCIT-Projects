/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package documentcomparer;

/**
 *
 * @author amjad
 */
public class Word {

    private String word;
    private int frequency;

    public Word(String word) {
        this.word = word;
        this.frequency = 0;
    }



    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    

}
