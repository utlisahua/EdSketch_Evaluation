/**
 * @author Lisa Dec 1, 2016 TestDriver.java 
 */
package test.sketch4j.example;

import java.text.NumberFormat;

import juzi.BacktrackException;
import sketch4j.executor.ExecutorType;
import sketch4j.generator.SketchRequest;
import test.sketch4j.example.linkedList.testDriver.TestDriver_LinkedList_Reverse_Assign;;

/**
 * While this can be replaced with JUNIT, I use main for simplicity.
 * @author lisahua
 */
public abstract class TestDriver_JUZI extends TestDriver {
	public TestDriver_JUZI() {
	}

	public TestDriver_JUZI(int[] arr) {
	}

	public abstract boolean test();

	public static void main(String[] a) {
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		SketchRequest.setExecutor(ExecutorType.JUZI);
		long startTime = System.currentTimeMillis();
		try {
			do {
				SketchRequest.initialize();
				try {
					TestDriver_LinkedList_Reverse_Assign.main(a);
				} catch (BacktrackException e) {
					//System.out.println("****** JUZI BACKTRACK");
				}
			} while (SketchRequest.getExecutor().incrementCounter());
		} catch (RuntimeException e) {
//			System.out.println("****ALL TESTS PASSED!  Time: "
//					+ nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)));
			System.out.println(TestDriver.getString());
			System.out.println(
//					"****ALL TESTS PASSED! SOLUTION" + ++count + "  Time: "
					 nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)) +","
					+
					TestDriver_LinkedList_Reverse_Assign.counter + ","+ (TestDriver_LinkedList_Reverse_Assign.counter-TestDriver_LinkedList_Reverse_Assign.pruneCounter) 
					 );
//			System.out.println(TestDriver.getString());
		}

	}

}
