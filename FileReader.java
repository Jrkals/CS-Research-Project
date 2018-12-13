import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
/*
 * Class which does all of the file reading and parsing of all the various
 * text and csv files
 */
public class FileReader implements Macros {
	private File file;
	private Scanner scan;
	private ArrayList<String> words = new ArrayList<String>();
	private int sizeOfWords;
	private String[] wordArray;
	private char[] characterSet;
	private String fileName;
	private String[] fileList = new String[NUMBER_OF_TEXTS];

	public FileReader(String filename) {
		fileName = filename;
		file = new File(filename);
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// Scan words in file
	public String[] getWords() {
		//Scan first two lines, Hochberg formatted these with a number then a blank
		//they are not neede
		scan.nextLine();
		scan.nextLine();
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split(" "); // make array of individual words
			// add each word to the word arrayList
			for(String word: wordsInLine) {
				words.add(word);
			}
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

	// Scan words in file without skipping the first two lines
	// used for variantList.txt
	public String[] getWordsNoSkip() {
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split(" "); // make array of individual words
			// add each word to the word arrayList
			for(String word: wordsInLine) {
				words.add(word);
			}
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

	// Scan words in file using tab delimeter-
	// used for scanning in matrix files created
	public String[] getWordsTab() {
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split("\t"); // make array of individual words
			// add each word to the word arrayList
			for(String word: wordsInLine) {
				words.add(word);
			}
		}
		sizeOfWords = words.size();
		//Put words into fixed size array
		String[] wordsArray = new String[sizeOfWords];
		for(int i = 0; i < sizeOfWords; i++) {
			wordsArray[i] = words.get(i);
		}
		wordArray = wordsArray;
		return wordArray;
	}
	
	// Scan words in file using tab delimeter-
	// used for scanning in matrix files created
	public String[] getWordsTabAndComma() {
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split(",\t"); // make array of individual words
			// add each word to the word arrayList
			for(String word: wordsInLine) {
				words.add(word);
			}
		}
		sizeOfWords = words.size();
		//Put words into fixed size array
		String[] wordsArray = new String[sizeOfWords];
		for(int i = 0; i < sizeOfWords; i++) {
			wordsArray[i] = words.get(i);
		}
		wordArray = wordsArray;
		return wordArray;
	}
	/*
	 * read a global alignment table and return it as an 2d array of strings
	 * this is a csv file
	 */
	public String[][] getGlobalAlignmentWords(){
		String[][] rv = new String[NUMBER_OF_TEXTS][LENGTH_OF_LONGEST_TEXT];
		for(int i = 0; i < NUMBER_OF_TEXTS; i++) {
			String[] line = scan.nextLine().split(","); // csv file
			line[0] = "";
			//	Utilities.printArray(line);
			rv[i] = line;
		}
		return rv;
	}

	// put words string array into char[]
	public char[] getCharacters() {
		getWords();
		char[] characters = new char[getCharCount(wordArray)];
		ArrayList<Character> chars = new ArrayList<>();
		for(int i = 0; i < wordArray.length; i++) {
			String temp = wordArray[i];
			for(int j = 0; j < temp.length(); j++) {
				chars.add(temp.charAt(j));
			}
		}
		// convert arrayList chars to fixed size char [] characters
		for(int i = 0; i < chars.size(); i++) {
			characters[i] = chars.get(i);
		}
		characterSet = characters;
		return characters;
	}

	public int getSizeOfWords() {
		return sizeOfWords;
	}

	public int getSizeOfChars() {
		return characterSet.length;
	}

	// num of characters in an array of strings
	private int getCharCount(String[] words) {
		int charCount = 0;
		for(int i = 0; i < words.length; i++) {
			charCount += words[i].length();
		}
		//System.out.println("charCount is "+charCount);
		return charCount;
	}
	// makes hash map of chars to their occurrence rate
	public HashMap<Character, Double> calculateFrequencies(){
		getCharacters();
		HashMap<Character, Double> frequencies = new HashMap<>();
		for(int i = 0; i < characterSet.length; i++) {
			// if contains, increase number of occurrences
			if(frequencies.containsKey(characterSet[i])) {
				frequencies.put(characterSet[i], frequencies.get(characterSet[i])+1);
			}
			//else put in for first time
			else {
				frequencies.put(characterSet[i], 1.0);
			}
		}
		// map letter to its occurrence rate
		for(Character key: frequencies.keySet()) {
			System.out.println("occurences " + frequencies.get(key));
			double frequency = frequencies.get(key)/characterSet.length;
			//	System.out.println("freq is "+frequency);
			//need to conv characterSet.length to double before dividing
			frequencies.put(key, frequency);
			System.out.printf("key: %s frequency: %.3f \n", key, frequency);
		}
		return frequencies;
	}

	/*
	 * if the file read in is the alignment scores return it as a 2d array of ints 
	 */
	int[][] get2dArrayOfInts(){
		String[] words = getWordsTabAndComma();
		double size = Math.sqrt(words.length);
		int[][] rv = new int[(int)size][(int)size];
		int count = 0;
		for(int i = 0; i < (int) size; i++) {
			for(int j = 0; j < (int) size; j++) {
				rv[i][j] = Integer.parseInt(words[count]);
				count++;
			}
		}
		return rv;
	}

	String getFileName() {
		int x = fileName.indexOf("copy");
		//System.out.println("name is: "+fileName.substring(x-4, x-1));
		return fileName.substring(x-4, x-1);
	}

	String getFileName(String file) {
		int x = file.indexOf("copy");
		//System.out.println("name is: "+fileName.substring(x-4, x-1));
		return file.substring(x-4, x-1);
	}
	/*
	 * get the files from Variantlist.txt and return the number section as a string[]
	 */
	String[] getFileNamesFromVariantList() {
		getWordsNoSkip();
		int i = 0;
		for(String file: wordArray) {
		//	System.out.println(file);
			fileList[i] = getFileName(file);
			i++;
		}
		return fileList;
	}
	/*
	 * fill a hashmap with the filename associated with their index
	 */
	HashMap<Integer, String> returnIndexedNames(){
		HashMap<Integer, String> numToNames = new HashMap<>();
		for(int i = 0; i < fileList.length; i++) {
			numToNames.put(i, fileList[i]);
		}
		
		return numToNames;
	}

}
