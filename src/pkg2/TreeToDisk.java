package pkg2;

import java.io.*;

public class TreeToDisk {
	
	  private static final int PAGE_SIZE = 128; // Default Data Page size
	  public RandomAccessFile TreeFile;

	
/*******************************************************************************************/	  
	  public TreeToDisk() throws FileNotFoundException{
		  TreeFile = new RandomAccessFile ("tree", "rw");
	  }
/*******************************************************************************************/
	  public void WriteNode(BTreeNode auxnode) throws IOException{
		  TreeFile = new RandomAccessFile ("tree", "rw");
		  
		   ByteArrayOutputStream bos = new ByteArrayOutputStream();
		   DataOutputStream dos = new DataOutputStream(bos);
		 // System.out.println(auxnode.pageCounter);
		  String s;
		  int o; 
		 // byte[] src;
		  //byte[] dst=new byte[12]; 
		 
		  for(int i=0;i<auxnode.count;i++){
		//	System.out.println(auxnode.key[i].getWord()); 
		  	s=auxnode.key[i].getWord();
		  	o=auxnode.key[i].getInfo();
		  
		  	//src=s.getBytes();
		  	//System.arraycopy(src, 0, dst, 1, src.length); 
		  	dos.writeUTF(s);
		  	//dos.write(dst, 0, 12); // Write fixed size string (12 bytes)
		  	dos.writeInt(o);
		  } 
		  dos.close();
		// Get the bytes of the serialized object
		  byte[] buf = bos.toByteArray(); // Creates a newly allocated byte array.
		// System.out.println("\nbuf size: " + buf.length + " bytes");
		  byte[] DataPage = new byte[PAGE_SIZE];
		  System.arraycopy( buf, 0, DataPage, 0, buf.length); // Copy buf data to DataPage of DataPageSize
		  bos.close();
		  // write to the file	
		  TreeFile.seek(auxnode.pageCounter*PAGE_SIZE);
		  TreeFile.write(DataPage);
		  TreeFile.close();
	  }
/*******************************************************************************************/	  
	  public void ReadNode(BTreeNode auxnode) throws IOException{
		  TreeFile = new RandomAccessFile ("tree", "rw");
		  byte[] ReadDataPage = new byte[PAGE_SIZE];
		  ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
		  DataInputStream ois= new DataInputStream(bis);
		  
		  TreeFile.seek(auxnode.pageCounter*PAGE_SIZE);
		  TreeFile.read(ReadDataPage);
			
			for(int i=0;i<auxnode.count;i++){
			System.out.println();
			System.out.print("  " +ois.readUTF());
			System.out.print(" ," + ois.readInt());
		 	}
			TreeFile.close();
	  }
/*******************************************************************************************/	  	  
	  public int FindNode(BTreeNode auxnode,String key) throws IOException{
		  TreeFile = new RandomAccessFile ("tree", "rw");
		  byte[] ReadDataPage = new byte[PAGE_SIZE];
		  ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
		  DataInputStream ois= new DataInputStream(bis);
		  String word;
		  int info;
		  TreeFile.seek(auxnode.pageCounter*PAGE_SIZE);
		  TreeFile.read(ReadDataPage);
			
			for(int i=0;i<auxnode.count;i++){
			
					word=ois.readUTF();
					info=ois.readInt();
					if(key.equals(word)){
						System.out.println("Key found with info:"+info);
						TreeFile.close();
						return 1;
					}
		 	}
			TreeFile.close();
			return -200;
	  }
 /*******************************************************************************************/	   
	  public void ReadTree(BTreeNode auxnode) throws IOException{
		  
		  ReadNode(auxnode);
		  if(!auxnode.isLeaf){
			  for(int i =0; i<=auxnode.count;i++){
				  ReadTree(auxnode.child[i]);
			  }
		  }
	  }
/*******************************************************************************************/	   	  
	  public void SearchTree(BTreeNode auxnode,String key) throws IOException{
		  int res;
		 res=FindNode(auxnode,key);
		  if(res==-200 || !auxnode.isLeaf){
			  for(int i =0; i<=auxnode.count;i++){
				 res= FindNode(auxnode.child[i],key);
				  if(res!=-200)
					  break;
			  } 
		  }
		  if(res==-200)
		  {
			  System.out.println("This key does not exist!!");
		  }
		
	  }

}
