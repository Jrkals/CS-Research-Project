import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
/*
 * class to Find the original text by first taking 
 * a majority reading at every place in a global alignment
 * and then comparing that reading with all of the manuscripts
 * and picking the closest one as the original
 */
public class OriginalTextFinder implements Macros {
	FileReader fr;
	int LENGTHOFMAX = 255; // length of the longest text
	int NUMBEROFTEXTS = 21;
	String[] originalText = new String[LENGTHOFMAX];
	String[][] table = new String[NUMBEROFTEXTS][LENGTHOFMAX];
	int locOfOriginal;
	
	public OriginalTextFinder(String filename) {
		fr = new FileReader(filename);
	}
	public OriginalTextFinder() {
		//default constructor
	}
	/*
	 * Read the global alignment and pick the most common non __ reading 
	 * at each location to produce a guess at the original text
	 */
	public String[] findOriginalText() {
		readTable();
		// iterate through table and find majority readings
		for(int i = 0; i < table[0].length; i++) { // ignore first column which is now blank
			HashMap<String, Integer> frequencies = new HashMap<>();
			for(int j =0; j < table.length; j++) { 
				String key = table[j][i];
				//System.out.print("k"+key+ " ");
				// never put a gap or " " in the final string
				if(key.equals("_")) {
					continue;
				}
			//	System.out.println(key);
				if(frequencies.containsKey(key)) {
					frequencies.put(key, frequencies.get(key)+1);
				}
				else {
					frequencies.put(key, 0);
				}
			} // end of j
		//	System.out.println();
			// store the most common reading
			originalText[i] = findMax(frequencies);
		} // end of i
		return originalText;
	}
	
	/*
	 * read the global alignment and fill the table array
	 */
	public void readTable() {
		table = fr.getGlobalAlignmentWords();
	}
	/*
	 * finds the string with the largest frquency and returns it
	 */
	private String findMax(HashMap<String, Integer> hm) {
		int max = -1;
		String maxString = "";
		for(String s: hm.keySet()) {
			if(hm.get(s) > max) {
				max = hm.get(s);
				maxString = s;
			}
		}
		return maxString;
	}
	
	public void writeToFile(String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		//write number and then a blank line so that this file
		//matches the format of the other input files
		fw.write("1000\n\n");
		for(String word: originalText) {
			fw.write(word+ " ");
		}
		fw.write("\n");
		fw.close();
	}
	/*
	 * write the most common reading on the last line of
	 * globalAlignment.csv
	 */
	public void writeToGlobalAlignment() throws IOException {
		FileWriter fw = new FileWriter(GLOBAL_ALIGNMENT_FILE, true); // set true to append not overwrite
		fw.write("majority,");
		for(String word: originalText) {
			fw.append(word+",");
		}
		fw.close();
	}
	
	/*
	 * takes the 'original string' and aligns it with all of the manuscripts to
	 * see which is closest to it. That manuscript is the actual original for 
	 * the purposes of the tree
	 */
	public String findOriginalManuscript(String originalWritten) {
		int[] scores = new int[NUMBER_OF_TEXTS];
		FileReader frVarList = new FileReader(VARIANT_LIST_FILE);
		String[] files = frVarList.getWordsNoSkip(); // list of manuscripts
		String name = "";
		// align the original with all of the other texts
		for(int i = 0; i < files.length; i++) {
			wordAligner w = new wordAligner(originalWritten, files[i]);
			w.doAlignment(); // align the two texts
			scores[i] = w.getAlignmentScore(); // store score
		}
		// now return the name of the original manuscript
		int max = Utilities.findMax(scores);
		int loc = Utilities.findLocOfmax(max, scores);
		name = files[loc];
		locOfOriginal = loc;
		System.out.println("original file is text "+FileReader.getFileName(name));
		return name;
	}
	
	public int getLocOfOriginal() {
		return locOfOriginal;
	}
}
