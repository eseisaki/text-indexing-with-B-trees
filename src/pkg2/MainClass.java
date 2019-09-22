package pkg2;

import java.io.*;
import java.util.*;


public class MainClass {
	
	public static void main( String[] args ) throws IOException
	{
		final int PAGE_SIZE=128;	
		
		File file = new File("textfile.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		String line = null;
		int line_count=0;
		int byte_count;
		int total_byte_count=0;
		int fromIndex;
		
		ArrayList<Tuple> arraylist = new ArrayList<Tuple>();
		
		while( (line = br.readLine())!= null ){
			line_count++;
			fromIndex=0;
        		// \\s+ means any number of whitespaces between tokens

    		String [] tokens = line.split(",\\s+|\\s*\\\"\\s*|\\s+|\\.\\s*|\\s*\\:\\s*");
    		String line_rest=line;
    			
    		for (int i=1; i <= tokens.length; i++) {
				byte_count = line_rest.indexOf(tokens[i-1]);
				
				if ( tokens[i-1].length() != 0){
				//System.out.println("(line:" + line_count + ", word:" + i + ", start_byte:" 
				//	+ (total_byte_count + fromIndex + byte_count) + "' word_length:" + tokens[i-1].length() + ") = " + tokens[i-1]);
				  arraylist.add(new Tuple(tokens[i-1], total_byte_count + fromIndex + byte_count));
				}
				fromIndex = fromIndex + byte_count + tokens[i-1].length();
				if (fromIndex < line.length())
				  line_rest = line.substring(fromIndex);
    			}
			total_byte_count += fromIndex + 2; // 2 bytes for CR
		}	
	//Sorting based on Word
	       System.out.println("Word Sorting:");
	 	Collections.sort(arraylist, Tuple.WordComparator);

		for (int i=0;i<arraylist.size();i++){
		   System.out.println(arraylist.get(i));
		}	
	
		
	BTree bt = new BTree(PAGE_SIZE/(16*2));
	String[] arr = { "Garbage","Barber" ,"Row","Allay", "Eagle",
			"Forest", "Rest", "Teach", "Clown", "Harry", "Kelvin",
	"Mayor", "Lake", "Zoro", "Xray" };
	for ( int i = 0; i < arr.length; i++ )
		bt.insert( arr[i] );
	
	bt.print(bt.getRoot());
	System.out.println("---------------------  ");
	bt.printNodes(bt.getRoot());
	System.out.println("---------------------  ");
	bt.printTree();
	System.out.println();
	System.out.println("---------------------  ");

	bt.searchForNode(bt.getRoot(), "Kelvin");
	
	ListNode ln;
	PostingList pl=new PostingList();
	
	 ln=new ListNode("Garbage1",1);
	 pl.insertListNode(ln, 1);
	 ln=new ListNode("Garbage2",1);
	 pl.insertListNode(ln, 1);
	 ln=new ListNode("Garbage3",1);
	 pl.insertListNode(ln, 1);
	 ln=new ListNode("Row1",2);
	 pl.insertListNode(ln, 2);
	 ln=new ListNode("Row2",2);
	 pl.insertListNode(ln, 2);
	 ln=new ListNode("Row3",2);
	 pl.insertListNode(ln, 2);
	 ln=new ListNode("Garbage4",1);
	 pl.insertListNode(ln, 1);
	 ln=new ListNode("Garbage5",1);
	 pl.insertListNode(ln, 1);
	 ln=new ListNode("Row4",2);
	 pl.insertListNode(ln, 2);
	 ln=new ListNode("Row5",2);
	 pl.insertListNode(ln, 2);
	 ln=new ListNode("Garbage6",1);
	 pl.insertListNode(ln, 1);
	 ln=new ListNode("Garbage7",1);
	 pl.insertListNode(ln, 1);
	 
	 pl.completeList();
	 
	}
	
	
}
