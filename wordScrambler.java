import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//Justin Kalan
//9-4-18
/* 
 * A file to scramble a text
 * The input is John 1:1-18 which 
 * will be scrambled and sent to Dr. Hochberg.
 */
public class wordScrambler {

	public static void main(String[] args) {

		ArrayList<String> words = new ArrayList<String>();
		
		File file = new File("/Users/justin/Dropbox/School/CS_Research/john1-1-18.txt");
		Scanner scan;
		try {
			scan = new Scanner(file);
			// Scan words in file
			while(scan.hasNext()) {
				String line = scan.nextLine();
				String[] wordsInLine = line.split(" "); // make array of individual words
				// add each word to the word arrayList
				for(String word: wordsInLine) {
					words.add(word);
				}
			}
			// check to see if all the words are there
			System.out.println("there are " + words.size() + " words");
		/*	for(String word: words) {
				System.out.println(word);
			}*/
			
			String[] scrambledWords = ScrambleWord(words);
			System.out.println("********SCRAMBLED WORDS**********");
			//make sure they are scrambled
			for(String word: scrambledWords) {
				System.out.println(word);
			}
			
			//Write scrambled words to a file
			File outputFile = new File("/Users/justin/Dropbox/School/CS_Research/scrambled.txt");
			try {
				FileWriter fwr = new FileWriter(outputFile);
				for(String word: scrambledWords) {
					fwr.write(word +" ");
				}
				fwr.close();
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("could not find output file");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(" could not find input file");
		}
	} // end of main
	
	/* 
	 * Function to take an arraylist of words and return
	 * a fixed array of them scrambled
	 */
	static String[] ScrambleWord(ArrayList<String> words) {
		// random number generator
		Random ran = new Random();
		
		//size of the original words array
		//(it will change so this must be stored)
		int size = words.size();
		
		//Fixed array of scrambled words
		String[] scrambledWords = new String[words.size()];
		
		// fill scrambled words with random num from words then remove
		// that number from words
		for(int i = 0; i < size; i++) {
			//System.out.print("i is "+i);
			
			// pick a random spot on words based on its size which 
			// decreases each time as words are removed
			int randomSpot = ran.nextInt(words.size());
			//System.out.print(" spot is "+ randomSpot);
			scrambledWords[i] = words.get(randomSpot); // put the word in the array
			//System.out.print(" word picked is "+words.get(randomSpot));
			words.remove(randomSpot); // removes the word at that spot
			//System.out.print(" size of words is "+words.size());
			//System.out.println();
		}
		return scrambledWords;
	}
} // end of class
