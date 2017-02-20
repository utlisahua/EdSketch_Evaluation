/**
 * @author Lisa Dec 1, 2016 TestDriver.java 
 */
package test.sketch4j.example;

import java.text.NumberFormat;

import sketch4j.executor.ExecutorType;
import sketch4j.executor.SketchExecutor;
import sketch4j.generator.SketchRequest;
import test.sketch4j.example.rbt.TestDriver_RBT_delete;

/**
 * While this can be replaced with JUNIT, I use main for simplicity.
 * 
 * @author lisahua
 *
 */
public abstract class TestDriver {

	public TestDriver() {
	}

	public TestDriver(int[] arr) {
	}

	public abstract boolean test();

	public static void main(String[] a) {
		
//		TestDriver driver = new TestDriver_FindMedian();
		SketchRequest.setExecutor(ExecutorType.JPF);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		long startTime = System.currentTimeMillis();
		try {
			TestDriver_RBT_delete.main(a);
//			if (!driver.test())
//				throw new RuntimeException("****TEST FAILURE");
//		} catch (NullPointerException npe) {
////			npe.printStackTrace();
//			System.out.println(driver.toString() + "*****BACKTRACKING: null pointer exception");
//			SketchRequest.backtrack();
		} catch (RuntimeException re) {
			System.out.println("****ALL TESTS PASSED!  Time: " + nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)));
			throw new RuntimeException("****FOUND FIRST SOLUTION");
		}
		System.out.println(SketchRequest.getString());
//		System.out.println("****ALL TESTS PASSED!  Time: " + nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)));
//		
	}

	public SketchExecutor SketchRequest() {
		return SketchRequest.getExecutor();
	}
	public static String getString() {
		return SketchRequest.getString();
	}
}
