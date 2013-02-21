
package documentcomparer;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;


public class WeightMarker 
{
	public void markverbs(BufferedReader verbInput, ArrayList<Classifier> classifier) throws NumberFormatException, IOException
	{
            String temp ="";
            while((temp=verbInput.readLine())!=null)
            {
        	//System.out.println(temp);
        	String[] toks = temp.split(" ");
        	String word1 = toks[0];
        	String word2 = toks[1];
        	String weight = toks[2];
        	//System.out.println("Word1:"+word1+"Word2:"+word2+"Weight:"+weight);
        	for(int i=0; i<classifier.size(); i++)
        	{
        		if(classifier.get(i).word!=null && (classifier.get(i).word.toUpperCase().equals(word1.toUpperCase())
        										|| classifier.get(i).word.toUpperCase().equals(word2.toUpperCase())))
        			classifier.get(i).weight = Integer.parseInt(weight);
                //Classifier get = classifier.get(i);
                //System.out.println("**********************WeightMarker*********************");
                //get.print();
                
                }

            }

            verbInput.mark(0);
            verbInput.reset();
            verbInput.reset();
	}
	
	public void markAdjectives(BufferedReader adjectiveInput, ArrayList<Classifier> classifier) throws NumberFormatException, IOException
	{
            String temp = "";
            while((temp=adjectiveInput.readLine())!=null)
            {
        	//System.out.println(temp);
        	String[] toks = temp.split(" ");
        	String word = toks[0];
        	String weight = toks[1];
        	for(int i=0; i<classifier.size(); i++)
        	{
        		if(classifier.get(i).word!=null && classifier.get(i).word.toUpperCase().equals(word.toUpperCase()))
        			classifier.get(i).weight = Integer.parseInt(weight);
        	}
            }
            adjectiveInput.mark(0);
            adjectiveInput.reset();
            adjectiveInput.reset();
	}
	
	public void markAdverbs(BufferedReader adverbInput, ArrayList<Classifier> classifier) throws NumberFormatException, IOException
	{
            String temp = "";
            while((temp=adverbInput.readLine())!=null)
            {
        	//System.out.println(temp);
        	String[] toks = temp.split(" ");
        	String word = toks[0];
        	String weight = toks[1];
        	for(int i=0; i<classifier.size(); i++)
        	{
        		if(classifier.get(i).word!=null && classifier.get(i).word.toUpperCase().equals(word.toUpperCase()))
        			classifier.get(i).weight = Integer.parseInt(weight);
        	}
            }
            adverbInput.mark(0);
            adverbInput.reset();
            adverbInput.reset();
	}
}
