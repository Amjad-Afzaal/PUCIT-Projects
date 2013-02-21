
package documentcomparer;
import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class POSTagger 
{
        private  MaxentTagger tagger;

    POSTagger()
    {
        try {
            tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
        } catch (IOException ex) {
            Logger.getLogger(POSTagger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(POSTagger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String tagSentence(String sentence) throws IOException, ClassNotFoundException
    {
	 
	 String taggedSentence = tagger.tagString(sentence);
	 return taggedSentence;
    }
}
