import java.util.ArrayList;

public class wordAligner {
	String[] words1;
	String[] words2;
	
	ArrayList<String> words1Corrected = new ArrayList<String>();
	ArrayList<String> words2Corrected = new ArrayList<String>();
	int[][] scoringMatrix;
	String[][] traverseMatrix;
	int gapPenalty = -5; // penalty for inserting '_'
	int alignmentScore = 3; // score for matching identical strings
	int similarScore = 1; // score for matching similar strings
	final int SCORE = -10000; // initial value for scoring matrix

	public wordAligner(String filename1, String filename2) {
		FileReader fr = new FileReader(filename1);
		FileReader fr2 = new FileReader(filename2);

		words1 = fr.getWords();
		words2 = fr2.getWords();

		scoringMatrix = new int[words1.length][words2.length];
		traverseMatrix = new String[words1.length][words2.length];

		// initialize traverse matrix to non 0,1,2 values
		for(int i = 0; i < traverseMatrix.length; i++) {
			for(int j = 0; j < traverseMatrix[i].length; j++) {
				traverseMatrix[i][j] = "X"; // not touched value
				// if it remains -1 later it will not be part of the alignment
			}
		}
		//initialize scoring matrix and fill arrayLists
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
		printAlignment();
	}
	
	private int fillScoringMatrix(int i, int j){
		//	System.out.println("i is "+i+ " and j is "+j);
		//	System.out.println("scoring matrix at i,j is "+scoringMatrix[i][j]);

		// Check if seen before
		if(scoringMatrix[i][j] != SCORE) {
			return scoringMatrix[i][j];
		}
		//Base cases
		// either align or put a gap
		if(i == 0 && j == 0) {
			//	align first two or put a gap to start
			scoringMatrix[i][j] = max(scoreAlignment(words1[i], words2[j]), gapPenalty);
			if(scoreAlignment(words1[i], words2[j]) > gapPenalty) {
				//	System.out.println("start same");
			}
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
			scoringMatrix[i][j] = max(a,b,c);
			//	System.out.println("max is "+max(a,b,c));
			// a is max do nothing just align chars
			return scoringMatrix[i][j];
		}

	}
	
	void traverseScoreMatrixBackwards() {
		//indices
		int i,j;
		i = words1.length-1;
		j = words2.length-1;
		while(i >= 1 && j >= 1) {
			int diag = scoringMatrix[i-1][j-1];
			int up = scoringMatrix[i-1][j];
			int left = scoringMatrix[i][j-1];
			//	System.out.println("max is" +max(diag, up, left));
			if(max(diag, up, left) == diag) {
				traverseMatrix[i][j] = "<-^";
				i--;
				j--;
			}
			else if(max(diag, up, left) == up) {
				traverseMatrix[i][j] = "^";
				i--;
			}
			else if(max(diag, up, left) == left) {
				traverseMatrix[i][j] = "<-";
				j--;
			}
		}
	}
	// use traverse matrix to make the appropriate alignments
	void alignWords() {
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
	
	public int scoreAlignment(String a, String b) {
		if(a.equals(b)) {
			return alignmentScore;
		}
		else if(isSimilar(a, b)) {
			return similarScore;
		}
		return 0-alignmentScore;
	}

	// returns whether two strings are similar
	// if > 50% of their characters match after the strings 
	// have been aligned they are similar.
	private boolean isSimilar(String a, String b) {
		double numMisses = 0.0; // number of places in string where chars don't match
		double max = (double)max(a.length(), b.length()); // length of the longest string
		// TODO align the strings before iterating through them
		for(int i = 0; i < max; i++) {
			if(a.charAt(i) != b.charAt(i)) {
				numMisses++;
			}
		}
		// > 50% of chars are the same
		if(numMisses/max >= .5) {
			return true;
		}
		return false;
	}

	//return the max of three numbers
	public int max(int a, int b, int c) {
		if(a > b && a > c) 
			return a;
		if(b > a && b > c) 
			return b;
		else {
			// check for possible tie
			if(c == a || c == b) {

			}
			return c;
		}
	}

	// return the max of two numbers
	public int max(int a, int b) {
		if(a > b)
			return a;
		return b;
	}
	
	// print a one dimensional int array
	static void printArray(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+ "\t ");
		}
		System.out.println();
	}
	// print a one dimensional string array
	static void printArray(String[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+ "\t ");
		}
		System.out.println();
	}
	
	void printAlignment() {
		for(int i = 0; i <words1Corrected.size(); i++) {
			System.out.println(words1Corrected.get(i));
		}
		for(int i = 0; i <words2Corrected.size(); i++) {
			System.out.println(words2Corrected.get(i));
		}
	}

}
