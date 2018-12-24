import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/*
 * A class that takes two texts and aligns them
 * The texts are sets of strings.
 * Works the same as the other aligner classes
 */

public class wordAligner implements Alignment, Macros {
	String[] words1; // initial array of the text being aligned
	String[] words2; // initial array of the text being aligned
	
	ArrayList<String> words1Corrected = new ArrayList<String>();
	ArrayList<String> words2Corrected = new ArrayList<String>();
	int[][] scoringMatrix;
	String[][] traverseMatrix;
	int similarScore = 1; // score for matching similar strings
	String filename1;
	String filename2;

	public wordAligner(String file1, String file2) {
		FileReader fr = new FileReader(file1);
		FileReader fr2 = new FileReader(file2);
		filename1 = fr.getFileName()+"_";
		filename2 = fr2.getFileName()+".csv"; // save as comma separated value files for excel

		// fill the arrays with the words from the file
		words1 = fr.getWords(); 
		words2 = fr2.getWords();

		scoringMatrix = new int[words1.length][words2.length];
		traverseMatrix = new String[words1.length][words2.length];

		// initialize traverse matrix to all X's
		for(int i = 0; i < traverseMatrix.length; i++) {
			for(int j = 0; j < traverseMatrix[i].length; j++) {
				traverseMatrix[i][j] = "X"; // not touched value
			}
		}
		//fill arrayLists
		for(int i = 0; i < words1.length; i++) {
			words1Corrected.add(words1[i]);
			for(int j = 0; j < words2.length; j++) {
				if(i == 0) { // only do this once 
					words2Corrected.add(words2[j]);
				}
				scoringMatrix[i][j] = SCORE;
			}
		}
	}
	
	public void doAlignment() {
		//create matrix of scores
		fillScoringMatrix(words1.length-1, words2.length-1);
		traverseScoreMatrixBackwards();
		alignWords();
	//	System.out.println("******************");
		printAlignmentToFile();
	}
	/*
	 * used for J Unit tests
	 */
	public void doAlignmentNoFileOutput() {
		//create matrix of scores
		fillScoringMatrix(words1.length-1, words2.length-1);
		traverseScoreMatrixBackwards();
		alignWords();
		printAlignmentToScreen();
	}
	
