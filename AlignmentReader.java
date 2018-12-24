import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/*
 * Class to read alignment files
 */
public class AlignmentReader {
	private File file;
	private Scanner scan;
	private ArrayList<String> words = new ArrayList<String>();
	private int sizeOfWords;
	private String[] wordArray;
	
	public AlignmentReader(String filename) {
		file = new File(filename);
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Scan words in file without skipping the first two lines
	public String[] getWordsNoSkip() {
		while(scan.hasNext()) {
			String line = scan.nextLine();
			// end of file
			if(line.equals("**********END***********")) {
				break;
			}
			String[] wordsInLine = line.split(","); // make array of individual words
			words.add(wordsInLine[1]); // add the second word, ignore the first
		}
		sizeOfWords = words.size();
		//Put words into fixed size array
		String[] wordsArray = new String[sizeOfWords];
		for(int i = 0; i < sizeOfWords; i++) {
			wordsArray[i] = words.get(i);
		}
		scan.close();
		wordArray = wordsArray;
		return wordArray;
	}
	
	
	
	// gets the words of the first of the pair of aligned strings
	
	public String[] getWordsFirstAlignment() {
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split(","); // make array of individual words
			words.add(wordsInLine[0]); // add the second word, ignore the first
		}
		sizeOfWords = words.size();
		//Put words into fixed size array
		String[] wordsArray = new String[sizeOfWords];
		for(int i = 0; i < sizeOfWords; i++) {
			wordsArray[i] = words.get(i);
		}
		scan.close();
		wordArray = wordsArray;
		return wordArray;
	}
	/*
	 * function to get words but skip first line
	 */
	public String[] getWordsSkipFirst() {
		scan.nextLine();
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split(","); // make array of individual words
			words.add(wordsInLine[0]); // add the second word, ignore the first
		}
		sizeOfWords = words.size();
		//Put words into fixed size array
		String[] wordsArray = new String[sizeOfWords];
		for(int i = 0; i < sizeOfWords; i++) {
			wordsArray[i] = words.get(i);
		}
		scan.close();
		wordArray = wordsArray;
		return wordArray;
	}
	
	public int getSizeOfWords() {
		return sizeOfWords;
	}
	
	public String[] getWords() {
		return wordArray;
	}
}
