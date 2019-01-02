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
	// skip the first line since the name of the file is on that line
	// exit when reaching the end of the alignment (third to last line)
	public String[] getWordsFirstAlignment() {
		scan.nextLine(); // skip first line
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split(","); // make array of individual words
			if(wordsInLine[0].equals("Total Diffs")) {
				break;
			}
			words.add(wordsInLine[0]); // add the first word, ignore the second
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
	 * exit when reaching the end of the alignment (third to last line)
	 */
	public String[] getWordsSkipFirst() {
		scan.nextLine(); //skip the first line
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split(","); // make array of individual words
			// if reached last line exit
			if(wordsInLine[0].equals("Total Diffs")) {
				break;
			}
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
	
	public int getSizeOfWords() {
		return sizeOfWords;
	}
	
	public String[] getWords() {
		return wordArray;
	}
}
