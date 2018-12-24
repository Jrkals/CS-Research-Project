import java.io.FileWriter;
import java.io.IOException;
/*
 * A class which compares all of the texts together and writes tables of their 
 * similarity by alignment score. 
 */
public class Differ implements Macros {
	double[][] coherenceTable;
	int[][] alignmentScores;
	String[] files;
	String[] fileNames; // {"109","140","167","200","211","253","299","310",
			//"317","321","427","433","486","535","587","629","667", "702","772","816","833","988"};
	String longestFileName = "";
	static int numberOfFiles;
	static int lengthOfLongestFile; // word length not char length
	/*
	 * Read texts from Variant list, get their names and
	 * initialize the alignment score and coherence tables
	 */
	public Differ(String variantList) {
		FileReader frVarList = new FileReader(variantList);
		files = frVarList.getWordsNoSkip(); // list of manuscripts
		numberOfFiles = files.length;
		fileNames = new String[numberOfFiles];
		// fill fileNames with the number part of each file (ignore the path)
		int k = 0;
		for(String file: files) {
			fileNames[k] = frVarList.getFileName(file);
			k++;
		}
		System.out.println("number of files is "+files.length);
		// initialize arrays of scores
		coherenceTable = new double[files.length][files.length];
		alignmentScores = new int[files.length][files.length];
		for(int i = 0; i < files.length; i++) {
			for(int j = 0; j < files.length; j++) {
				// aligning with self means 4000 distance and 100% coherence
				if(i == j) {
					alignmentScores[i][j] = 4000;
					coherenceTable[i][j] = 100.0;
				}
				else {
					coherenceTable[i][j] = -1.0;
					alignmentScores[i][j] = -1;
				}
			}
		}
	}
	// Do all of the alignments and enter the scores into the tables
	public double [][] makeDiffTable() {
		int numManuscripts = files.length;
		int numberOfIdenticals = 0;
		for(int i = 0; i < numManuscripts; i++) {
			String file1Name = files[i];
			for(int j = 0; j < numManuscripts; j++) {
				// if already done the work here, go to next one
				if(coherenceTable[i][j] != -1.0) {
					continue;
				}
				String file2Name = files[j];
				// see if they are identical
				if(areIdentical(file1Name, file2Name)) {
					numberOfIdenticals++;
				}
			//	newAlignment aligner = new newAlignment(file1Name, file2Name); // char alignment
				wordAligner aligner = new wordAligner(file1Name, file2Name); // word alignment
				aligner.doAlignment();
				//Coherence
				double preGCoh = aligner.findPreGeneologicalCoherence();
				coherenceTable[i][j] = preGCoh;
				coherenceTable[j][i] = preGCoh;
				//Alignment
				int score = aligner.getAlignmentScore();
				alignmentScores[i][j] = score;
				alignmentScores[j][i] = score;
			}
		}
		System.out.println(numberOfIdenticals+" files are identical");
		return coherenceTable;
	}

	// enter the alignmentScores into the coherence Table
	public int [][] getScoreTable() {
		return alignmentScores;
	}

	//Writes the table of pre geneological coherences to a file
	public void writeCoherenceTable(String fileName) throws IOException {
		FileWriter frw = new FileWriter(fileName);
		for(int i = 0; i < files.length; i++) {
			for(int j = 0; j < files.length; j++) {
				String numAsString = coherenceTable[i][j] + "";
				if(numAsString.length() > 6) {
					String firstThreeDecimals = numAsString.substring(0, 6);
					frw.write(firstThreeDecimals + ",\t");
				}
			}
			frw.write("\n");
		}
		frw.close();
	}

	/*
	 * Writes the alignment scores of all texts with each other to a file of the 
	 * given name
	 */
	public void writeAlignmentScoreTable(String filename) throws IOException{
		FileWriter frw = new FileWriter(filename);
		for(int i = 0; i < files.length; i++) {
			for(int j = 0; j < files.length; j++) {
				String numAsString = alignmentScores[i][j] + "";
				frw.write(numAsString + ",\t");
			}
			frw.write("\n");
		}
		frw.close();
	}
	
	
	//Write the character lengths of the files to a file
	public void writeLengths(String filename) throws IOException{
		FileWriter frw = new FileWriter(filename);
		for(int i = 0; i < files.length; i++) {
			FileReader f = new FileReader(files[i]);
			f.getCharacters();
			int length = f.getSizeOfChars();
			frw.write("Text"+f.getFileName() + "," + length + "\n");
		}
		frw.close();
	}
	
	//Write the word lengths of the files to a file and find the longest
	public void writeWordLengths(String filename) throws IOException{
		FileWriter frw = new FileWriter(filename);
		frw.write("Text, length\n");
		int max = -1; // max word length
		for(int i = 0; i < files.length; i++) {
			FileReader f = new FileReader(files[i]);
			f.getWords();
			int length = f.getSizeOfWords();
			if(length > max) {
				max = length;
				longestFileName = files[i];
			}
			frw.write("Text"+f.getFileName() + "," + length + "\n");
		}
		lengthOfLongestFile = max;
		frw.close();
	}
	
	// check whether two files are identical
	private boolean areIdentical(String fileName1, String fileName2) {
		FileReader f1 = new FileReader(fileName1);
		FileReader f2 = new FileReader(fileName2);
		char [] f1chars = f1.getCharacters();
		char [] f2chars = f2.getCharacters();
		if(f1chars.length != f2chars.length) {
			return false;
		}
		else {
		//	System.out.println(f1.getFileName()+ " and "+f2.getFileName()+ " are the same length");
			for(int i = 0; i < f1chars.length; i++) {
				if(f1chars[i] != f2chars[i]) {
		//			System.out.println("difference at "+i+" "+f1chars[i]+" vs. "+f2chars[i]);
					return false;
				}
			}
		}
		//else true
		System.out.println(f1.getFileName()+ " and "+f2.getFileName()+ " are the same!");
		return true;
	}
	
	/*
	 * write file which stacks all of the alignments of the files
	 * with the longest file
	 */
	public void writeGlobalAlingment(String filename) throws IOException {
		String longestFile = getLongest();
		String[][] globalAlignment = new String[numberOfFiles][lengthOfLongestFile];
		FileWriter fwr = new FileWriter(filename);
		String filereaderName = longestFile; // alignment file
		//TODO fix the 140 issue and make it generic
		AlignmentReader ar1 = new AlignmentReader(filereaderName+"140.csv"); // 109_140.csv
		globalAlignment[0] = ar1.getWordsFirstAlignment(); // get 109 only
		// change back to the original
		filereaderName = longestFile;
		// get the alignments
		for(int i = 1; i < numberOfFiles; i++) {
			filereaderName += fileNames[i] + ".csv"; // add the number next to 109_ e.g., 109_140.csv
			AlignmentReader ar = new AlignmentReader(filereaderName);
			globalAlignment[i] = ar.getWordsSkipFirst();
			// change back to the original
			filereaderName = longestFile;
		}
		// write the alignments
		for(int i = 0; i < numberOfFiles; i++) {
			fwr.write(fileNames[i]+",");
			for(int j = 0; j < lengthOfLongestFile; j++) {
				fwr.write(globalAlignment[i][j] + ",");
			}
			fwr.write("\n");
		}
		fwr.close();
	}
	
	/*
	 * return the path of the longest file
	 */
	private String getLongest() {
		FileReader fr = new FileReader(longestFileName);
		String name = fr.getFileName(longestFileName)+ "_";
		// below yields path.../Alignments/###
		return longestFileName.substring(0, longestFileName.length()-12) + "Alignments/"+ name;
	}
}
