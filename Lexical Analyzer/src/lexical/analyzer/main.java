/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Amjad Afzaal
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // Open the input file
        BufferedReader br = new BufferedReader(new FileReader("abc.tpl"));
        // Open the output file for writing errors
        BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
        // Load all the transition Table in memory
        TransitionTableList transitionTableList = new TransitionTableList();
        transitionTableList.readAllTransitionTable();
        // Creating Token List which will be write in token.txt
        TokenList tokenList = new TokenList();
        // Creating Symbol Table which will be write in symboltbl.txt
        SymbolTable symbolTable = new SymbolTable();
        // Creating a variable to track the line number in source file
        long lineNumber = 0;
        // Creating token number;
        long tokenNumber = 0;// foundFirst = 0, foundLast = 0;
        // Creating Buffer
        String line;
        // Reading the line from the source code untill fill ends and perform all operation of lexical analyzer in this loop
        while((line = br.readLine()) != null)
        {
            // increasing line number
            String originolLine = line;
            lineNumber ++;
            // Creating two pointers i.e current and forward pointer
            int current = 0, forward = 0;
            // Took the first character in the buffer and passed it to transition table to verify and then use forward pointer to verify characters we run the loop untill forward pointer goes location where we found invalid character or buffer ends.
            line = main.removeSpaces(line);
            line = line + " ";
            System.out.println("Line is \"" + line + "\"");
            
            while(forward < line.length())
            {
                forward = current + 1;
                if(forward > line.length())
                        break;
                // Check for comments
                if(line.charAt(current) == '/' && line.charAt(current + 1) == '/')
                    break;
                // Create a token that will match with longest matching DFA
                    String name = "", validToken = "";
                    boolean flag = false, forwardFlag = false, validTokenFlag = false;
                    if(forward < line.length() && line.charAt(forward - 1) == '!' && line.charAt(forward) == '=')
                    {
                        forwardFlag = true;
                        validToken = line.substring(forward - 1, forward + 1);
                        forward += 2;
                    }
                    else if(forward < line.length() && line.charAt(forward - 1) == '|' && line.charAt(forward) == '|')
                    {
                        forwardFlag = true;
                        validToken = line.substring(forward - 1, forward + 1);
                        forward += 2;
                    }
                    else if(forward < line.length() && line.charAt(forward - 1) == '&' && line.charAt(forward) == '&')
                    {
                        forwardFlag = true;
                        validToken = line.substring(forward - 1, forward + 1);
                        forward += 2;
                    }
                    // Check for error
                if(forwardFlag == false && 
                        transitionTableList.isTokenFoundInAnyTable(line.substring(current, current + 1)) == false)
                {
                    if(!(line.substring(current, current + 1).equals(" ")) &&
                        !(line.substring(current, current + 1).equals("\t")) )
                    {
                        System.out.println("Invalid symbol \"" + line.substring(current, current + 1) + "\""
                            + " at line number " + lineNumber);
                        bw.write("Invalid symbol \"" + line.substring(current, current + 1) + "\""
                            + " at line number " + lineNumber);
                        bw.newLine();
                    }
                    current = current + 1;
                    forward = current;
                    continue;
                }
                // Finiding validToken
                    while(forwardFlag == false && forward < line.length() && transitionTableList.isTokenValid(line.substring(current, forward)))
                    {
                        while(forward < line.length() && line.charAt(forward) == '_')
                            forward ++;
                        if(flag == false && line.charAt(forward) == '.' && (line.charAt(forward - 1) >= 48 && line.charAt(forward + 1) <= 57))
                        {
                            forward += 2 ;
                            flag = true;
                            continue;
                        }
                        if(forward < line.length() && line.charAt(forward - 1) == '<' && line.charAt(forward) == '=')
                        {
                            forwardFlag = true;
                            validToken = line.substring(forward - 1, forward + 1);
                            forward += 2;
                            break;
                        }
                        if(forward < line.length() && line.charAt(forward - 1) == '>' && line.charAt(forward) == '=')
                        {
                            forwardFlag = true;
                            validToken = line.substring(forward - 1, forward + 1);
                            forward += 2;
                            break;
                        }
                        if(forward < line.length() && line.charAt(forward - 1) == '=' && line.charAt(forward) == '=')
                        {
                            forwardFlag = true;
                            validToken = line.substring(forward - 1, forward + 1);
                            forward += 2;
                            break;
                        }
                        forward++;
                    }
                    
                    if(forward > line.length())
                        break;
                    
                    // Adding the token in tokenList
                    while(forward >= 0 && transitionTableList.isTokenValid(line.substring(current, forward)) == false)
                    {
                        forward -- ;
                        validTokenFlag = true;
                    }
                    
                    if(validTokenFlag)  {
                        validToken = line.substring(current, forward);
                        forward ++ ;
                    }
                    if(forwardFlag == false && validTokenFlag == false)    validToken = line.substring(current, forward - 1);
                    //if(dotFlag) validToken = validToken + line.substring(current, forward - 2);
                    if(transitionTableList.getMarkedTableName().equals("identifier"))
                        name = "id";
                    else if(transitionTableList.getMarkedTableName().equals("number"))
                        name = "number";
                    else if(transitionTableList.getMarkedTableName().equals("Arthimetic Operator"))
                        name = validToken;
                    else if(transitionTableList.getMarkedTableName().equals("Comparison Operator"))
                        name = validToken;
                    else if(transitionTableList.getMarkedTableName().equals("Keyword if"))
                        name = validToken;
                    else if(transitionTableList.getMarkedTableName().equals("Keyword while"))
                        name = validToken;
                    else if(transitionTableList.getMarkedTableName().equals("Keyword break"))
                        name = validToken;
                    else if(transitionTableList.getMarkedTableName().equals("Keyword print"))
                        name = validToken;
                    else if(transitionTableList.getMarkedTableName().equals("Curly Brackets"))
                        name = validToken;
                    else if(transitionTableList.getMarkedTableName().equals("Round Brackets"))
                        name = validToken;
                    Token token = new Token (name, 
                            symbolTable.getTokenNumberIfItAlreadyExist(validToken) == 0 ? 
                            tokenNumber +=1 : symbolTable.getTokenNumberIfItAlreadyExist(validToken));
                    //Token is added in the tokenList
                    System.out.println("<" + validToken + "," + token.getValue() + ">");
                    tokenList.add(token);
                    transitionTableList.setAllTablesUnMarked();
                    // Doing the symbol Table Entry
                    name = validToken;
                    if(symbolTable.ifTokenAlreadyExist(name))
                    {
                        symbolTable.setSymbolTableEntryFoundLastProperty(name, lineNumber); 
                    }
                    else
                    {
                        SymbolTableEntry ste = new SymbolTableEntry (name, tokenNumber, lineNumber, lineNumber);
                        symbolTable.add(ste);
                    }
                    current = forward - 1;
            }
            
        }
        
        tokenList.writeTokenFile();
        symbolTable.writeSymbolTableFile();
        br.close();
        bw.close();
    }
    
    public static String removeSpaces(String line)
    {
        String newLine = "";
        for(int i=0; i<line.length(); i++)
        {
            if(line.charAt(i) == ' ' && line.charAt(i) == '\t')
                continue;
            newLine += line.charAt(i);
        }
        return newLine;
    }
}