import java.util.ArrayList;

public class twoStringAligner implements Alignment {
	ArrayList<Character> word1Corrected = new ArrayList<>();
	ArrayList<Character> word2Corrected = new ArrayList<>();
	int[][] scoringMatrix;
	String[][] traverseMatrix;

	String word1;
	String word2;

	public twoStringAligner(String w1, String w2) {
		//System.out.println("aligning "+w1+" and "+w2);
		word1 = w1;
		word2 = w2;
		scoringMatrix = new int[word1.length()][word2.length()];
		traverseMatrix = new String[word1.length()][word2.length()];

		// initialize traverse matrix to non 0,1,2 values
		for(int i = 0; i < traverseMatrix.length; i++) {
			for(int j = 0; j < traverseMatrix[i].length; j++) {
				traverseMatrix[i][j] = "X"; // not touched value
				// if it remains -1 later it will not be part of the alignment
			}
		}
		//initialize scoring matrix and fill arrayLists
		for(int i = 0; i < word1.length(); i++) {
			word1Corrected.add(word1.charAt(i));
			for(int j = 0; j < word2.length(); j++) {
				if(i == 0) { // only do this once 
					word2Corrected.add(word2.charAt(j));
				}
				scoringMatrix[i][j] = SCORE;
			}
		}
	}

	public void doAlignment() {
		//create matrix of scores
		fillScoringMatrix(word1.length()-1, word2.length()-1);
		traverseScoreMatrixBackwards();
		alignWords();
	//	printAlignment();
	}

	public int fillScoringMatrix(int i, int j){
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
			scoringMatrix[i][j] = max(scoreAlignment(word1.charAt(i), word2.charAt(j)), gapPenalty);
			if(scoreAlignment(word1.charAt(i), word2.charAt(j)) > gapPenalty) {
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
			int a = fillScoringMatrix(i-1, j-1)+ scoreAlignment(word1.charAt(i), word2.charAt(j));
			int b = fillScoringMatrix(i, j-1) + gapPenalty;
			int c = fillScoringMatrix(i-1, j) + gapPenalty;
			scoringMatrix[i][j] = max(a,b,c);
			//	System.out.println("max is "+max(a,b,c));
			// a is max do nothing just align chars
			return scoringMatrix[i][j];
		}

	}

	public void traverseScoreMatrixBackwards() {
		//indices
		int i,j;
		i = word1.length()-1;
		j = word2.length()-1;
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
	// use traverse matrix to make the appropriate alignments
	public void alignWords() {
		// start at string length, when insert '_' increase by one
		int iteratorI = 1; // each time a '_' is inserted this inc by 1
		int iteratorJ = 1; // ''
		for(int i = 0; i < word1.length(); i++) {
			for(int j = 0; j < word2.length(); j++) {
				if(!traverseMatrix[i][j].equals("X")) {
					//check one of the two change markers (insert a '_'
					//gap in string1
					if(traverseMatrix[i][j].equals("<-")) {
						word1Corrected.add(i + iteratorI, '_');
						iteratorI++;
					}
					//gap in string2
					else if(traverseMatrix[i][j].equals("^")) {
						word2Corrected.add(j + iteratorJ, '_');
						iteratorJ++;
					}
				}
			}
		}
	}

	public int scoreAlignment(char a, char b) {
		if(a == b) {
			return alignmentScore;
		}
		return 0-alignmentScore;
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

	public void printAlignment() {
		for(int i = 0; i <word1Corrected.size(); i++) {
			System.out.print(word1Corrected.get(i));
		}
		System.out.println();
		for(int i = 0; i <word2Corrected.size(); i++) {
			System.out.print(word2Corrected.get(i));
		}
		System.out.println();
	}

	public void printMatrices() {
		print2DArray(traverseMatrix);
		print2DArray(scoringMatrix);
	}

	public String getWord1Aligned() {
		String rv = "";
		for(Character c: word1Corrected) {
			rv += c;
		}
		return rv;
	}

	public String getWord2Aligned() {
		String rv = "";
		for(Character c: word2Corrected) {
			rv += c;
		}
		return rv;
	}

	// print a one dimensional int array
	void printArray(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+ "\t ");
		}
		System.out.println();
	}

	// print a one dimensional string array
	void printArray(String[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+ "\t ");
		}
		System.out.println();
	}

	void print2DArray(String[][] a) {
		for(int i = 0; i < a.length; i++) {
			printArray(a[i]);
		}
	}

	void print2DArray(int[][] a) {
		for(int i = 0; i < a.length; i++) {
			printArray(a[i]);
		}
	}
	
	public void printAlignmentToFile() {
		System.out.println("TODO");
	}

}
