
package documentcomparer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordTagSplitter 
{
	public void splitWordTag(String[] tokens, ArrayList<Classifier> classifier)
	{
        Pattern pattern = Pattern.compile("/*VB/*");
        Pattern pattern1 = Pattern.compile("/*JJ/*");
        Pattern pattern2 = Pattern.compile("/*RB/*");
        for(int i=0; i<tokens.length; i++)
        {
        	Classifier obj = new Classifier();
        	obj.tag = tokens[i];
        	Matcher verbMatch = pattern.matcher(tokens[i]);
            Matcher adjectiveMatch = pattern1.matcher(tokens[i]);
            Matcher adverbMatch = pattern2.matcher(tokens[i]);
        	if(verbMatch.find())
        	{
        		obj.word = (tokens[i].split("\\/"))[0];
        	}
        	else if(adjectiveMatch.find())
        	{
        		obj.word = (tokens[i].split("\\/"))[0];
        	}
        	else if(adverbMatch.find())
        	{
        		obj.word = (tokens[i].split("\\/"))[0];
        	}
        	classifier.add(obj);
        }
	}
}
