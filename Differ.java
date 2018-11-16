import java.io.FileWriter;
import java.io.IOException;

public class Differ {
	double[][] coherenceTable;
	int[][] alignmentScores;
	String[] files;
	// default constructor
	public Differ() {
		FileReader frVarList = new FileReader("/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/variantList.txt");
		files = frVarList.getWordsNoSkip(); // list of manuscripts
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
	// enter the pre G coherences into the coherence Table
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
				newAlignment aligner = new newAlignment(file1Name, file2Name);
				aligner.doAlignment();
				//Coherence
				double preGCoh = newAlignment.findPreGeneologicalCoherence();
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
	
	
	//Write the word lengths of the file to a fi
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
			System.out.println(f1.getFileName()+ " and "+f2.getFileName()+ " are the same length");
			for(int i = 0; i < f1chars.length; i++) {
				if(f1chars[i] != f2chars[i]) {
					System.out.println("difference at "+i+" "+f1chars[i]+" vs. "+f2chars[i]);
					return false;
				}
			}
		}
		//else true
		System.out.println(f1.getFileName()+ " and "+f2.getFileName()+ " are the same!");
		return true;
	}
}
