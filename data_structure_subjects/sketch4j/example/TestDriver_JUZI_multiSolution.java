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
 * 
 * @author lisahua
 *
 */
public abstract class TestDriver_JUZI_multiSolution extends TestDriver {
	// private static int SOLUTION_BOUND = 100;
	public static int counter = 0;
	public static int pruneCounter = 0;

	public TestDriver_JUZI_multiSolution() {
	}

	public TestDriver_JUZI_multiSolution(int[] arr) {
	}

	public abstract boolean test();

	public static void main(String[] a) {

		// try {
		//// PrintWriter writer = new PrintWriter("reverse_with.txt");
		// } catch (FileNotFoundException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		SketchRequest.setExecutor(ExecutorType.JUZI);
		long startTime = System.currentTimeMillis();
		do {
			try {
				SketchRequest.initialize();
				try {
					TestDriver_LinkedList_Reverse_Assign.main(a);
				} catch (BacktrackException e) {
					// System.out.println("****** JUZI BACKTRACK");
				}

			} catch (RuntimeException e) {
				System.out.println(
						"****ALL TESTS PASSED! SOLUTION  " + 
						
				nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)) +"  "
						+ SketchRequest.getString()
				+" ,"+
				TestDriver_LinkedList_Reverse_Assign.counter + ","+ (TestDriver_LinkedList_Reverse_Assign.counter-TestDriver_LinkedList_Reverse_Assign.pruneCounter) 
				 
						);
//				 System.out.println(TestDriver.getString());
			}
		} while (SketchRequest.getExecutor().incrementCounter());
//		System.out.println("Final " + counter + "," + counter + "\nTime "
//				+ nf.format((System.currentTimeMillis() - startTime) * 1.0 / Math.pow(10, 3)));
	}

}
