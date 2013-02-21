/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer;

/**
 *
 * @author Amjad Afzaal
 */
public class TransitionTable {
    
    private String tableName;
    private int noOfStates;
    private int noOfAlphabets;
    private String transitionTable [] [] ;
    private String finalState;
    private boolean isMarked;
    private String deadState;

    public TransitionTable(String tableName, String deadState, int noOfStates, int noOfAlphabets) {
        this.tableName = tableName;
        this.noOfStates = noOfStates;
        this.noOfAlphabets = noOfAlphabets;
        this.finalState = "";
        this.isMarked = false;
        this.deadState = deadState;
        this.transitionTable = new String [this.noOfStates + 1][this.noOfAlphabets + 1];
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDeadState() {
        return deadState;
    }

    public void setDeadState(String deadState) {
        this.deadState = deadState;
    }

    
    public int getNoOfAlphabets() {
        return noOfAlphabets;
    }

    public void setNoOfAlphabets(int noOfAlphabets) {
        this.noOfAlphabets = noOfAlphabets;
    }

    public int getNoOfStates() {
        return noOfStates;
    }

    public void setNoOfStates(int noOfStates) {
        this.noOfStates = noOfStates;
    }

    public String[][] getTransitionTable() {
        return transitionTable;
    }

    public void setTransitionTable(String [] [] transitionTable) {
        this.transitionTable = transitionTable;
    }

    public String getFinalState() {
        return finalState;
    }

    public void setFinalState(String finalState) {
        this.finalState = finalState;
    }
    
    public void markFinalState()
    {
        for(int i=0; i<=this.noOfStates; i++)
        {
            if(this.transitionTable[i][0].charAt(0) == '+')
            {
                this.finalState = this.transitionTable[i][0] + " " + this.finalState;
            }
        }
        
    }

    public boolean isIsMarked() {
        return isMarked;
    }

    public void setIsMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }
    
    public boolean isTokenIsInThisTable(String token)
    {
        int flag = 0;
        for(int i=1; i<=this.noOfAlphabets; i++)
        {
            if(this.transitionTable[0][i].charAt(0) == token.charAt(0))
                flag = 1;
        }
        if(flag == 0)
            return false;
        
        String currentState = this.transitionTable[1][0];
        for(int i=0; i<token.length(); i++)
        {
            if(this.columnValue(token.charAt(i)) != 0)
                currentState = this.transitionTable[this.rowValue(currentState)][this.columnValue(token.charAt(i))];
            else
                currentState = "";
            if(currentState.equals(this.deadState))
                return false;
        }
        String tokens[];
        tokens = this.finalState.split(" ");
        for(int i=0; i<tokens.length; i++)
        {
            if(currentState.equals(tokens[i]))
            {
                this.isMarked = true;
                return true;
            }
        }
        return false;
    }
    
    public void printTable()
    {
        //System.out.println(this.tableName + " = " + this.isMarked);
        for(int i=0; i<=this.noOfStates; i++)
        {
            for(int j=0; j<=this.noOfAlphabets; j++)
                System.out.print(this.transitionTable[i][j] + "\t");
            System.out.println();
        }
    }

    private int columnValue(char alphabet)
    {
        for(int i=1; i<=this.noOfAlphabets; i++)
        {
            if(this.transitionTable[0][i].charAt(0) == alphabet)
                return i;
        }
        return 0;
    }
    
    private int rowValue(String state)
    {
        for(int i=1; i<=this.noOfStates; i++)
        {
            if(this.transitionTable[i][0].equals(state))
                return i;
        }
        return 0;
    }
}
