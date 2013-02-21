/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amjad Afzaal
 */
public class TransitionTableList {
    
    private ArrayList <TransitionTable> transitionTableList;

    public TransitionTableList() {
        
        this.transitionTableList = new ArrayList <TransitionTable> ();
    }

    public ArrayList<TransitionTable> getTransitionTableList() {
        return transitionTableList;
    }

    public void setTransitionTableList(ArrayList<TransitionTable> transitionTableList) {
        this.transitionTableList = transitionTableList;
    }
    
    public void readAllTransitionTable()
    {
        try 
        {
            BufferedReader br = new BufferedReader (new FileReader ("transitiontbl.txt"));
            String noOfTables = br.readLine();
            //System.out.println(noOfTables);
            for(int i=0; i<Integer.parseInt(noOfTables); i++)
            {
                String tableName = br.readLine();
                //System.out.println(tableName);
                int noOfStates = Integer.parseInt(br.readLine()), noOfAlphabets = Integer.parseInt(br.readLine());
                //System.out.println(noOfStates);
                //System.out.println(noOfAlphabets);
                String deadState = br.readLine();
                TransitionTable tt = new TransitionTable (tableName, deadState, noOfStates, noOfAlphabets);
                String arr [] [] = new String [noOfStates + 1] [noOfAlphabets + 1];
                for(int j=0; j<=noOfStates; j++)
                {
                    String line = br.readLine(), token [];
                    //System.out.println(line);
                    token = line.split(" ");
                    //System.out.println(token.length);
                    for(int k=0; k<=noOfAlphabets; k++)
                    {
                      //  System.out.println("j = " + j + "\tk = " + k);
                        arr[j][k] = token[k];
                    }
                        
                }
                tt.setTransitionTable(arr);
                tt.markFinalState();
                this.transitionTableList.add(tt);
                br.readLine();
            }
            
            br.close();
        }
        catch (Exception ex) 
        {
            Logger.getLogger(TransitionTableList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isTokenValid(String token)
    {
        for(int i=0; i<this.transitionTableList.size(); i++)
        {
            if(this.transitionTableList.get(i).isTokenIsInThisTable(token))
            {
                for(int j=0; j<this.transitionTableList.size(); j++)
                {
                    if(j == i)
                        continue;
                    this.transitionTableList.get(j).setIsMarked(false);
                }
                return true;
            }
        }
        return false;
    }
    
    public String getMarkedTableName()
    {
        for(int i=0; i<this.transitionTableList.size(); i++)
        {
            if(this.transitionTableList.get(i).isIsMarked())
                return this.transitionTableList.get(i).getTableName();
        }
        return "";
    }
    
    public void setAllTablesUnMarked()
    {
        for(int i=0; i<this.transitionTableList.size(); i++)
            this.transitionTableList.get(i).setIsMarked(false);
    }
    
    public boolean isTokenFoundInAnyTable(String token)
    {
        for(int i=0; i<this.transitionTableList.size(); i++)
        {
            if(this.transitionTableList.get(i).isTokenIsInThisTable(token))
                return true;
        }
        return false;
    }
    
    public void printAllTables()
    {
        for(int i=0; i<this.transitionTableList.size(); i++)
            this.transitionTableList.get(i).printTable();
    }
    
    
}
