import java.io.IOException;
import java.util.HashMap;

// Class for running the whole project
public class mainProject implements Macros {

	public static void main(String[] args) throws IOException {
		long currentTime = System.currentTimeMillis();
		System.out.println("working...");
		Differ d = new Differ();

		if(TESTING) {
			twoStringAligner tw = new twoStringAligner("notredameisgoodtoo", "notrelamistou");
			twoStringAligner tw1 = new twoStringAligner("am", "Everett");
			tw.doAlignment();
			String filename1 = "/Users/justin/Dropbox/School/CS_Research/wordTest1.txt";
			String filename2 = "/Users/justin/Dropbox/School/CS_Research/wordTest2.txt";
			wordAligner wa = new wordAligner(filename1, filename2);
			wa.doAlignment();
		}
		if(DO_ALIGNMENTS) {
			d.makeDiffTable(); //
			int[][] scores = d.getScoreTable(); 
			d.writeAlignmentScoreTable(ALIGNMENT_TABLE_FILE);
			d.writeWordLengths(WORD_LENGTHS_FILE);
		}
		if(DO_GLOBAL_ALIGNMENT) {
			d.writeGlobalAlingment(GLOBAL_ALIGNMENT_FILE);
		}
		if(MAKE_TREE) {
			FileReader f1 = new FileReader(ALIGNMENT_TABLE_FILE);
			int[][] scores = f1.get2dArrayOfInts();
			/* test data from wikipedia
			 * int[][] scores = {{0,17,21,31,23},{17,0,30,34,21},{21,30,0,28,39},
			 *			{31,34,28,0,43},{23,21,39,43,0}};
			 */
			TreeProducer treeMaker = new TreeProducer(scores);
			treeMaker.makeTree();
		}
		if(FIND_ORIGINAL_TEXT) {
			String filename = GLOBAL_ALIGNMENT_FILE;
			OriginalTextFinder otf = new OriginalTextFinder(filename);
			String[] original = otf.findOriginalText();
			for(String word: original) {
				System.out.print(word+ " ");
			}
			System.out.println();

			if(WRITE_ORIGINAL) {
				String outfile = ORIGINAL_GUESS_FILE;
				otf.writeToFile(outfile);
			}
		}
		
		String varList = VARIANT_LIST_FILE;
		FileReader frv = new FileReader(varList);
		String[] filenames = frv.getFileNamesFromVariantList();
		HashMap<Integer, String> numsForNames = frv.returnIndexedNames();
		
		String filename = NAME_OF_ORIGINAL_GUESS_FILE;
		OriginalTextFinder otf = new OriginalTextFinder();
		String nameOfOriginal = otf.findOriginalManuscript(filename);
		String nameOfOriginalWithoutPath = frv.getFileName(nameOfOriginal);
		int locOriginal = otf.getLocOfOriginal();
		RealTreeMaker rtm = new RealTreeMaker(nameOfOriginalWithoutPath, filenames, numsForNames, locOriginal);
		rtm.makeTree();


		if(ALIGN_ORIGINAL_WITH_ORIGINAL) {
			String file1 = nameOfOriginal;
			String file2 = NAME_OF_ORIGINAL_GUESS_FILE;
			wordAligner wa = new wordAligner(file1, file2);
			wa.doAlignment();
		}

		if(CHECK_TIME) {
			long newTime = System.currentTimeMillis();
			long diffTime = (newTime-currentTime)/1000;
			System.out.println("Finished in "+diffTime+" seconds");
		}
	}

}
