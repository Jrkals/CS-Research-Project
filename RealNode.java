import java.util.ArrayList;

/*
 * Node class for the final tree
 * every node has a parent and a list of children and a name
 */
public class RealNode {
	String name;
	RealNode parent;
	ArrayList<RealNode> children;
	boolean rootHuh = false;
	int numChildren = 0;

	public RealNode() {
		name = "";
		parent = null;
		children = null;
		rootHuh = false;
	}

	public RealNode(String nm, RealNode par) {
		name = nm;
		parent = par;	
	}


	public String getName() {
		return name;
	}

	public ArrayList<RealNode> getChildren() {
		return children;
	}

	public RealNode getParent() {
		return parent;
	}

	// makes this node the root
	public void setRoot(String nm) {
		rootHuh = true;
		name = nm;
	}

	public void addChildToRoot(String pair) {
		String firstPair = pair.split(",")[0];
		if(firstPair.equals(name)) {
			this.children.add(new RealNode(firstPair, this));
		}
		this.children.add(new RealNode(firstPair, this));

	}
	/*
	 * check whether a given named node is in the tree
	 */
	public boolean contains(String nm) {
		//base case
		if(numChildren == 0) {
			return this.name.equals(nm);
		}
		else { // see if it is the parent
			if(this.name.equals(nm)) {
				return true;
			}
			else { // see if it is a child
				for(RealNode child: children) {
					return child.contains(nm);
				}
			}
		}
		return false;
	}
	/*
	 * return the node of a given name
	 */
	public RealNode getNode(String nm) {
		if(this.contains(nm)) {
			if(this.name.equals(nm)) {
				return this;
			}
			else {
				for(RealNode child: children) {
					return child.getNode(nm);
				}
			}
		} 
		//else not found
		System.out.println("ERROR: NODE NOT FOUND");
		return null;
	}
	
	/*
	 * add child node to tree
	 */
	public void addChild(RealNode n) {
		this.children.add(n);
		n.parent = this;
		numChildren++;
	}
	
	public void printTree() {
		if(this.numChildren == 0) {
			System.out.println(this.name);
		}
		else {
			System.out.println(this.name);
			for(RealNode child: children) {
				child.printTree();
			}
		}
	}
}
