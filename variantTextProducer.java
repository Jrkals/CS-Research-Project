import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class variantTextProducer {

	public static void main(String[] args) throws IOException {
		
		String originalFileName = "/Users/justin/Dropbox/School/CS_Research/variant17_from_variant12.txt";
		String variantFileName = "/Users/justin/Dropbox/School/CS_Research/variant24_from_variant17.txt";
		
		ArrayList<String> words = new ArrayList<String>();
		// Read a the File
		File originalFile = new File(originalFileName);
		Scanner scan;
		scan = new Scanner(originalFile);
		// Scan words in file
		while(scan.hasNext()) {
			String line = scan.nextLine();
			String[] wordsInLine = line.split(" "); // make array of individual words
			// add each word to the word arrayList
			for(String word: wordsInLine) {
				words.add(word);
			}
		}
		scan.close();
		// check to see if all the words are there
		System.out.println("there are " + words.size() + " words");
		
		ArrayList<String> newWords = new ArrayList<>();
		// Make changes to words
		newWords = makeVariations(words);
		
		// Write modified word list to file
		File outputFile = new File(variantFileName);
		
		FileWriter fwr = new FileWriter(outputFile);
		for(String word: newWords) {
			fwr.write(word +" ");
		}
		fwr.close();
	} //end of main
	
	// Function to make slight variations on words
	// keeps the word list the same size
	public static ArrayList<String> makeVariations(ArrayList<String> words) {
		ArrayList<String> newWords = new ArrayList<>();
		Random rand = new Random();
		
		for (int i = 0; i < words.size(); i++){
			String word = words.get(i); // current word
		//	System.out.println("current word is "+word);
			int randNum = rand.nextInt(100); // gens rand num from 0-9
			//Make change
			if(randNum == 5) {  // 1 in 10 chance of this
				int randNum2 = rand.nextInt(5); // 0-4
				// Add letter to word
				if(randNum2 == 0) {
					word = insertRandomLetter(word);
					newWords.add(word);
				}
				//subtract letter from word
				else if(randNum2 == 1) {
					word = removeRandomLetter(word);
					newWords.add(word);
				}
				//swap letter from word with another
				else if(randNum2 == 2) {
					int randIndex = rand.nextInt(word.length());
					// get number from 97–122 (ASCII lower case chars)
					int randLetter = rand.nextInt(26) + 97; 
					char newChar = (char)randLetter; // cast to char
					System.out.println("new char is "+newChar);
					word = replace(word, randIndex, newChar);
					newWords.add(word);
				}
				// add a new word to the list words
				else if(randNum2 == 3) {
					System.out.println("adding a word to the list " +word);
					//add word twice
					newWords.add(word);
					newWords.add(word);
				}
				// delete the word from the list words
				else {
					System.out.println("removing the "+i+ "th word from the list "+word);
				}
			} // end randNum
			else { //no changes just append the word
			newWords.add(word);
			}
		} // end of for
		return newWords;
	}
	
	// Function that replaces the char in word at index randIndex with newChar
	private static String replace(String word, int randIndex, char newChar) {
		System.out.println("Replacing word");
		System.out.println("Original word " +word);
		String newString = "";
		for(int i = 0; i < word.length(); i++) {
			// swap char
			if(i == randIndex) {
				newString += newChar;
			}
			// just copy the original string
			else {
			newString += word.charAt(i);
			}
		}
		System.out.println("Modified word "+ newString);
		return newString;
	}

	// Function to insert a new letter to a random spot in a word
	// increases the length of the word
	public static String removeRandomLetter(String word) {
		System.out.println("removing random letter from "+word);
		String newString = "";
		Random rand = new Random();
		if(word.length() == 0) {
			System.out.println("empty word");
			return "";
		}
		// pick a random spot in the word
		int randIndex = rand.nextInt(word.length());
		for(int i = 0; i < word.length(); i++) {
			// if at that spot do nothing
			if(i == randIndex) {
				continue;
			}
			else {
				newString += word.charAt(i);
			}
		}
		System.out.println("modified word is "+newString);
		return newString;
	}
	
	// Function to remove a letter from a word
	// decreases the length of the word by 1
	public static String insertRandomLetter(String word) {
		System.out.println("inserting random letter from " +word);
		String newString = "";
		Random rand = new Random();
		// pick a random spot in the word
		int randIndex = rand.nextInt(word.length());
		for(int i = 0; i < word.length(); i++) {
			// if at that copy and add a new char
			if(i == randIndex) {
				newString += word.charAt(i);
				
				// get number from 97–122 (ASCII lower case chars)
				int randLetter = rand.nextInt(26) + 97; 
				char newChar = (char)randLetter; // cast to char
				newString += newChar;
			}
			else {
				newString += word.charAt(i);
			}
		}
		System.out.println("new word looks like "+newString);
		return newString;
	}

}// end of class
