package pkg2;

public class BTreeNode{
	  public NodeKey[] key; //Array with the keys of the node
	  public BTreeNode[] child;	//Array with pointers to the children of the node
	  boolean isLeaf;	//Says if a node is a leaf
	  public int count;	
	  private int n; //Each node has at least n-1 and at most 2n-1 keys
	  public int pageCounter;
 /*******************************************************************************************/
	  public  BTreeNode(int n){
		   this.n = n;
		   isLeaf = true;
		   key = new NodeKey[2*n-1];
		   child = new BTreeNode[2*n];
		   count=0; 
	 }
/*******************************************************************************************/
	 public boolean isFull(){
		 return count==(2*n-1);
	 }
 /*******************************************************************************************/	 
	 public void printNode(){
		 //Prints all keys in node
		 for(int i =0; i<count;i++)
			 System.out.print(key[i].getWord()+" ");
		 System.out.println(isLeaf);
	 	}
	 
	
}