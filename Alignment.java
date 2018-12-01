// when alignmentScore > gapPentalty it seems to work well
public interface Alignment {
	int gapPenalty = -5; // penalty for inserting '_'
	int alignmentScore = 6; // score for matching identical strings/characters
	final int SCORE = -10000; // initial value for scoring matrix
	
	void doAlignment();
	int fillScoringMatrix(int a, int b);
	public void traverseScoreMatrixBackwards();
	public void alignWords();
	public int max(int a, int b);
	public int max(int a, int b, int c);
	public void printAlignmentToFile();
}
