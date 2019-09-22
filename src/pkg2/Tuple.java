package pkg2;

import java.util.Comparator;
public class Tuple  {
	    private String word;
	    private int position;

	    public Tuple (String word, int position) {
		    this.word = word;
		    this.position = position;
	    }

	    public String getWord() {
		    return this.word;
	    }

	    public int getPage() {
		    return this.position;
	    }

	    public static Comparator<Tuple> WordComparator = new Comparator<Tuple>() {

	    public int compare(Tuple s1, Tuple s2) {
		   String Word1 = s1.getWord().toUpperCase();
		   String Word2 = s2.getWord().toUpperCase();

	    	   //ascending order
	   	   return Word1.compareTo(Word2);

  	    }
	    };

	    @Override
	    public String toString() {
		  return "[ Word=" + word + ", Pos=" + position + "]";
	    }
}
