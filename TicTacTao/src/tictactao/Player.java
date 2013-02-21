/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactao;

/**
 *
 * @author Amjad Afzaal
 */
public class Player {
    
    private String name, level;
    private char marker;
    private int score;

    public Player(String name, String level, char marker, int score) {
        this.name = name;
        this.level = level;
        this.marker = marker;
        this.score = score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public char getMarker() {
        return marker;
    }

    public void setMarker(char marker) {
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    
    
}
