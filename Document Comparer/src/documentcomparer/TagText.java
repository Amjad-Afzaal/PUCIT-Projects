
package documentcomparer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*; 


import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import javax.swing.JOptionPane;
 
public class TagText 
{
    //private BufferedReader verbInput;
    //private BufferedReader adjInput;
    //private MaxentTagger tagger;
    private POSTagger tagger;
    private BufferedReader verbInput;
    private BufferedReader adjectiveInput;
    private BufferedReader adverbInput;
    private WordTagSplitter wordTagSplitter;
    TagText()
    {
        try
        {
            tagger = new POSTagger();
            verbInput = new BufferedReader(new FileReader("myVerb.txt"), 350000);
            adjectiveInput = new BufferedReader(new FileReader("myAdjective.txt"), 350000);
            adverbInput = new BufferedReader(new FileReader("myAdverb.txt"),5000);
            wordTagSplitter = new WordTagSplitter();
        } 
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(TagText.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public int Main(String args) throws IOException,ClassNotFoundException
    {
        // Initialize the tagger
        
 
        // The sample string
        //String sample = "It was the worst win";
        /*String sample = args;
        // The tagged string
        String tagged = tagger.tagString(sample);
 
        // Output the result
        System.out.println(tagged);
        
        
        
        
        
        String tokens[];
        tokens = tagged.split(" ");
        ArrayList<Classifier> classifier = new ArrayList<Classifier>();
        
        Pattern pattern = Pattern.compile("/*VBP/*");
        Pattern pattern1 = Pattern.compile("/*JJ/*");
        
        
        
        for(int i=0; i<tokens.length; i++)
        {
        	Classifier obj = new Classifier();
        	obj.setTag(tokens[i]);
        	Matcher verbMatch = pattern.matcher(tokens[i]);
            Matcher adjMatch = pattern1.matcher(tokens[i]);
        	if(verbMatch.find())
        	{
        		obj.setWord((tokens[i].split("\\/"))[0]) ;
        	}
        	else if(adjMatch.find())
        	{
        		obj.setWord((tokens[i].split("\\/"))[0]);
        	}
        	classifier.add(obj);
        	
        }
        
        String temp ="";
        
        while((temp=verbInput.readLine())!=null)
        {
        	String[] toks = temp.split(" ");
        	String word = toks[0];
        	String weight = toks[1];
        	for(int i=0; i<classifier.size(); i++)
        	{
        		if(classifier.get(i).getWord()!= null && classifier.get(i).getWord().toUpperCase().equals(word.toUpperCase()))
        			classifier.get(i).setWeight(Integer.parseInt(weight));
        	}
        }

        this.verbInput.mark(0);
        this.verbInput.reset();
    
        while((temp=adjInput.readLine())!=null)
        {
        	String[] toks = temp.split(" ");
               // System.out.println(toks[0] +  "\t" + toks[1]);
        	String word = toks[0];
        	String weight = toks[1];
        	for(int i=0; i<classifier.size(); i++)
        	{
        		if(classifier.get(i).getWord()!=null && classifier.get(i).getWord().toUpperCase().equals(word.toUpperCase()))
        			classifier.get(i).setWeight(Integer.parseInt(weight));
        	}
        }

        this.adjInput.mark(0);
        this.adjInput.reset();

        for(int i=0; i<classifier.size(); i++)
        {
        	
        	System.out.println(classifier.get(i).getTag()+"	"+classifier.get(i).getWord() + "	" + classifier.get(i).getWeight());
        }
        
      
        int sentiment = 0;
        for(int i=0; i<classifier.size(); i++)
        {
        	if(sentiment==0)
        	{
        		sentiment += classifier.get(i).getWeight();
        	}
        	else if(sentiment<0)
        	{
        		if(classifier.get(i).getWeight()>0)
        		{
        			sentiment *= classifier.get(i).getWeight();
        		}
        		else if(classifier.get(i).getWeight()<0)
        		{
        			sentiment += classifier.get(i).getWeight();
        		}
        	}
        	else
        	{
        		if(classifier.get(i).getWeight()>0)
        		{
        			sentiment += classifier.get(i).getWeight();
        		}
        		else if(classifier.get(i).getWeight()<0)
        		{
        			sentiment *= classifier.get(i).getWeight();
        		}
        	}
        }
        
        
        
        if(sentiment>0)
        {
        	//JOptionPane.showMessageDialog(null, "POSITIVE");
                return 1;
        }
        else if(sentiment<0)
        {
            //JOptionPane.showMessageDialog(null, "NEGATIVE");
            return -1;
        }
        else
        {
            //JOptionPane.showMessageDialog(null, "NEUTRAL");
            return 0;
        }*/
        

        String tagged = tagger.tagSentence(args);
        System.out.println(tagged);

    	String tokens[];
    	tokens = tagged.split(" ");
    	ArrayList<Classifier> classifier = new ArrayList<Classifier>();
    	//wordTagSplitter = new WordTagSplitter();
    	wordTagSplitter.splitWordTag(tokens, classifier);
        WeightMarker weightMarker = new WeightMarker();
    	weightMarker.markverbs(verbInput, classifier);
    	weightMarker.markAdjectives(adjectiveInput, classifier);
	weightMarker.markAdverbs(adverbInput, classifier);
        WeightAnalyzer weightAnalyzer = new WeightAnalyzer();
        int num = weightAnalyzer.analyzeWeights(classifier);
        //System.out.println("Befor exit " + verbInput.readLine());
        System.out.println("Num = " + num);
        verbInput = new BufferedReader(new FileReader("myVerb.txt"), 350000);
        adjectiveInput = new BufferedReader(new FileReader("myAdjective.txt"), 350000);
        adverbInput = new BufferedReader(new FileReader("myAdverb.txt"),5000);
    	return num;
    }
}
 