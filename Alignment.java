/*
 * Interface for alingment class which names common methods and 
 * holds a few common int values for the scores
 */

public interface Alignment {
	// when alignmentScore > gapPentalty it seems to work well
	int gapPenalty = -5; // penalty for inserting '_'
	int alignmentScore = 6; // score for matching identical strings/characters
	final int SCORE = -10000; // initial value for scoring matrix
	
	void doAlignment();
	int fillScoringMatrix(int a, int b);
	public void traverseScoreMatrixBackwards();
	public void alignWords();
	public void printAlignmentToFile();
}
