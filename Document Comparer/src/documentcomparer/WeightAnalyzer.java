
package documentcomparer;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import java.awt.*;
/*import org.jfree.chart.*;
import org.jfree.chart.title.*;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.*;*/

public class WeightAnalyzer 
{
	public int analyzeWeights(ArrayList<Classifier> classifier)
	{
		int sentiment = 0;
        for(int i=0; i<classifier.size(); i++)
        {
                Classifier get = classifier.get(i);
                get.print();
        	if(sentiment==0)
        	{
        		sentiment += classifier.get(i).weight;
        	}
        	else if(sentiment<0)
        	{
        		if(classifier.get(i).weight>0)
        		{
        			sentiment *= classifier.get(i).weight; 
        		}
        		else if(classifier.get(i).weight<0)
        		{
        			sentiment += classifier.get(i).weight;
        		}
        	}
        	else
        	{
        		if(classifier.get(i).weight>0)
        		{
        			sentiment += classifier.get(i).weight;
        		}
        		else if(classifier.get(i).weight<0)
        		{
        			sentiment *= classifier.get(i).weight;
        		}
        	}
                //this.classifyAnalyzedWeight(sentiment);
        	
        }
        return sentiment;
	}
	
	public void classifyAnalyzedWeight(int analyzedWeight)
	{
		JOptionPane dialoge = new JOptionPane();
        
        if(analyzedWeight>0)
        	dialoge.showMessageDialog(null, "POSITIVE");
        else if(analyzedWeight<0)
        	dialoge.showMessageDialog(null, "NEGATIVE");
        else dialoge.showMessageDialog(null, "NEUTRAL");
	}
	
	/*public void drawgraph(int val)
	{		
				DefaultPieDataset pieDataset = new DefaultPieDataset();
				
				if(val == 0)
				{
					pieDataset.setValue("NEUTRAL", new Integer(100));					
				}
				else if(val == 1)
				{
					pieDataset.setValue("POSITIVE", new Integer(75));
					pieDataset.setValue("NEUTRAL", new Integer(25));
				}
				else if(val == 2)
				{
					pieDataset.setValue("POSITIVE", new Integer(100));					
				}
				else if(val == -1)
				{
					pieDataset.setValue("NEGATIVE", new Integer(75));
					pieDataset.setValue("NEUTRAL", new Integer(25));
				}
				else if(val == -2)
				{
					pieDataset.setValue("NEGATIVE", new Integer(100));					
				}
				
				
				JFreeChart chart = ChartFactory.createPieChart
				("Graphical Sentiment Analysis", pieDataset, true,true,true);

				ChartFrame frame1=new ChartFrame("Analysis Pie Chart",chart);
		  
				frame1.setVisible(true);
		  
				frame1.setSize(300,300);
		 
	}
*/
}
