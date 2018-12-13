import static org.junit.Assert.*;

import org.junit.Test;

/*
 * Unit test class for RealNode
 */
public class RealNodeTests {

	RealNode n;
	RealNode n2;
	
	private void setupTree() {
		n = new RealNode();
		n.setRoot("317");
		n2 = new RealNode("312", null);
		n2.addChild(new RealNode("109", n2));
		RealNode.addNodeToGraph(n2);
	}
	
	private void setupBiggerTree() {
		n = new RealNode();
		n.setRoot("317");
		n2 = new RealNode("427", null);
		n2.addChild(new RealNode("702", n2));
		n2.addChild(new RealNode("321", n2));
		RealNode.addNodeToGraph(n2);
		RealNode n3 = n.getNode("702");
		n3.addChild(new RealNode("833", n3));
		RealNode.addNodeToGraph(n3);
		n.addChild(new RealNode("167", n));
		RealNode n4 = n.getNode("167");
		n4.addChild(n2);
	}
	/*
	 * Tests will small tree
	 */
	 @Test
	public void testRealNodeContains() {
		setupTree();
		assertEquals(true, n2.contains("109"));
	}
	@Test
	public void testRealNodeContains2() {
		setupTree();
		assertEquals(true, n.contains("109"));
	}
	@Test
	public void testRealNodeContains3() {
		setupTree();
		assertEquals(true, n.contains("312"));
	}
	@Test
	public void testRealNodeContains4() {
		setupTree();
		assertEquals(true, n.contains("317"));
	}
	@Test
	public void testRealNodeContains5() {
		setupTree();
		assertEquals(false, n.contains("318"));
	}
	@Test
	public void testRealNodeContains6() {
		setupTree();
		assertEquals(false, n.contains("4000"));
	}
	
	@Test
	public void testGetNode1() {
		setupTree();
		assertEquals("109", n.getNode("109").name);
	}
	@Test
	public void testGetNode2() {
		setupTree();
		assertEquals("312", n.getNode("312").name);
	}
	@Test
	public void testGetNode3() {
		setupTree();
		assertEquals("317", n.getNode("317").name);
	}
	
	/*
	 * Tests with bigger tree
	 */
	@Test
	public void testGetNodeBig1() {
		setupBiggerTree();
		assertEquals("167", n.getNode("167").name);
	}
	
	@Test
	public void testGetNodeBig2() {
		setupBiggerTree();
		assertEquals("833", n.getNode("833").name);
	}
	
	@Test
	public void testContainsBig3() {
		setupBiggerTree();
		assertEquals(true, n.contains("321"));
	}

}
