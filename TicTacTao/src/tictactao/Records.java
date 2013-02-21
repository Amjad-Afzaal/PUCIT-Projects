/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Amjad Afzaal
 */
public class Records 
{
    private Player [] people;
    private int free;
    public Records() 
    {
        this.people = new Player [10];
        this.free = 9;
        for(int i=0; i<10; i++)
	{
            Player temp = new Player("", "", '-', 0);
            this.people[i] = temp;
	}
    }
    
    public void add(Player p)
    {
        if(free >= 0)
        {
            this.people[free] = p;
            free --;
        }
            
        else
        {
            
            free = 9;
            this.sort();
            this.people[free] = p;
            free --;
        }
        
        
    }

    public void sort() 
    {
        for(int i=0; i<10; i++)
	{
            for(int j=0; j<9; j++)
            {
		if(this.people[j].getScore() < this.people[j+1].getScore())
		{
                    Player temp = this.people[j];
                    this.people[j] = this.people[j+1];
                    this.people[j+1] = temp;
		}
            }
	}
    }
    
    
    public void read()
    {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream("records.txt"))));
            String line, token[];
           
            while((line = br.readLine()) != null)
            {
           
                if(!line.equals(""))
                {
                    token = line.split(",");
                    Player p = new Player(token[0], token[1], '-', Integer.parseInt(token[2]));
             
                    this.add(p);
                    
                }
             
                
            }
            br.close();
            
        }
         catch (IOException ex) {
            Logger.getLogger(Records.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.sort();
        free = 9;
        
    }
    
    public void write()
    {
        this.sort();
        try {
            BufferedWriter pw = new BufferedWriter(new FileWriter("records.txt"));
            
            for(int i=0; i<10; i++)
            {
                
                if(this.people[i].getName().equals(""))
                    continue;
              
                String line = this.people[i].getName() + "," + this.people[i].getLevel()
                        + "," + this.people[i].getScore();
                
                pw.write(line + "\n");
                pw.newLine();
              
            }
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(Records.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Player returnPlayer(int i)
    {
        return this.people[i];
    }
    
    public void display()
    {
        for(int i=0; i<10; i++)
        {
            JOptionPane.showMessageDialog(null, i +  " "  + this.people[i].getName() + " "
                    + this.people[i].getLevel() + " " + this.people[i].getScore());
        }
    }
}
