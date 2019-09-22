package pkg2;

import java.util.Comparator;

public class NodeKey {

	private String word;
	private int info;
	
	public NodeKey(String word, int info) {
		if(word.length()>12)
			this.word=word.substring(0, 11);
		else
			this.word=word;
	
		this.info = info;
	}

	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		if(word.length()>12)
			this.word=word.substring(0, 11);
		else
			this.word=word;
	}
	public int getInfo() {
		return info;
	}
	public void setInfo(int info) {
		this.info = info;
	}
	
	public static Comparator<NodeKey> WordComparator = new Comparator<NodeKey>() {

		public int compare(NodeKey s1, NodeKey s2) {
		String Word1 = s1.getWord().toUpperCase();
		String Word2 = s2.getWord().toUpperCase();

		//ascending order
		return Word1.compareTo(Word2);
		}
	};
	
}
