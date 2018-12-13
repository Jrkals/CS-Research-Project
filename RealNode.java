import java.util.ArrayList;
import java.util.HashSet;

/*
 * Node class for the final tree
 * every node has a parent and a list of children and a name
 */
public class RealNode {
	String name;
	RealNode parent;
	ArrayList<RealNode> children = new ArrayList<RealNode>();
	static HashSet<RealNode> nodeSet = new HashSet<RealNode>();
	boolean rootHuh = false;
	boolean hasParent = false;

	public RealNode() {
		name = "";
		parent = null;
		rootHuh = false;
		nodeSet.add(this);
	}

	public RealNode(String nm, RealNode par) {
		name = nm;
		setParent(par);
		nodeSet.add(this);
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

	public boolean hasParent() {
		return hasParent;
	}

	// makes this node the root
	public void setRoot(String nm) {
		rootHuh = true;
		name = nm;
		hasParent = true;
	}

	public void addChildToRoot(String pair) {
		RealNode newNode = new RealNode(pair, this);
		newNode.setParent(this);
		this.addChild(newNode);
		nodeSet.add(newNode);
	}

	/*
	 * Recursive method to see if the tree contains a node
	 * of the given name
	 */
	boolean contains(String nm) {
		if(graphContains(nm)) {
			return true;
		}
		//base case
		if(this.children.size() == 0) {
			return this.name.equals(nm);
		}
		else {
			if(this.name.equals(nm)) {
				return true;
			}
			boolean rv = false; 
			// see if the children contain it recursively
			for(RealNode child: children) {
				if(child.contains(nm)) {
					rv = true;
				}
			}
			return rv;
		}
	}

	RealNode getNode(String nm) {
		//if(nm.equals("109"))
		//	System.out.println("looking for "+nm);
		// make sure it is in the tree
		if(!contains(nm) && ! graphContains(nm)) {
			System.out.println("DOES NOT CONTAIN "+nm);
			return null;
		}
		if(this.name.equals(nm)) {
			if(nm.equals("109"))
				System.out.println("at "+nm);
			return this;
		}
		else {
			// search children
			for(RealNode child: children) {
				if(child.name.equals(nm)) {
					if(nm.equals("109"))
						System.out.println("found in children "+nm);
					return child;
				}
				else {
					return child.getNode(nm);
				}
			} // end search children
			
			// search graph
			for(RealNode n: nodeSet) {
				if(n.name.equals(nm)) {
					//if(nm.equals("109"))
					//	System.out.println("found in graph "+nm+ "hasParent = "+n.hasParent);
					return n;
				}
			} // end search graph
		}
		return null;
	}

	/*
	 * add child node to tree
	 */
	public void addChild(RealNode n) {
		this.children.add(n);
		n.setParent(this);
		addNodeToGraph(n);
	}

	public void printTree() {
		// base case print self
		if(this.children.size() == 0) {
			System.out.print(this.name +"\t");
		}
		// else self then children
		else {
			switch(children.size()) {
			case 1: System.out.println("\t"+this.name);
			System.out.println("\t|\n"); 
			this.children.get(0).printTree(); 
			break;
			case 2: System.out.println("\t"+this.name);
			System.out.println("/ \t\t \\"); 
			this.children.get(0).printTree(); 
			this.children.get(1).printTree(); 
			break;
			case 3: System.out.println("\t"+this.name);
			System.out.println("/ \t | \t\t \\"); 
			for(RealNode child: children)child.printTree(); 
			break;
			case 4: System.out.println("\t\t"+ this.name); 
			System.out.println("/ \t | \t | \t \\"); 
			for(RealNode child: children)child.printTree(); 
			break;
			default: for(RealNode child: children)child.printTree(); System.out.println();
			}
		}

	}

	private void setParent(RealNode n) {
		if(n == null) {
			this.parent = null;
			this.hasParent = false;
			return;
		}
		this.parent = n;
		this.hasParent = true;
	}

	/*
	 * add node to static graphMembers list
	 */
	public static void addNodeToGraph(RealNode n) {
		nodeSet.add(n);

	}
	/*
	 * check to see if the graph contains a node of that name
	 */
	private boolean graphContains(String nm) {
		for(RealNode n: nodeSet) {
			if(n.name.equals(nm)) {
				return true;
			}
		}
		return false;
	}
	
	public void printNodeSet() {
		for(RealNode n: nodeSet) {
			System.out.println(n.name +" parent?: "+n.hasParent);
		}
	}
}
