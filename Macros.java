
public interface Macros {
	
	int NUMBER_OF_TEXTS = 22; // TODO set automatically
	int LENGTH_OF_LONGEST_TEXT = 254; //TODO set this automatically
	
	// the following are me pretending to use the C preprocessor
	boolean DO_ALIGNMENTS = false;
	boolean MAKE_TREE = false;
	boolean DO_GLOBAL_ALIGNMENT = false;
	boolean TESTING = false;
	boolean CHECK_TIME = true;
	boolean WRITE_ORIGINAL = false;
	boolean ALIGN_ORIGINAL_WITH_ORIGINAL = false;
	boolean FIND_ORIGINAL_TEXT = false;
	
	// the following are filepaths necessary for reading and writing to files
	String ROOT_DIRECTORY = "/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/";
	String VARIANT_LIST_FILE  = ROOT_DIRECTORY + "variantList.txt";
	String ALIGNMENTS_FOLDER = ROOT_DIRECTORY + "Alignments/";
	String ALIGNMENT_TABLE_FILE = ROOT_DIRECTORY + "alignmentTable1.txt";
	String WORD_LENGTHS_FILE = ROOT_DIRECTORY + "word_lengths.csv";
	String GLOBAL_ALIGNMENT_FILE = ROOT_DIRECTORY + "Alignments/globalAlignment.csv";
	String ORIGINAL_GUESS_FILE = ROOT_DIRECTORY + "Alignments/Original1.txt";
	String NAME_OF_ORIGINAL_GUESS_FILE = ROOT_DIRECTORY + "100_copy.txt";
	String NAME_OF_LONGEST_FILE = "/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/Alignments/109_"; // TODO set auto

}
