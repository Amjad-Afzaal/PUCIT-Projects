/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer;

/**
 *
 * @author Amjad Afzaal
 */
public class Token {
    
    private String name;
    private long value;

    public Token(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
    
    
}
