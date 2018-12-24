import java.util.ArrayList;
import java.util.HashMap;

/*
 * Class which produces and UPGMA tree of a selection of given manuscripts.
 * UPGMA creates a binary tree with theoretical ancestors of closely allied texts.
 * This requires using some but not all of the texts in the collection
 * 
 * 
 * 
 * test data from wikipedia
 * int[][] scores = {{0,17,21,31,23},{17,0,30,34,21},{21,30,0,28,39},
 * {31,34,28,0,43},{23,21,39,43,0}};
 */
public class TreeProducer {
	ArrayList<ArrayList<Integer>> table = new ArrayList<>();

	//arrayList containing the list of nodes e.g., A, B, C, DE, FGH etc.
	// gets shorter over time as node names get combined
	ArrayList<String> nodeNameList = new ArrayList<>();
	HashMap<String, Node> tree = new HashMap<>();
	
	final int selfScore = 4000;

	public TreeProducer(int[][] scores) {
		nodeNameList.add(getNextChar('@')); // '@' is right before 'A' in ASCII
		// copy into arrayList
		for(int i =0; i <scores.length;i++) {
			table.add(new ArrayList<Integer>());
			// make new char out of previous entry
			nodeNameList.add(getNextChar(nodeNameList.get(i).charAt(0)));
			for(int j = 0; j <scores[i].length; j++) {
				table.get(i).add(scores[i][j]);
			}
		}
		//remove last which is extra since put A in before loop and went through whole loop
		nodeNameList.remove(nodeNameList.size()-1);
	//	System.out.println("node name lise size: "+nodeNameList.size());
	}
	
	/*
	 * Constructor which takes a list of names and does not produce a set of ordered characters as names
	 */
	public TreeProducer(int[][] scores, String[] names) {
		// copy names into list
		for(String n:names) {
			nodeNameList.add(n + ",");
		}
		// copy scores into arrayList
		for(int i =0; i <scores.length;i++) {
			table.add(new ArrayList<Integer>());
			for(int j = 0; j <scores[i].length; j++) {
				table.get(i).add(scores[i][j]);
			}
		}
	}
	

	public void makeTree() {
		//print2DArray(table);
		int i = 0;
		while(table.size() > 1) {
		//	System.out.println("node name list size: "+nodeNameList.size());
			//	System.out.println("i is "+i+" table size in makeTree is "+table.size());
			int[] rowsOfSmallest = findBiggestDistance(table.size(), table.get(i).size());
			combineRows(rowsOfSmallest[0], rowsOfSmallest[1]);
			//print2DArray(table);
		}
		//print2DArray(table);
		//printTree(nodeNameList.get(0));
		printTreeAsString();
	}

	// returns next alphabetical character as a string
	private String getNextChar(char c) {
		c++;
		// convert to string
		return c+"";
	}


	/*
	 * search the table for the smallest distance and return to location of it
	 * as an int[] of size 2.
	 */
	private int[] findSmallestDistance(int dimx, int dimy) {
		int min = 100000000;
		int[] rv = new int[2]; 
		for(int i = 0; i < dimx; i++) {
			for(int j = 0; j < dimy; j++) {
				if(table.get(i).get(j) < min && table.get(i).get(j) != 0) {
					min = table.get(i).get(j);
					rv[0] = i;
					rv[1] = j;
				}
			}
		}
		//	System.out.println("min is"+min);
		//	System.out.println("rows are "+rv[0]+ ", "+ rv[1]);
		return rv;
	}
	
	/*
	 * search the table for the largest score and return to location of it
	 * as an int[] of size 2.
	 */
	private int[] findBiggestDistance(int dimx, int dimy) {
		int max = -100000;
		int[] rv = new int[2]; 
		for(int i = 0; i < dimx; i++) {
			for(int j = 0; j < dimy; j++) {
				if(table.get(i).get(j) > max && table.get(i).get(j) != selfScore) {
					max = table.get(i).get(j);
					rv[0] = i;
					rv[1] = j;
				}
			}
		}
		//	System.out.println("max is"+max);
		//	System.out.println("rows are "+rv[0]+ ", "+ rv[1]);
		return rv;
	}

	// takes location of the smallest matching values and changes scoreTable into a smaller
	// table with those two rows averaged into one
	private void combineRows(int row1, int row2) {
		// done at this point
		if(row1 == row2) {
			return;
		}
		//	System.out.println("table size is "+table.size());
		ArrayList<Integer> firstRow = table.get(row1);
		ArrayList<Integer> secondRow = table.get(row2);
		// if last two items do combine here
		if(table.size() == 2) {
		//	System.out.println("last time through doing something");
			Node node = new Node();
			node.distance = table.get(row2).get(0)/2;
			node.leftChild = nodeNameList.get(row1);
			node.rightChild = nodeNameList.get(row2);
			combineNodeNames(row1, row2);
			tree.put(nodeNameList.get(0), node);
			table.remove(row2);
			ArrayList<Integer> finalRow = new ArrayList<Integer>();
			finalRow.add(0);
			finalRow.add((int)node.distance);
			table.set(0, finalRow);
		}
		else if(row1 != row2) {
			Node node = combineRows(firstRow, secondRow);
			combineNodeNames(row1, row2);
			tree.put(nodeNameList.get(row1), node);
		}
	}

