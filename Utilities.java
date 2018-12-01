/*
 * class for common methods used throughout the project
 * these are all static and are called by class name
 */
public class Utilities {

	// print a one dimensional int array
	static void printArray(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+ "\t ");
		}
		System.out.println();
	}
	// print a one dimensional string array
	static void printArray(String[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+ "\t ");
		}
		System.out.println();
	}
	
	// find the first instance where a num is in an array
	static int findLocOfmax(int num, int[] array) {
		for(int i = 0; i <array.length; i++) {
			if (array[i] == num){
				return i;
			}
		}
		return -1; // not found
	}
	
	static int findMax(int[] array) {
		int rv = -1000000;
		for(int i = 0; i < array.length; i++) {
			if(rv < array[i]) {
				rv = array[i];
			}
		}
		return rv;
	}
}
