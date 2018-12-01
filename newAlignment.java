import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class newAlignment implements Alignment {
	char[] string1;// = {'a', 'd', 'b', 'c', 'e'};
	char[] string2;// = {'a','b','c'};
	int[][] scoringMatrix; // holds value of alignments
	String[][] traverseMatrix; // holds order of best alignments
	ArrayList<Character> string1Corrected = new ArrayList<>(); // aligned strings
	ArrayList<Character> string2Corrected = new ArrayList<>();
	int MAXSIZE; // length of longest string between string1 and string2
	String filename1 = "";
	String filename2 = "";

	public newAlignment(String file1Name, String file2Name) {

		//	System.out.println("file1 is "+filename1+ "\nfile 2 is "+filename2);
		FileReader fr = new FileReader(file1Name);
		FileReader fr2 = new FileReader(file2Name);

		/*	filename1 = file1Name.substring(64, 72); // just the XXX_copy.txt part of the string
		filename2 = file2Name.substring(64, 76);*/
		filename1 = fr.getFileName();
		filename2 = fr.getFileName();

		string1 = fr.getCharacters();
		string2 = fr2.getCharacters();
		MAXSIZE = max(string1.length, string2.length);
		//	System.out.println("max is "+MAXSIZE);

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
	}

	public void doAlignment() {
		//create matrix of scores
		fillScoringMatrix(string1.length-1, string2.length-1);
		traverseScoreMatrixBackwards();
		alignWords();
		printAlignmentToFile();
		//	double preGCoherence = findPreGeneologicalCoherence();
		/*	System.out.printf("%.3f", preGCoherence);
		System.out.println("% coherent");
		System.out.println();*/
	}

	//Write's alignment out to a file
	public void printAlignmentToFile() {
		//Write scrambled words to a file
		String outputFileString = "/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/Alignments/";
		outputFileString += filename1 + filename2;
		//	System.out.println(outputFileString);
		File outputFile = new File(outputFileString);
		try {
			FileWriter fwr = new FileWriter(outputFile);
			//print aligned strings when done
			for(int i = 0; i < string1Corrected.size(); i++) {
				fwr.write(string1Corrected.get(i));
			}
			fwr.write("\n");
			fwr.write("TEXT 2*******************************************\n");
			for(int i = 0; i < string2Corrected.size(); i++) {
				fwr.write(string2Corrected.get(i));
			}
			fwr.write("\n");
			fwr.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("could not find output file");
		}

	}

	public static int scoreAlignment(char a, char b) {
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
			scoringMatrix[i][j] = max(scoreAlignment(string1[i], string2[j]), gapPenalty);
			if(scoreAlignment(string1[i], string2[j]) > gapPenalty) {
				//	System.out.println("start same");
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

	public void traverseScoreMatrix() {
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

	public void traverseScoreMatrixBackwards() {
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
	public void alignWords() {
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

	// return the bottm right score
	// this is the score of the alignment of the two strings
	public int getAlignmentScore() {
		return scoringMatrix[scoringMatrix.length-1][scoringMatrix[0].length-1];
	}

	public double findPreGeneologicalCoherence() {
		double numDiff = 0.0;
		for(int i = 0; i < string1Corrected.size(); i++) {
			// if chars are diff
			if(string1Corrected.get(i) != string2Corrected.get(i)) {
				numDiff++;
			}
		}
		return 100.0-numDiff/string1Corrected.size();

	}
	/*
	 * the Algorithm yields some instances where an alignment will yield
	 * a char a then a _ then the rest of the _____ because the char c after the
	 * ______c matches the first char a in other words: _a______a where it should be
	 * (some char)a________. Thus some post processing needs to be done to fix this
	 */

	/*	private void postProcess() {
		// one loop over both corrected strings since they should be the same size
		for(int i = 0; i < string1Corrected.size(); i++) {

		}
	}*/
}

