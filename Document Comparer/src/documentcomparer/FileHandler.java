/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package documentcomparer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author amjad
 */
public class FileHandler {

    public static void openFile(String f1, String f2) throws FileNotFoundException, IOException
    {
        BufferedReader br1 = new BufferedReader(new FileReader(f1));
        BufferedReader br2 = new BufferedReader(new FileReader(f2));

        // Reading document 1
        String d1 = "", temp;
        try {
            while ((temp = br1.readLine()) != null) {
                d1 = d1 + temp;
            }
        }
        catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        temp = null;
        //JOptionPane.showMessageDialog(null, d1);

        // Reading Document 2

        String d2 = "";
        try {
            while ((temp = br2.readLine()) != null) {
                d2 = d2 + temp;
            }
        }
        catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        //JOptionPane.showMessageDialog(null, d2);

        // Parsing Documents
        String d1sen [], d2sen [];
        d1sen = d1.split("\\.");
        d2sen = d2.split("\\.");
        //System.out.println("Document 1 \n");
        //for(int i=0; i<d1sen.length; i++)
          //  System.out.println(d1sen[i] + "\n");
        //System.out.println("\nDocument 2 \n");
        //for(int i=0; i<d2sen.length; i++)
          //  System.out.println(d2sen[i] + "\n");

        ArrayList arr1 = new ArrayList(), arr2 = new ArrayList();
        arr1.addAll(Arrays.asList(d1sen));
        arr2.addAll(Arrays.asList(d2sen));

        // Creating documents objects

        Document doc1 = new Document(arr1), doc2 = new Document(arr2);
        Comparer obj = new Comparer();
        obj.compare(doc1, doc2);
        JOptionPane.showMessageDialog(null,"After Comparing document 1 vs document 2");
        doc1.print();
        JOptionPane.showMessageDialog(null,"After Comparing document 2 vs document 1");
        doc2.print();
        br1.close();
        br2.close();
    }

}
