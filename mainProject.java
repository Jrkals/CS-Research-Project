import java.io.IOException;
import java.util.HashMap;

// Class for running the whole project
public class mainProject implements Macros {
	static String nameOfOriginalFile;
	
	public static void main(String[] args) throws IOException {
		long currentTime = System.currentTimeMillis();
		System.out.println("working...");
		Differ d = new Differ(VARIANT_LIST_FILE);
		if(DO_ALIGNMENTS) {
			System.out.println("doing alignments...");
			d.makeDiffTable(); //
			d.writeAlignmentScoreTable(ALIGNMENT_TABLE_FILE);
			d.writeWordLengths(WORD_LENGTHS_FILE);
		}
		
		if(DO_GLOBAL_ALIGNMENT) {
			System.out.println("writing global alignment...");
			d.writeGlobalAlingment(GLOBAL_ALIGNMENT_FILE);
		}
		
		if(MAKE_UPGMA_TREE) {
			System.out.println("making UPGMA tree...");
			Differ d2 = new Differ(UPGMA_VARIANT_LIST);
			d2.makeDiffTable();
			d2.writeAlignmentScoreTable(UPGMA_ALIGNMENTS_FILE);
			// get scores from alignment file
			FileReader f1 = new FileReader(UPGMA_ALIGNMENTS_FILE);
			int[][] scores = f1.get2dArrayOfInts();
			// make tree
			FileReader f2 = new FileReader(UPGMA_VARIANT_LIST);
			String[] filenames = f2.getFileNamesFromVariantList();
			TreeProducer treeMaker = new TreeProducer(scores, filenames);
			treeMaker.makeTree();
		}
		
		if(FIND_ORIGINAL_TEXT) {
			System.out.println("guessing the Original Text...");
			OriginalTextFinder otf = new OriginalTextFinder(GLOBAL_ALIGNMENT_FILE);
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
		
		if(MAKE_ACTUAL_TREE) {
			System.out.println("making tree of descent and finding the original...");
			String varList = VARIANT_LIST_FILE;
			FileReader frv = new FileReader(varList);
			String[] filenames = frv.getFileNamesFromVariantList();
			HashMap<Integer, String> numsForNames = frv.returnIndexedNames();

			String filename = NAME_OF_ORIGINAL_GUESS_FILE;
			OriginalTextFinder otf = new OriginalTextFinder();
			nameOfOriginalFile = otf.findOriginalManuscript(filename);
			String nameOfOriginalWithoutPath = frv.getFileName(nameOfOriginalFile);
			int locOriginal = otf.getLocOfOriginal();
			RealTreeMaker rtm = new RealTreeMaker(nameOfOriginalWithoutPath, filenames, numsForNames, locOriginal);
			rtm.makeTree();
			//rtm.printTree();
			System.out.println();
		}
		
		if(ALIGN_ORIGINAL_WITH_ORIGINAL) {
			System.out.println("aligning the suppossed original with the actual original...");
			String file1 = nameOfOriginalFile;
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
