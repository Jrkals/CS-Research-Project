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

public class RealTreeMaker {
	ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
	String[] listOfManuscripts;
	ArrayList<String> listOfPairs = new ArrayList<>();
	RealNode tree = new RealNode();
	HashMap<String, Integer> indexedNames = new HashMap<>();
	int indexOfOriginalInMatrix = 0;
	int selfScore = 4000;
	String nameOfRoot = "";

	public RealTreeMaker(String rootName, String[] flnms, HashMap<String, Integer> nfn) {
		tree.setRoot(rootName); 
		listOfManuscripts = flnms;
		makeListOfpairs(); // fill the list based on the listOfManu...
		indexedNames = nfn;
		// fill matrix
		FileReader f1 = new FileReader("/Users/justin/Dropbox/School/CS_Research/TreeOfDocuments/copies/alignmentTable1.txt");
		int[][] scores = f1.get2dArrayOfInts();
		for(int i = 0; i < scores.length; i++) {
			ArrayList<Integer> nextRow = new ArrayList<Integer>();
			matrix.add(nextRow);
			for(int j = 0; j < scores[0].length; j++) {
				matrix.get(i).add(scores[i][j]);
			}
		}
	}

	public void makeTree() {
		while(listOfPairs.size() > 0) {
			int[] biggestPair = findBiggestDistance(); // closest two manuscripts
			String currentPair = indexedNames.get(biggestPair[0]) +","+ indexedNames.get(biggestPair[1]);
			String firstOfPair = currentPair.split(",")[0];
			String secondOfPair = currentPair.split(",")[1];
			if(biggestPair[0] == indexOfOriginalInMatrix || biggestPair[1] == indexOfOriginalInMatrix) {
				// simply attatch the child
				tree.addChildToRoot(currentPair);
			}
			else { //see which is closer to original text
				if(matrix.get(biggestPair[0]).get(indexOfOriginalInMatrix) >
				matrix.get(biggestPair[1]).get(indexOfOriginalInMatrix)) { // first is bigger
					if(tree.contains(firstOfPair)) {
						RealNode currentNode = tree.getNode(firstOfPair);
						currentNode.addChild(new RealNode(secondOfPair, currentNode));
					}
					else { // parent node not in tree
						RealNode parent = new RealNode(secondOfPair, null);
						RealNode child = new RealNode(firstOfPair, parent);
						parent.addChild(child); // adds the child and sets child's parent
					}
				}
				else { // second is bigger
					if(tree.contains(secondOfPair)) {
						RealNode currentNode = tree.getNode(secondOfPair);
						currentNode.addChild(new RealNode(firstOfPair, currentNode));
					}
					else { // parent node not in tree
						RealNode parent = new RealNode(firstOfPair, null);
						RealNode child = new RealNode(secondOfPair, parent);
						parent.addChild(child); // adds the child and sets child's parent
					}
				}
			}
			// pair aligned, to be removed after the tree is changed
			listOfPairs.remove(currentPair);
			removeAllBadPairs();
		} // end of while
		tree.printTree();
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
		for(String pair: listOfPairs) {
			String fp = pair.split(",")[0];
			String sp = pair.split(",")[1];
			// if both have parent, remove the pair
			if(tree.getNode(fp).getParent() != null &&
					tree.getNode(sp).getParent() != null) {
				listOfPairs.remove(pair);
			}
		}
	}
}
