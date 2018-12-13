/*
 * Node class for UPGAM tree
 */
public class Node {
	double distance;
	
	String leftChild;
	String rightChild;
	
	
	public Node(String s1, String s2, int dist) {
		distance = dist;
		leftChild = s1;
		rightChild = s2;
	}
	
	public Node() {
		distance = 0.0;
		leftChild = null;
		rightChild = null;
	}
	
	void printTree() {
		System.out.println(leftChild+rightChild);
		System.out.print("   /");
		System.out.print("\t\t\\");
		System.out.println();
		System.out.print("  /");
		System.out.print("\t\t \\");
		System.out.println();
		System.out.print(" /");
		System.out.print("\t\t  \\");
		System.out.println();
		System.out.print(leftChild);
		System.out.println("\t\t"+rightChild);
	}
	
	

}
