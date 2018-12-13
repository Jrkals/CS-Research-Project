import java.util.ArrayList;
import java.util.HashMap;
/*
 * class which makes the RealNode tree
 * given an original and the set of distances between manuscripts
 * the maker aligns the closest pairs and matches them as parent
 * and child depending on their relation to the root. It does this for
 * all pairs. once two members of a pair both have a parent, the 
 * potential pair is removed from the listOfPairs
 */

public class RealTreeMaker implements Macros {
	ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
	String[] listOfManuscripts;
	ArrayList<String> listOfPairs = new ArrayList<>();
	RealNode tree = new RealNode();
	HashMap<Integer, String> indexedNames = new HashMap<>(); // maps loc to manuscript name e.g., 0: "109", 1: "211" etc.
	int indexOfOriginalInMatrix;
	int selfScore = 4000; // score of a matrix aligned with itself
	int badScore = -1000; // score of spot which has been used already
	String nameOfRoot;

	/*
	 * Takes a name of the root Node, a list of manuscript names, and a Hashmap which maps the 
	 * a number to a manuscript name e.g., 8 -> "317", and the location of the original manuscript in the 
	 * list of manuscripts e.g.,
	 * (rootName: "317", flnm: ["109", "211", "232"...], nfn: {0:109, 1:211....}, locOfOrignal: 4)
	 */
	public RealTreeMaker(String rootName, String[] flnms, HashMap<Integer, String> nfn, int locOriginal) {
		tree.setRoot(rootName); 
		listOfManuscripts = flnms;
		makeListOfpairs(); // fill the list based on the listOfManuscripts
		indexedNames = nfn;
		indexOfOriginalInMatrix = locOriginal;
		// fill matrix by reading the alignment table
		FileReader f1 = new FileReader(ALIGNMENT_TABLE_FILE);
		int[][] scores = f1.get2dArrayOfInts();
		for(int i = 0; i < scores.length; i++) {
			ArrayList<Integer> nextRow = new ArrayList<Integer>();
			matrix.add(nextRow);
			for(int j = 0; j < scores[0].length; j++) {
				matrix.get(i).add(scores[i][j]);
			}
		}

		tree.setRoot(rootName);
		nameOfRoot = rootName;
	}

