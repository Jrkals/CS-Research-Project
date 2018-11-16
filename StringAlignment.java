import java.util.ArrayList;

public class StringAlignment {
	static char[] string1;// = {'a', 'd', 'b', 'c', 'e'};
	static char[] string2;// = {'a','b','c'};
	static int[][] scoringMatrix; // holds value of alignments
	static String[][] traverseMatrix; // holds order of best alignments
	static ArrayList<Character> string1Corrected = new ArrayList<>(); // aligned strings
	static ArrayList<Character> string2Corrected = new ArrayList<>();
	static int gapPenalty = -2; // penalty for inserting '_'
	static int alignmentScore = 3;
	static final int SCORE = -10000; // initial value for scoring matrix
	static int MAXSIZE; // length of longest string between string1 and string2
	
	public static void main(String[] args) {
		String file1Name = "/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/833_copy.txt";
		String file2Name = "/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/988_copy.txt";
		FileReader fr = new FileReader(file1Name);
		FileReader fr2 = new FileReader(file2Name);
		//	HashMap<Character, Double> freqs1 = fr.calculateFrequencies();
		//	HashMap<Character, Double> freqs2 = fr2.calculateFrequencies();

		string1 = fr.getCharacters();
		string2 = fr2.getCharacters();
		MAXSIZE = max(string1.length, string2.length);

		scoringMatrix = new int[string1.length][string2.length];
		traverseMatrix = new String[string1.length][string2.length];

		// initialize traverse matrix to non 0,1,2 values
		for(int i = 0; i < traverseMatrix.length; i++) {
			for(int j = 0; j < traverseMatrix[i].length; j++) {
				traverseMatrix[i][j] = "X"; // not touched value
				// if it remains -1 later it will not be part of the alignment
			}
		}
		scoringMatrix[0][0] = 1;

		//initialize scoring matrix and fill arrayLists
		for(int i = 0; i < string1.length; i++) {
			string1Corrected.add(string1[i]);
			for(int j = 0; j < string2.length; j++) {
				if(i == 0) { // only do this once 
					string2Corrected.add(string2[j]);
				}
				scoringMatrix[i][j] = SCORE;
			}
		}

		//create matrix of scores
		long time = System.currentTimeMillis();
		fillScoringMatrix(string1.length-1, string2.length-1);
		long time2 = System.currentTimeMillis();
		System.out.println(time2-time+ "ms to run");
		traverseScoreMatrixBackwards();
		alignStrings();

		//print aligned strings when done
		for(int i = 0; i < string1Corrected.size(); i++) {
			System.out.print(string1Corrected.get(i));
		}
		System.out.println();
		for(int i = 0; i < string2Corrected.size(); i++) {
			System.out.print(string2Corrected.get(i));
		}
		System.out.println();
		
		double preGCoherence = findPreGeneologicalCoherence();
		System.out.printf("%.3f", preGCoherence);
		System.out.println("% coherent");
		
		//Print scoring matrix when done
	/*	System.out.println("Scoring Matrix");
		for(int i = 0; i <string1.length; i++) {
			printArray(scoringMatrix[i]);
		}
		System.out.println();
		//Print traverse matrix when done
		System.out.println("Traverse Matrix");
		for(int i = 0; i < string1.length; i++) {
			printArray(traverseMatrix[i]);
		}*/
		System.out.println();
	}

	public static int scoreAlignment(char a, char b) {
		if(a == b) {
			return alignmentScore;
		}
		return 0-alignmentScore;
	}

	//return the max of three numbers
	public static int max(int a, int b, int c) {
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
	public static int max(int a, int b) {
		if(a > b)
			return a;
		return b;
	}

	private static int fillScoringMatrix(int i, int j){
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
			scoringMatrix[i][j] = max(scoreAlignment(string1[i], string2[j]), gapPenalty);
			if(scoreAlignment(string1[i], string2[j]) > gapPenalty) {
				System.out.println("start same");
			}
			return scoringMatrix[i][j];
		}
		// gap in string1 
		else if(i == 0) {
			scoringMatrix[i][j] = fillScoringMatrix(i, j-1) + gapPenalty;
			return scoringMatrix[i][j];
		}
		// gap in string2
		else if(j == 0) {
			scoringMatrix[i][j] = fillScoringMatrix(i-1, j) + gapPenalty;
			return scoringMatrix[i][j];
		}
		// recurse and take the max
		else {
			int a = fillScoringMatrix(i-1, j-1)+ scoreAlignment(string1[i], string2[j]);
			int b = fillScoringMatrix(i, j-1) + gapPenalty;
			int c = fillScoringMatrix(i-1, j) + gapPenalty;
			scoringMatrix[i][j] = max(a,b,c);
			//	System.out.println("max is "+max(a,b,c));
			// a is max do nothing just align chars
			return scoringMatrix[i][j];
		}

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
	
	static void traverseScoreMatrix() {
		//indices
		int i,j;
		i = j = 0;
		while(i < string1.length-1 && j < string2.length-1) {
			int diag = scoringMatrix[i+1][j+1];
			int down = scoringMatrix[i+1][j];
			int right = scoringMatrix[i][j+1];
		//	System.out.println("max is" +max(diag, down, right));
			if(max(diag, down, right) == diag) {
				traverseMatrix[i+1][j+1] = "<-^";
				i++;
				j++;
			}
			else if(max(diag, down, right) == down) {
				traverseMatrix[i+1][j] = "^";
				i++;
			}
			else if(max(diag, down, right) == right) {
				traverseMatrix[i][j+1] = "<-";
				j++;
			}
		}
	}
	
	static void traverseScoreMatrixBackwards() {
		//indices
		int i,j;
		i = string1.length-1;
		j = string2.length-1;
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
	static void alignStrings() {
		// start at string length, when insert '_' increase by one
		int iteratorI = 1; // each time a '_' is inserted this inc by 1
		int iteratorJ = 1; // ''
		for(int i = 0; i < string1.length; i++) {
			for(int j = 0; j < string2.length; j++) {
				if(!traverseMatrix[i][j].equals("X")) {
					//check one of the two change markers (insert a '_'
					//gap in string1
					if(traverseMatrix[i][j].equals("<-")) {
						string1Corrected.add(i + iteratorI, '_');
						iteratorI++;
					}
					//gap in string2
					else if(traverseMatrix[i][j].equals("^")) {
						string2Corrected.add(j + iteratorJ, '_');
						iteratorJ++;
					}
				}
			}
		}
	}
	
	private static double findPreGeneologicalCoherence() {
		double numDiff = 0.0;
		for(int i = 0; i < string1Corrected.size(); i++) {
			// if chars are diff
			if(string1Corrected.get(i) != string2Corrected.get(i)) {
				numDiff++;
			}
		}
		
		return 100.0-numDiff/string1Corrected.size();
		
	}
}
