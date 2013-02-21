// <editor-fold defaultstate="collapsed" desc="comment">
package documentcomparer;// </editor-fold>

public class Classifier {
	
	public String tag;
	public String word;
	public int weight;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void print()
    {
        System.out.println("Tag = " + tag + "\tWord = " + word + "\tWeight = " + weight);
    }

}
