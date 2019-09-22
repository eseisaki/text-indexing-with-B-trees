  package pkg2;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BTree{
	
    private BTreeNode root;
    private int n; //2n is the maximum number of children a node can have
    private int height;
    private int NodesCounter;
    private TreeToDisk d;
    
/******************************************************************************************/
    public BTree(int n) throws FileNotFoundException{
    	d=new TreeToDisk();
    	root = new BTreeNode(n);
    	this.n = n;
    	NodesCounter=0;
    	height = 0;
    	root.pageCounter=0;
    }
/*******************************************************************************************/
    public void printHeight(){
    	System.out.println("Tree height is "+height);
    }
/*******************************************************************************************/
    public void insert(String newKey) throws IOException{
    	if (root.isFull()){//Split root;
    		splitRoot();
    		height++;
    	}
    	insertNonFull(root,newKey);
    } 
/********************************************************************************************/     
    /*	Insert new key to current node
	 We make sure that the current node is not full by checking and
	 splitting if necessary before descending to node
*/
	 public void insertNonFull(BTreeNode node,String newKey) throws IOException{
	//System.out.println("inserting " + newKey); // Debugging code
		 int i=node.count-1;
		 if (node.isLeaf){	//case current node is a leaf (and not full)
			 while ((i>=0) && (newKey.compareTo(node.key[i].getWord())<0)) { //linear search to the node (from end to start) 
				 node.key[i+1] = node.key[i];	//move keys once to the right
				 i--;
			 }
			 node.count++;
			 node.key[i+1]=new NodeKey(newKey,i);
			 d.WriteNode(node);
		 }
		 else{
			 while ((i>=0)&& (newKey.compareTo(node.key[i].getWord())<0)) {
				 i--;
			 }
			 int insertChild = i+1;  // Subtree where new key must be inserted
			 //DiskRead(node.child[insertChild]);
			 if (node.child[insertChild].isFull()){
			 	split(node,insertChild,newKey);
			 }
			 else
			 	insertNonFull(node.child[insertChild],newKey);
		 }
	 }	
	 
	 public void split(BTreeNode node,int insertChild,String newKey) throws IOException{
		 // The root of the subtree where new key will be inserted has to be split
		    // We promote the median key of that root to the current node and
		    // update keys and references accordingly
	
		    //System.out.println("This is the full node we're going to break ");
		    // Debugging     code
		    //c[insertChild].printNodes();
		    //System.out.println("going to promote " + c[insertChild].key[T-1]);
			  node.count++;
			  node.child[node.count]=node.child[node.count-1];
		    for(int j = node.count-1;j>insertChild;j--){
		    	node.child[j] =node.child[j-1];
		    	node.key[j] = node.key[j-1];
		    }
		    node.key[insertChild]= node.child[insertChild].key[n-1];
		    node.child[insertChild].count = n-1;
		    d.WriteNode(node);
		    NodesCounter++;
			node.child[insertChild].pageCounter=NodesCounter;
			d.WriteNode(node.child[insertChild]);
		    BTreeNode newNode = new BTreeNode(n);
		    for(int k=0;k<n-1;k++){
		      newNode.child[k] = node.child[insertChild].child[k+n];
		      newNode.key[k] = node.child[insertChild].key[k+n];
		    }
	
		    newNode.child[n-1] = node.child[insertChild].child[2*n-1];
		    newNode.count=n-1;
		    newNode.isLeaf =node.child[insertChild].isLeaf;
		    node.child[insertChild+1]=newNode;
		    NodesCounter++;
			node.child[insertChild+1].pageCounter=NodesCounter;
		    d.WriteNode(node.child[insertChild+1]);
		    
		    //System.out.println("This is the left side ");
		    //c[insertChild].printNodes(); 
		    //System.out.println("This is the right side ");
		    //c[insertChild+1].printNodes();
		    //c[insertChild+1].printNodes();
	
		    if ((newKey.compareTo(node.key[insertChild].getWord())<0)){
		    	insertNonFull(node.child[insertChild],newKey);
		    }
		    else{
		    	insertNonFull(node.child[insertChild+1],newKey);
		    }
		}
/**
 * @throws IOException *****************************************************************************************/
	    public void splitRoot() throws IOException{
	//Splits the root into three nodes.
	//The median element becomes the only element in the root
	//The left subtree contains the elements that are less than the median
	//The right subtree contains the elements that are larger than the median
	//The height of the tree is increased by one

	//System.out.println("Before splitting root");
	//root.printNodes(); // Code used for debugging
	    	BTreeNode leftChild = new BTreeNode(n);
	    	BTreeNode rightChild = new BTreeNode(n);
	    	leftChild.isLeaf = root.isLeaf;
	    	rightChild.isLeaf = root.isLeaf;
	    	leftChild.count = n-1;
	    	rightChild.count = n-1;
	    	int median = n-1;
			for (int i = 0;i<n-1;i++){
				leftChild.child[i] = root.child[i];
				leftChild.key[i] = root.key[i];
			}
			leftChild.child[median]= root.child[median];
			for (int i = median+1;i<root.count;i++){
				rightChild.key[i-median-1] = root.key[i];
			}
			rightChild.child[median]=root.child[root.count];
			root.key[0]=root.key[median];
			root.count = 1;
			root.child[0]=leftChild;
			root.child[1]=rightChild;
			NodesCounter++;
			root.child[0].pageCounter=NodesCounter;
			NodesCounter++;
			root.child[1].pageCounter=NodesCounter;
			root.isLeaf = false;
			d.WriteNode(root);
			d.WriteNode(root.child[0]);
			d.WriteNode(root.child[1]);
			//System.out.println("After splitting root");
			//root.printNodes();
		    }	   
/*******************************************************************************************/ 
	    class Ref{
	    	BTreeNode refNode;
	    	int refKeyPlace;
	    	
	    	Ref(BTreeNode n, int i){
	    		refNode=n;
	    		refKeyPlace=i;
	    	}
	    }	
/*******************************************************************************************/   
	    /*The correct child is chosen by performing a linear search of the values in the node. 
	    After finding the value greater than or equal to the desired value, the child pointer 
	    to the immediate left of that value is followed. If all values are less than the desired value,
	    the rightmost child pointer is followed. Of course, the search can be terminated as soon
	    as the desired node is found*/	
	    public Ref Btree_search(BTreeNode searchNode,String key){
	    	Ref r;
	    	int i=1;
	    	//Increment of i for all the time that the searching key is greater than all the
	    	//other keys who are stored in this node
	    	//We make a linear search of the values in the node
	    	while(i<=searchNode.count && key.compareTo(searchNode.key[i].getWord())>0){
	    		i++;
	    	}
	    	//We skip loop,only if the key is smaller or equal with some key
	    	//or if we have reached the end of the node without finding the key
	    	if(i<=searchNode.count && key.compareTo(searchNode.key[i].getWord())==0){
	    		r=new Ref(searchNode,i);
	    		return r;	//We have successfully found the key
	    	}				//so the search is over
	    	if(searchNode.isLeaf){
	    		return null;	//Unsuccessful search in this node who is a leaf
	    						//so the search is over
	    	}
	    	else{
	    		return Btree_search(searchNode.child[i],key);	//keep searching
	    	}	
	    }    
/**
 * @throws IOException *****************************************************************************************/   	    
    public void print(BTreeNode node) {
  
    	//Prints all keys in the tree in ascending order
    	if (node.isLeaf){
    	  for(int i =0; i<node.count;i++)
    	    System.out.print(node.key[i].getWord()+" ");
    	  System.out.println();
    	}
    	else{
    	  for(int i =0; i<node.count;i++){
    		  print( node.child[i]);
    	    System.out.print(node.key[i].getWord()+" ");
    	  }
    	  print(node.child[node.count]);
    	}
    	
    }  
/*******************************************************************************************/
	 public void printNodes(BTreeNode node){
	//Prints all keys in the tree, node by node, using preorder
	//It also prints the indicator of whether a node is a leaf
	//Used mostly for debugging purposes
		 node.printNode();
		if (!node.isLeaf){
			for(int i =0; i<=node.count;i++){
				printNodes(node.child[i]);
			}
		}
	
	 }    
/*******************************************************************************************/	 
	 public void printTree() throws IOException{
		 d.ReadTree(root);
		 
	 }
	 
	 public void searchForNode(BTreeNode auxnode,String key) throws IOException{
		 d.SearchTree(auxnode, key);
	 }
/*******************************************************************************************/    
	 public BTreeNode getRoot() {
		 return root;
	 }
	 public void setRoot(BTreeNode root) {
		 this.root = root;
	 }
}