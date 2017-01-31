/**
 * @author Lisa Jan 24, 2017 TestDriver_FindMedian.java 
 */
package test.sketch4j.example.rbt;

import sketch4j.generator.SketchRequest;
import test.sketch4j.example.TestDriver;

public class TestDriver_RBT extends TestDriver {
public static int count= 0;
public static int prunCount= 0;
	public static void main(String[] arg) {
		int[][] arr = { {1,0,2,3 }, {1,0,3,2 }, {2,0,1,3 }, { 2,1,0,3 },{ 1,0,2 },{0,1},{1,0}  };
count++;
		// TestDriver_JUZI_multiSolution.counter++;
		try {
			for (int[] a : arr) {
				RedBlackTree_base expect = new RedBlackTree_base();
				RedBlackTree_sketch_Assign sketch = new RedBlackTree_sketch_Assign();
				for (int i : a) {
					expect.put(i);
					sketch.put(i);
				}
				if (!checkEq(expect.root, sketch.root))
					throw new RuntimeException("******Test Failure");
			}
		} catch (NullPointerException npe) {
//			 npe.printStackTrace();
			// System.out.println( "*****BACKTRACKING: null pointer exception");
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
//			 System.out.println( re.getMessage());
			SketchRequest.backtrack();
		}
		System.out.println(count+","+(count-prunCount));
		// System.out.println("****ALL TESTS PASSED! Time: " +
		// nf.format((System.currentTimeMillis() - startTime) * 1.0 /
		// Math.pow(10, 3)));
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}

	@Override
	public boolean test() {

		return false;
	}

	private static boolean checkEq(Entry expect, Entry sketch) {
		if (expect == null && sketch == null)
			return true;
		if (expect == null || sketch == null || expect.key != sketch.key || expect.color != sketch.color)
			return false;
		return checkEq(expect.left, sketch.left) && checkEq(expect.right, sketch.right);
	}

}
