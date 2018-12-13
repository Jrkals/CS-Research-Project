import java.util.ArrayList;

/*
 * Class to take two individual words (no spaces)
 * and align them. Works like the other alignment classes
 */
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
			scoringMatrix[i][j] = Utilities.max(scoreAlignment(word1.charAt(i), word2.charAt(j)), gapPenalty);
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
			scoringMatrix[i][j] = Utilities.max(a,b,c);
			//	System.out.println("max is "+max(a,b,c));
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
		Utilities.print2DArray(traverseMatrix);
		Utilities.print2DArray(scoringMatrix);
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
	
	public void printAlignmentToFile() {
		System.out.println("TODO");
	}

}
