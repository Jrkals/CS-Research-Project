import java.io.IOException;

// Class for running the whole project
public class mainProject {

	public static void main(String[] args) throws IOException {
		System.out.println("working...");
		long currentTime = System.currentTimeMillis();
		Differ d = new Differ();
		d.makeDiffTable(); //
		int[][] scores = d.getScoreTable(); 
		d.writeAlignmentScoreTable("/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/alignmentTable1.txt");
		d.writeLengths("/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/lengths.txt");
	/*	FileReader f1 = new FileReader("/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/alignmentTable1.txt");
		int[][] scores = f1.get2dArrayOfInts();*/
		
	 /* test data from wikipedia
	 * int[][] scores = {{0,17,21,31,23},{17,0,30,34,21},{21,30,0,28,39},
	 *			{31,34,28,0,43},{23,21,39,43,0}};
	 */
		TreeProducer treeMaker = new TreeProducer(scores);
		treeMaker.makeTree();
		long newTime = System.currentTimeMillis();
		long diffTime = (newTime-currentTime)/1000;
		System.out.println("Finished in "+diffTime+" seconds");
	}

}
