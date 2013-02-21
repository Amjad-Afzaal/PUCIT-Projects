/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer;

/**
 *
 * @author Amjad Afzaal
 */
public class SymbolTableEntry {

    private String symbolName;
    private long index;
    private long foundFirst;
    private long foundLast;

    public SymbolTableEntry(String symbolName, long index, long foundFirst, long foundLast) {
        this.symbolName = symbolName;
        this.index = index;
        this.foundFirst = foundFirst;
        this.foundLast = foundLast;
    }

    public long getFoundFirst() {
        return foundFirst;
    }

    public void setFoundFirst(long foundFirst) {
        this.foundFirst = foundFirst;
    }

    public long getFoundLast() {
        return foundLast;
    }

    public void setFoundLast(long foundLast) {
        this.foundLast = foundLast;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }
    
    
}
