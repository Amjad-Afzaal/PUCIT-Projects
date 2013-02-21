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
public class SymbolTable {

    private ArrayList <SymbolTableEntry> symbolTable;

    public SymbolTable() {
        
        this.symbolTable = new ArrayList <SymbolTableEntry> ();
    }

    public ArrayList<SymbolTableEntry> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(ArrayList<SymbolTableEntry> symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    public void add(SymbolTableEntry symbolTableEntry)
    {
        this.symbolTable.add(symbolTableEntry);
    }
    
    public SymbolTableEntry get (int index)
    {
        return this.symbolTable.get(index);
    }
    
    public void writeSymbolTableFile()
    {
        try 
        {
            BufferedWriter bw = new BufferedWriter (new FileWriter ("symboltbl.txt"));
            for(int i=0; i<this.symbolTable.size(); i++)
            {
                String line = this.symbolTable.get(i).getSymbolName() + " "
                        + this.symbolTable.get(i).getFoundFirst() + " " 
                        + this.symbolTable.get(i).getFoundLast();
                bw.write(line);
                bw.newLine();
            }
            
            bw.close();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(SymbolTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public long getTokenNumberIfItAlreadyExist (String token)
    {
        for(int i=0; i<this.symbolTable.size(); i++)
        {
            if(this.symbolTable.get(i).getSymbolName().equals(token))
                return this.symbolTable.get(i).getIndex();
        }
        return 0;
    }
    
    public boolean ifTokenAlreadyExist (String token)
    {
        for(int i=0; i<this.symbolTable.size(); i++)
        {
            if(this.symbolTable.get(i).getSymbolName().equals(token))
                return true;
        }
        return false;
    }
    
    public void setSymbolTableEntryFoundLastProperty(String symbolName, long foundLastValue)
    {
        for(int i=0; i<this.symbolTable.size(); i++)
        {
            if(this.symbolTable.get(i).getSymbolName().equals(symbolName))
            {
                this.symbolTable.get(i).setFoundLast(foundLastValue);
            }
        }
    }

}
