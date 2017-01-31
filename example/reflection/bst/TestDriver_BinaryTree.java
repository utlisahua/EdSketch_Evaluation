/**
 * @author Lisa Dec 1, 2016 TestDriver.java 
 */
package test.sketch4j.example.reflection.bst;

import sketch4j.generator.SketchRequest;
import test.sketch4j.example.TestDriver;

/**
 * While this can be replaced with JUNIT, I use main for simplicity.
 * 
 * @author lisahua
 *
 */
public class TestDriver_BinaryTree extends TestDriver {
	protected BinaryTree_base sketch;

	protected boolean checkEq(Node x, Node y) {
		if (x == null && y == null)
			return true;
		if (x.key != y.key)
			return false;
		if (!checkEq(x.left, y.left))
			return false;
		if (!checkEq(x.right, y.right))
			return false;
		return true;
	}

	public String toString() {
		return sketch.toString();
	}

	private boolean test1() {
		return testHelper(new int[] { 2 });

	}

	private boolean test2() {
		return testHelper(new int[] { 2, 1 }) && testHelper(new int[] { 1, 2 });
	}

	private boolean test3() {
		return testHelper(new int[] { 1, 2, 3 }) && testHelper(new int[] { 3, 1, 2 })
				&& testHelper(new int[] { 2, 1, 3 }) && testHelper(new int[] { 1, 3, 2 });
	}

	private boolean testRepOK1() {
		sketch =  new BinaryTree_sketch(new int[] { 4,2,7,3,5,10 }) ;
		sketch.root.right.left.right = sketch.root.left.right;
		try {
			if (!sketch.repOK())
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	private boolean testRepOK2() {
		sketch =  new BinaryTree_sketch(new int[] { 4,2,7,3,5,10 }) ;
		try {
			if (sketch.repOK())
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	protected boolean testHelper(int[] arr) {
		sketch = new BinaryTree_sketch(arr);
		// BinaryTree_base expect = new BinaryTree_expected(arr);

		try {
			if (sketch.repOK())
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public boolean test() {
		// if (!test1())
		// return false;
		// if (!test2())
		// return false;
		if (!testRepOK1()) return false;
//		if (!testRepOK2()) return false;
		return true;
	}

	public static void main(String[] a) {
		TestDriver driver = new TestDriver_BinaryTree();
		try {
			if (!driver.test())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			//npe.printStackTrace();
			//System.out.println( "*****BACKTRACKING: null pointer exception");
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
		//	System.out.println( re.getMessage());
			SketchRequest.backtrack();
		}
	//	System.out.println("****ALL TESTS PASSED!  Time: " + nf.format((System.nanoTime() - startTime) * 1.0 / Math.pow(10, 9)));
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}
}