	public void makeTree() {
		while(!all4kOrN1k()) {
			//System.out.println("size is "+listOfPairs.size());
			int[] biggestPair = findBiggestDistance(); // get closest two manuscripts

			// make a string of the two closest e.g., "317,211"
			String currentPair = indexedNames.get(biggestPair[0]) +","+ indexedNames.get(biggestPair[1]);	

			// check if its a valid pair, if not remove from the matrix and try again
			if(!listOfPairs.contains(currentPair)) {
				matrix.get(biggestPair[0]).set(biggestPair[1], badScore);
				matrix.get(biggestPair[1]).set(biggestPair[0], badScore);
				//System.out.println("skipping");
				continue;
			}
			// else it is a valid pair
			String firstOfPair = currentPair.split(",")[0];
			String secondOfPair = currentPair.split(",")[1];

			// connect straight to root
			// case 0
			if(biggestPair[0] == indexOfOriginalInMatrix) {
				// set spot to -1000 so it will not get picked again
				matrix.get(indexOfOriginalInMatrix).set(biggestPair[1], badScore);
				matrix.get(biggestPair[1]).set(indexOfOriginalInMatrix, badScore);
				// check if tree contains second node add it to the root
				if(tree.contains(secondOfPair)) {
					tree.addChild(tree.getNode(secondOfPair));
					System.out.println(nameOfRoot+" is0.0.0 the parent of "+indexedNames.get(biggestPair[1]));
				}
				else { // create a new node and add it to the root
					tree.addChildToRoot(secondOfPair);
					System.out.println(nameOfRoot+" is0.0.1 the parent of "+indexedNames.get(biggestPair[1]));
				}
			}
			else if( biggestPair[1] == indexOfOriginalInMatrix) {
				// set spot to -1000 so it will not get picked again
				matrix.get(indexOfOriginalInMatrix).set(biggestPair[0], badScore);
				matrix.get(biggestPair[0]).set(indexOfOriginalInMatrix, badScore);
				// if tree contains first node, add it to the root
				if(tree.contains(firstOfPair)) {
					tree.addChild(tree.getNode(firstOfPair));
					System.out.println(nameOfRoot+" is0.1.0 the parent of "+indexedNames.get(biggestPair[0]));
				}
				else { // create a new node and add it to the root
				//	tree.printNodeSet();
					tree.addChildToRoot(firstOfPair);
				//	tree.printNodeSet();
					System.out.println(nameOfRoot+" is0.1.1 the parent of "+indexedNames.get(biggestPair[0]));
				}
				//	System.out.println(firstOfPair+" haspar = "+tree.getNode(firstOfPair).hasParent());
			}

			// connecting two non-root nodes
			else { 
				// 4 cases:
				// CASE 1 both in tree
				if(tree.contains(firstOfPair) && tree.contains(secondOfPair)) {
					// CASE 1.1 first is closer to original
					if(matrix.get(biggestPair[0]).get(indexOfOriginalInMatrix) > matrix.get(biggestPair[1]).get(indexOfOriginalInMatrix)) {
						// 2 cases
						// CASE 1.1.1 second has parent so make it the parent of the first
						if(tree.getNode(secondOfPair).hasParent()) {
							RealNode sn = tree.getNode(secondOfPair);
							sn.addChild(tree.getNode(firstOfPair));
							System.out.println(indexedNames.get(biggestPair[1]) +" is1.1.1 the parent of "+indexedNames.get(biggestPair[0]));
						}
						// CASE 1.1.2 second has no parent so make it the child of the first
						else {
							RealNode cn = tree.getNode(firstOfPair);
							cn.addChild(tree.getNode(secondOfPair));
							System.out.println(indexedNames.get(biggestPair[0]) +" is1.1.2 the parent of "+indexedNames.get(biggestPair[1]));
						}
					}
					// CASE 1.2 second is closer to original
					else {
						// 2 cases
						// CASE 1.2.1 first has parent so make it the parent of the second
						if(tree.getNode(firstOfPair).hasParent()) {
							RealNode cn = tree.getNode(firstOfPair);
							cn.addChild(tree.getNode(secondOfPair));
							System.out.println(indexedNames.get(biggestPair[0]) +" is1.2.1 the parent of "+indexedNames.get(biggestPair[1]));
						}
						// CASE 1.2.2 first has no parent so make it the child of the second
						else {
							RealNode cn = tree.getNode(secondOfPair);
							cn.addChild(tree.getNode(firstOfPair));
							System.out.println(indexedNames.get(biggestPair[1]) +" is1.2.2 the parent of "+indexedNames.get(biggestPair[0]));
						}
					}
				} // end of CASE 1

				// CASE 2 the first is in tree but not the second
				else if(tree.contains(firstOfPair) && ! tree.contains(secondOfPair)) {
					//  CASE 2.1 first is closer to original
					if(matrix.get(biggestPair[0]).get(indexOfOriginalInMatrix) > matrix.get(biggestPair[1]).get(indexOfOriginalInMatrix)) {
						RealNode cn = tree.getNode(firstOfPair);
						RealNode sn = new RealNode(secondOfPair, cn);
						cn.addChild(sn);
						RealNode.addNodeToGraph(sn);
						System.out.println(indexedNames.get(biggestPair[0]) +" is2.1 the parent of "+indexedNames.get(biggestPair[1]));
					}
					// CASE 2.2 second is closer to original
					else {
						//CASE 2.2.1 the first has a parent so make it the parent of the second
						if(tree.getNode(firstOfPair).hasParent()) {
							RealNode firstNode = tree.getNode(firstOfPair);
							RealNode sn = new RealNode(secondOfPair, firstNode);
							firstNode.addChild(sn);
							RealNode.addNodeToGraph(sn);
							System.out.println(indexedNames.get(biggestPair[0]) +" is2.2.1 the parent of "+indexedNames.get(biggestPair[1]));
						}
						// CASE 2.2.2 first has no parent so make the second the parent
						else {
							RealNode sn = new RealNode(secondOfPair, null);
							sn.addChild(tree.getNode(firstOfPair));
							RealNode.addNodeToGraph(sn);
							System.out.println(indexedNames.get(biggestPair[1]) +" is2.2.2 the parent of "+indexedNames.get(biggestPair[0]));
						}
					}
				}// end of CASE 2

				// CASE 3 second in tree but not first
				else if(!tree.contains(firstOfPair) && tree.contains(secondOfPair)) {
					// CASE 3.1 first is closer to original
					if(matrix.get(biggestPair[0]).get(indexOfOriginalInMatrix) > matrix.get(biggestPair[1]).get(indexOfOriginalInMatrix)) {
						// CASE 3.1.1 second has parent so make it the parent of the first
						if(tree.getNode(secondOfPair).hasParent()) {
							RealNode sn = tree.getNode(secondOfPair);
							RealNode firstNode = new RealNode(firstOfPair, sn);
							sn.addChild(firstNode);
							RealNode.addNodeToGraph(firstNode);
							System.out.println(indexedNames.get(biggestPair[1]) +" is3.1.1 the parent of "+indexedNames.get(biggestPair[0]));
						}
						// CASE 3.1.2 second has no parent so make it the child of the first
						else {
							RealNode sn = tree.getNode(secondOfPair);
							RealNode firstNode = new RealNode(firstOfPair, null);
							firstNode.addChild(sn);
							RealNode.addNodeToGraph(firstNode);
							System.out.println(indexedNames.get(biggestPair[0]) +" is3.1.2 the parent of "+indexedNames.get(biggestPair[1]));
						}
					}
					// CASE 3.2 second is closer to original
					else {
						RealNode sn = tree.getNode(secondOfPair);
						sn.addChild(new RealNode(firstOfPair, sn));
						System.out.println(indexedNames.get(biggestPair[1]) +" is3.2 the parent of "+indexedNames.get(biggestPair[0]));
					}
				} // end of CASE 3

				else{ //CASE 4 neither are in the tree
					// CASE 4.1 first is closer to original so make it the parent of the second
					if(matrix.get(biggestPair[0]).get(indexOfOriginalInMatrix) > matrix.get(biggestPair[1]).get(indexOfOriginalInMatrix)) {
						RealNode firstNode = new RealNode(firstOfPair, null);
						RealNode sn = new RealNode(secondOfPair, firstNode);
						firstNode.addChild(sn);
						System.out.println(indexedNames.get(biggestPair[0]) +" is4.1 the parent of "+indexedNames.get(biggestPair[1]));
						RealNode.addNodeToGraph(firstNode);
					}
					// CASE 4.2 second is closer to original so make it the parent of the first
					else {
						RealNode sn = new RealNode(secondOfPair, null);
						RealNode firstNode = new RealNode(firstOfPair, sn);
						sn.addChild(firstNode);
						System.out.println(indexedNames.get(biggestPair[1]) +" is4.2 the parent of "+indexedNames.get(biggestPair[0]));
						RealNode.addNodeToGraph(sn);
					}
				} // end of CASE 4
				// set the matrix values to -1000 to show that the work is done
				matrix.get(biggestPair[0]).set(biggestPair[1], badScore);
				matrix.get(biggestPair[1]).set(biggestPair[0], badScore);
			} // end of else
			// pair aligned, to be removed after the tree is changed
			listOfPairs.remove(currentPair);
			removeAllBadPairs();
		} // end of while
		tree.printTree();
		//tree.printNodeSet();
	}

