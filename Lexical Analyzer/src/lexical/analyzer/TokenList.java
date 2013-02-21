/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amjad Afzaal
 */
public class TokenList {
    
    private ArrayList <Token> tokenlist;

    public TokenList() {
        this.tokenlist = new ArrayList <Token>();
    }

    public ArrayList <Token> getTokenlist() {
        return tokenlist;
    }

    public void setTokenlist(ArrayList <Token> tokenlist) {
        this.tokenlist = tokenlist;
    }
    
    public void add(Token token)
    {
        this.tokenlist.add(token);
    }
    
    public Token get(int index)
    {
        return this.tokenlist.get(index);
    }
    
    public void writeTokenFile()
    {
        try 
        {
            BufferedWriter bw = new BufferedWriter (new FileWriter("token.txt"));
            for(int i=0; i<this.tokenlist.size(); i++)
            {
                String line;
                if(this.tokenlist.get(i).getValue() == -1)
                     line = "<" + this.tokenlist.get(i).getName() + ">";
                else
                    line = "<" + this.tokenlist.get(i).getName() + "," 
                        + this.tokenlist.get(i).getValue() + ">";
                bw.write(line);
                bw.newLine();
            }
            
            bw.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(TokenList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
