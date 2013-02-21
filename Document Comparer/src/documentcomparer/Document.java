/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package documentcomparer;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author amjad
 */
public class Document {

    private String attitude, topic;
    private double similarity;
    private int difference;
    private ArrayList sentences;


    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        if(difference < 0)
            this.difference = -1 * difference;
        else
            this.difference =  difference;
    }
    

    Document(ArrayList sentences)
    {
        this.sentences = new ArrayList();
        this.sentences = sentences;
        
    }

    public ArrayList getSentences() {
        return sentences;
    }

    public void setSentences(ArrayList sentences) {
        this.sentences = sentences;
    }
    
    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void print()
    {
        JOptionPane.showMessageDialog(null, "Similarities b/w these documents are " + this.similarity + "% and "
                + "difference of their number of sentences is " + difference +"\n"
                + "Overall attitude of document is " + this.attitude +
                "\nSuggested Topics are " + this.topic
                );
    }

}