	/*
	 * fill list of pairs with manuscript names as name1,name2
	 */
	void makeListOfpairs() {
		for(int i = 0; i < listOfManuscripts.length; i++) {
			for(int j = 0; j < listOfManuscripts.length; j++) {
				// don't make a pair with itself
				if(i == j) {
					continue;
				}
				String pair = listOfManuscripts[i] +","+ listOfManuscripts[j];
				listOfPairs.add(pair);
			}
		}
	}

	/*
	 * search the table for the largest score and return to location of it
	 * as an int[] of size 2.
	 */
	private int[] findBiggestDistance() {
		int max = -100000;
		int[] rv = new int[2]; 
		for(int i = 0; i < matrix.size(); i++) {
			for(int j = 0; j < matrix.get(0).size(); j++) {
				if(matrix.get(i).get(j) > max && matrix.get(i).get(j) != selfScore) {
					max = matrix.get(i).get(j);
					rv[0] = i;
					rv[1] = j;
				}
			}
		}
		//	System.out.println("max is"+max);
		//	System.out.println("rows are "+rv[0]+ ", "+ rv[1]);
		return rv;
	}

	/*
	 * searches list of pairs and removes sets which both
	 * have parents
	 */
	void removeAllBadPairs() {
		ArrayList<String> toBeRemoved = new ArrayList<String>();
		for(String pair: listOfPairs) {
			String fp = pair.split(",")[0];
			String sp = pair.split(",")[1];
			// if both have parent, remove the pair
			if(tree.contains(fp) && tree.getNode(fp).hasParent()) {
				if(tree.contains(sp) && tree.getNode(sp).hasParent()) {
					toBeRemoved.add(pair);
					//	System.out.println("removed "+pair);
				}
			}
		}

		listOfPairs.removeAll(toBeRemoved);
	}
	/*
	 * checks whether the matrix is entirely -1000 and 4000 entries
	 * if so we are done building the tree
	 */
	boolean all4kOrN1k() {
		for(int i = 0; i < matrix.size(); i++) {
			for(int j = 0; j < matrix.get(i).size(); j++) {
				if(matrix.get(i).get(j) != badScore && matrix.get(i).get(j) != selfScore) {
					return false; // still work to do
				}
			}
		}
		return true; // we are done
	}
}