	/*
	 * (non-Javadoc)
	 * @see Alignment#fillScoringMatrix(int, int)
	 * Dynamic Programming Algorithm to align two strings
	 * Takes two integers representing indeces in an array and finds the best 
	 * possible alignment at any given spot
	 */
	public int fillScoringMatrix(int i, int j){
		//	System.out.println("i is "+i+ " and j is "+j);
		//	System.out.println("scoring matrix at i,j is "+scoringMatrix[i][j]);

		// Check if seen before
		if(scoringMatrix[i][j] != SCORE) {
			return scoringMatrix[i][j];
		}
		//Base cases
		// either align or put a gap to start
		if(i == 0 && j == 0) {
			scoringMatrix[i][j] = Utilities.max(scoreAlignment(words1[i], words2[j]), gapPenalty);
			return scoringMatrix[i][j];
		}
		// gap in word1 
		else if(i == 0) {
			scoringMatrix[i][j] = fillScoringMatrix(i, j-1) + gapPenalty;
			return scoringMatrix[i][j];
		}
		// gap in word2
		else if(j == 0) {
			scoringMatrix[i][j] = fillScoringMatrix(i-1, j) + gapPenalty;
			return scoringMatrix[i][j];
		}
		// recurse and take the max
		else {
			int a = fillScoringMatrix(i-1, j-1)+ scoreAlignment(words1[i], words2[j]);
			int b = fillScoringMatrix(i, j-1) + gapPenalty;
			int c = fillScoringMatrix(i-1, j) + gapPenalty;
			scoringMatrix[i][j] = Utilities.max(a,b,c);
			//	System.out.println("max is "+max(a,b,c));
			
			return scoringMatrix[i][j];
		}

	}
	/*
	 * (non-Javadoc)
	 * @see Alignment#traverseScoreMatrixBackwards()
	 * Fills in the traverse matrix with arrays pointing to the correct path 
	 * of alignment based upon the scoringMatrix score.
	 */
	public void traverseScoreMatrixBackwards() {
		int i,j; //indices
		i = words1.length-1;
		j = words2.length-1;
		while(i >= 1 && j >= 1) {
			int diag = scoringMatrix[i-1][j-1];
			int up = scoringMatrix[i-1][j];
			int left = scoringMatrix[i][j-1];
			//	System.out.println("max is" +max(diag, up, left));
			if(Utilities.max(diag, up, left) == diag) {
				traverseMatrix[i][j] = "<-^";
				i--;
				j--;
			}
			else if(Utilities.max(diag, up, left) == up) {
				traverseMatrix[i][j] = "^";
				i--;
			}
			else if(Utilities.max(diag, up, left) == left) {
				traverseMatrix[i][j] = "<-";
				j--;
			}
		}
		// Have reached top row or left side at this point or both
		//Go up
		if(j == 0) {
			while(i > 0) {
				traverseMatrix[i][j] = "^";
				i--;
			}
		}
		//Go left
		else if(i == 0) {
			while(j > 0) {
				traverseMatrix[i][j] = "<-";
				j--;
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * @see Alignment#alignWords()
	 * Go through the traverse matrix using the arrows it contains and
	 * make the necessary insertions and alignments to the alignment strings
	 */
	public void alignWords() {
		// start at string length, when insert '_' increase by one
		int iteratorI = 1; // each time a '_' is inserted this inc by 1
		int iteratorJ = 1; // ''
		for(int i = 0; i < words1.length; i++) {
			for(int j = 0; j < words2.length; j++) {
				if(!traverseMatrix[i][j].equals("X")) {
					//check one of the two change markers (insert a '_'
					//gap in string1
					if(traverseMatrix[i][j].equals("<-")) {
						words1Corrected.add(i + iteratorI, "_");
						iteratorI++;
					}
					//gap in string2
					else if(traverseMatrix[i][j].equals("^")) {
						words2Corrected.add(j + iteratorJ, "_");
						iteratorJ++;
					}
				}
			}
		}
	}
	/*
	 * Takes two strings and returns a score, one if they are identical-- the highest score. 
	 * one if they are similar (positive but less than the identical score)
	 * and one if they are different (the negative of the identical score).
	 */
	public int scoreAlignment(String a, String b) {
		//System.out.println(a+" and "+b);
		if(a.equals(b)) {
			return alignmentScore;
		}
		else if(isSimilar(a, b)) {
		//	System.out.println(a+" and "+b +" are similar");
			return similarScore;
		}
		return 0-alignmentScore;
	}

	/* returns whether two strings are similar
	* if > 50% of their characters match after the strings 
	* have been aligned they are similar.
	* */
	private boolean isSimilar(String a, String b) {
		double numMisses = 0.0; // number of places in string where chars don't match
		
		// not sure why but sometimes and "" gets in
		// throw this away
		if(a.equals("") || b.equals("")) {
			return false;
		}
		
		twoStringAligner tw = new twoStringAligner(a, b);
		tw.doAlignment();
		String newa = tw.getWord1Aligned();
		String newb = tw.getWord2Aligned();
		// this should never happen
		if(newa.length() != newb.length()) {
			System.out.println("error diff lenghts:");
			System.out.println("newa is "+newa.length()+"newb is "+newb.length());
		}
		double max = (double)Utilities.max(newa.length(), newb.length()); // length of the longest string
		
		for(int i = 0; i < max; i++) {
		//	System.out.println(newa.charAt(i)+" "+newb.charAt(i));
			if(newa.charAt(i) != newb.charAt(i)) {
				numMisses++;
			}
		}
		// if > 50% of chars are different they are not similar
		if(numMisses/max >= .5) {
			return false;
		}
		return true;
	}

	/*
	 * prints the alignment out to the screen
	 */
	public void printAlignmentToScreen() {
		for(int i = 0; i <words1Corrected.size(); i++) {
			System.out.print(words1Corrected.get(i) + "\t\t");
			System.out.print(words2Corrected.get(i));
			System.out.println();
		}
		System.out.println();
	}
	
	/* Write's alignment out to a file
	 * (non-Javadoc)
	 * @see Alignment#printAlignmentToFile()
	 * Prints the aligned words out to the specified file in the Alignments folder
	 */
		public void printAlignmentToFile() {
			//Write scrambled words to a file
			String outputFileString = ALIGNMENTS_FOLDER;
			outputFileString += filename1 + filename2;
			int totalDifferences = 0;
		//	System.out.println(outputFileString);
			File outputFile = new File(outputFileString);
			try {
				FileWriter fwr = new FileWriter(outputFile);
				// write the names of the texts at the top
				fwr.write(filename1+",");
				fwr.write(filename2+"\n");
				//print aligned strings when done
				for(int i = 0; i < words1Corrected.size(); i++) {
					fwr.write(words1Corrected.get(i)+ ",");
					fwr.write(words2Corrected.get(i));
					if(!(words1Corrected.get(i).equals(words2Corrected.get(i)))) {
						fwr.write(",different");
						totalDifferences++;
					}
					fwr.write("\n");
				}
				fwr.write("Total Diffs,");
				fwr.write(totalDifferences+ "\n");
				fwr.write("**********END***********");
				fwr.close();

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("could not find output file");
			}

		}
	
	// return the bottom right score
	// this is the score of the alignment of the two strings
	public int getAlignmentScore() {
		return scoringMatrix[scoringMatrix.length-1][scoringMatrix[0].length-1];
	}
	/*
	 * Returns the percent of characters which are the same between two strings
	 */
	public double findPreGeneologicalCoherence() {
		double numDiff = 0.0;
		for(int i = 0; i < words1Corrected.size(); i++) {
			// if chars are diff
			if(!words1Corrected.get(i).equals(words2Corrected.get(i))) {
				numDiff++;
			}
		}
		return 100.0-numDiff/words1Corrected.size();
	}
}
