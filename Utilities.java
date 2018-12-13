import java.util.ArrayList;

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

	static void printArray(ArrayList<Integer> al) {
		for(int i = 0; i < al.size(); i++) {
			System.out.print(al.get(i)+" ");
		}
		System.out.println();
	}

	//print 2d arrayList<Integers>
	static void printArray2D(ArrayList<ArrayList<Integer>> al) {
		for(int i = 0; i <al.size(); i++) {
			printArray(al.get(i));
		}
	}

	// prints 2d string array
	static void print2DArray(String[][] a) {
		for(int i = 0; i < a.length; i++) {
			printArray(a[i]);
		}
	}

	// print 2d int array
	static void print2DArray(int[][] a) {
		for(int i = 0; i < a.length; i++) {
			printArray(a[i]);
		}
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
	/*
	 * find the max value in an array
	 */
	static int findMax(int[] array) {
		int rv = -1000000;
		for(int i = 0; i < array.length; i++) {
			if(rv < array[i]) {
				rv = array[i];
			}
		}
		return rv;
	}


	//return the max of three numbers
	public static int max(int a, int b, int c) {
		if(a > b && a > c) 
			return a;
		if(b > a && b > c) 
			return b;
		else {
			// check for possible tie
			if(c == a || c == b) {

			}
			return c;
		}
	}

	// return the max of two numbers
	public static int max(int a, int b) {
		if(a > b)
			return a;
		return b;
	}

}
