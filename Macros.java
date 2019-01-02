
public interface Macros {
	
	int NUMBER_OF_TEXTS = 22;
	
	// the following are me pretending to use the C preprocessor
	boolean DO_ALIGNMENTS = true;
	boolean MAKE_UPGMA_TREE = false;
	boolean DO_GLOBAL_ALIGNMENT = true;
	boolean CHECK_TIME = true;
	boolean WRITE_ORIGINAL = true;
	boolean ALIGN_ORIGINAL_WITH_ORIGINAL = true;
	boolean FIND_ORIGINAL_TEXT = true;
	boolean MAKE_ACTUAL_TREE = true;
	boolean PRINT_ACTUAL_ORIGINAL_TO_CONSOLE = false;
	
	// the following are filepaths necessary for reading and writing to files
	String ROOT_DIRECTORY = "/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/";
	String VARIANT_LIST_FILE  = ROOT_DIRECTORY + "variantList.txt";
	String ALIGNMENTS_FOLDER = ROOT_DIRECTORY + "Alignments/";
	String ALIGNMENT_TABLE_FILE = ROOT_DIRECTORY + "alignmentTable1.txt";
	String WORD_LENGTHS_FILE = ROOT_DIRECTORY + "word_lengths.csv";
	String GLOBAL_ALIGNMENT_FILE = ROOT_DIRECTORY + "Alignments/globalAlignment.csv";
	String NAME_OF_ORIGINAL_GUESS_FILE = ROOT_DIRECTORY + "100_copy.txt";
	String UPGMA_ALIGNMENTS_FILE = ROOT_DIRECTORY + "UPGMAalignmentTable.txt";
	String UPGMA_VARIANT_LIST = ROOT_DIRECTORY + "UPGMAvariantList4.txt";
	String ACTUAL_ORIGINAL_FILE = ROOT_DIRECTORY + "317_copy.txt";

}
