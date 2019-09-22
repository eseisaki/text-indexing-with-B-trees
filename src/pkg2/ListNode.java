package pkg2;

public class ListNode {

		String fileName;
		int linebytes;
		ListNode continuePage;
		
		public ListNode(String fileName,int linebytes){
			if(fileName.length()>8)
				this.fileName=fileName.substring(0, 7);
			else
				this.fileName=fileName;
		
			this.linebytes = linebytes;
			continuePage=null;
		}

		public final String getFileName() {
			return fileName;
		}

		public final void setFileName(String fileName) {
			if(fileName.length()>8)
				this.fileName=fileName.substring(0, 7);
			else
				this.fileName=fileName;
		}

		public final int getLinebytes() {
			return linebytes;
		}

		public final void setLinebytes(int linebytes) {
			this.linebytes = linebytes;
		}

		public final ListNode getContinuePage() {
			return continuePage;
		}

		public final void setContinuePage(ListNode continuePage) {
			this.continuePage = continuePage;
		}
		
		
}
