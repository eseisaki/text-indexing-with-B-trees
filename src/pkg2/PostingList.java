package pkg2;

import java.io.*;
import java.util.*;


public class PostingList {
	
	  private static final int PAGE_SIZE = 128; // Default Data Page size
	  public RandomAccessFile ListFile;
	  List <List<ListNode>> mylist= new <List<ListNode>>ArrayList();
	  private int pageCounter=0;
/**************************************************************************************************************/	  
	public void insertListNode(ListNode newnode,int info) throws IOException{
		
		
		if(mylist.size()>=info){ 	//check if we already have this word in the search_machine	
			if( mylist.get(info-1).size()<3 ){	//check if we have reach the limits of the word's list
				mylist.get(info-1).add(newnode);
				System.out.println("We are here!!"+mylist.size());
				if(mylist.get(info-1).size()==3){
					PageToDisk(mylist.get(info-1),0,info); 
				}
			}
			else {
					System.out.println("Write Page to disk....");
					PageToDisk(mylist.get(info-1),pageCounter,info); //if the page is full write this page to disk and create a new one	
					System.out.println("New SUUBnode!!");
					mylist.get(info-1).clear();
					mylist.get(info-1).add(newnode);
					pageCounter++;
					
			}
		}
		
		else		//New node to the list with the insertion of the first element
		{		
			System.out.println("New node!!");
			List<ListNode> auxlist=new <List<ListNode>>ArrayList();
			auxlist.add(newnode);
			mylist.add(auxlist);
			pageCounter++;
		}
	}
	
/**************************************************************************************************************/
	
	public void completeList() throws IOException{
		for(int i=0;i<mylist.size();i++){
				if(mylist.get(i).size()<2){
					PageToDisk(mylist.get(i),0,i); 
				}
			}
	}
/**************************************************************************************************************/
		
	public void PageToDisk(List<ListNode> list , int link, int info) throws IOException{
		
		 ListFile = new RandomAccessFile ("postingList", "rw");
		 
		   ByteArrayOutputStream bos = new ByteArrayOutputStream();
		   DataOutputStream dos = new DataOutputStream(bos);
		 
		  for(int i=0;i<list.size();i++){
		  		dos.writeUTF(list.get(i).getFileName());
		  		dos.writeInt(list.get(i).getLinebytes());
		  } 
		  if(link!=0){
			  dos.writeUTF( Integer.toString(link));
		  }
		  else{
			  dos.writeUTF("NULL");
		  }
		  dos.close();
		  // Get the bytes of the serialized object
		  byte[] buf = bos.toByteArray(); // Creates a newly allocated byte array.
		  // System.out.println("\nbuf size: " + buf.length + " bytes");
		  byte[] DataPage = new byte[PAGE_SIZE];
		  System.arraycopy( buf, 0, DataPage, 0, buf.length); // Copy buf data to DataPage of DataPageSize
		  bos.close();
		 
		  // write to the file	
		  if(link!=0){
			  ListFile.seek((pageCounter)*PAGE_SIZE);
			  ListFile.write(DataPage);
			  ListFile.close();
		  }
		  else{
			  ListFile.seek((info)*PAGE_SIZE);
			  ListFile.write(DataPage);
			  ListFile.close();
		  }
		  
	}
	
}