	// changes NodeName list by length one by combining the two rows
	private void combineNodeNames(int row1, int row2) {
		// combine
		nodeNameList.set(row1, nodeNameList.get(row1) + nodeNameList.get(row2));

		//remove row2
		nodeNameList.remove(row2);
	}

	// combines two rows into 1 of size 1 less than the originals
	// returns the two strings of the children rows and the distance between them
	private Node combineRows(ArrayList<Integer> a, ArrayList<Integer> b) {
		Node rv = new Node();
		int indexOfColumnA = table.indexOf(a);
		int indexOfColumnB = table.indexOf(b);
		rv.leftChild = nodeNameList.get(indexOfColumnA);
		rv.rightChild = nodeNameList.get(indexOfColumnB);
		//	System.out.println("index of a is:"+indexOfColumnA);
		//	System.out.println("index of b is:"+indexOfColumnB);

		if(indexOfColumnA == -1 || indexOfColumnB == -1) {
			System.out.print("passed in invalid row");
			return null;
		}

		ArrayList<Integer> combined = new ArrayList<>();
	//	print1DArray(a);
	//	print1DArray(b);
		int spotOfRepeat = 0; // store index of where one row has dist to self
		//create new matrix which is an average of the first two
		for(int i = 0; i < a.size(); i++) {
			// Two ifs to store the dist to self avged with dist to other, one of these will be deleted.
			if(a.get(i) == 4000) {
				combined.add(4000); // new distance to self is 4000
				continue;
			}
			if(b.get(i) == 4000) {
				spotOfRepeat = i; // this index will be removed
			}
			combined.add((a.get(i) + b.get(i))/2);
		}
	//	System.out.println("combined is:");
	//	print1DArray(combined);
		rv.distance = findRepeat(combined);
		combined.remove(spotOfRepeat);
		//combined = removeRepeats(combined);

		// remove b
		table.remove(b);
		// replace a with combined a & b
		//	System.out.println("index of a is:"+table.indexOf(a));
		table.set(table.indexOf(a), combined);
		//print2DArray(table);
		//remove column of b
		for(int i = 0; i < table.size(); i++) {
			// don't remove the new row
			if(i != table.indexOf(combined)) {
				table.get(i).remove(indexOfColumnB);
			}
		}
		//print2DArray(table);
		//make column of a into row of a
		for(int i = 0; i < table.size(); i++) {
			table.get(i).set(indexOfColumnA, combined.get(i));
		}
		return rv;
	}
	// finds the repeated value which is the distance between the 
	// previously combined rows and returns it
	private double findRepeat(ArrayList<Integer> a) {
		int temp = 0;
		for(int i = 0; i < a.size(); i++) {
			temp = a.get(i);
			for(int j = 0; j < a.size(); j++) {
				if(temp == a.get(j) && j != i)
					return (double) temp;
			}
		}
		return 0.0;
	}

	// removes the repteated value in an array and returns one of size of the original -1
	private ArrayList<Integer> removeRepeats(ArrayList<Integer> a) {
		int temp = 0;
		int originalSize = a.size();

		for(int i = 0; i < a.size(); i++) {
			temp = a.get(i);
			for(int j = 0; j < a.size(); j++) {
				// remove repeat
				if(j != i && a.get(j) == temp) {
					System.out.println("removing: "+a.get(j));
					a.remove(j);
					// set other distance to 4000
					a.set(i, selfScore);
					return a;
				}
			}
		}
		int finalSize = a.size();
		if(originalSize-finalSize > 1) {
			System.out.println("error: too many numbers removed");
			return null;
		}
		if(originalSize-finalSize == 0) {
			System.out.println("error: too few numbers removed");
			return null;
		}
		return a;
	}

	void printTree(String s) {
		if(s.length() == 1) {
			//	System.out.println(s);
			return;
		}
		else {
			tree.get(s).printTree();
			printTree(tree.get(s).leftChild);
			printTree(tree.get(s).rightChild);
		}
	}

	void printTreeAsString() {
		for(String node: tree.keySet()) {
			System.out.print("there is a common parent between:");
			System.out.println(tree.get(node).leftChild + " and "+tree.get(node).rightChild);
		}
	}
}
