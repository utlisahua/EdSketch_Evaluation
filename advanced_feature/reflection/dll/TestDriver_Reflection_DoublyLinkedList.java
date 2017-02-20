/**
 * @author Lisa Dec 1, 2016 TestDriver.java 
 */
package test.sketch4j.example.reflection.dll;

import sketch4j.generator.SketchRequest;
import test.sketch4j.example.TestDriver;

/**
 * While this can be replaced with JUNIT, I use main for simplicity.
 * 
 * @author lisahua
 *
 */
public class TestDriver_Reflection_DoublyLinkedList extends TestDriver {
	DoublyLinkedList list;

	private boolean testRepOK_prevLoop() {
		list = new DoublyLinkedList(new int[] { 3, 2, 1 });
		list.header.next.prev  = list.header.next.next.next;
		boolean res = false;
		try {
			res = list.repOk();
		} catch (Exception e) {
			return false;
		}
		return res==false;
	}

	private boolean testRepOK_prevSelfLoop() {
		list = new DoublyLinkedList(new int[] { 3, 2, 1 });
		list.header.next.next.prev = list.header.next.next;
		boolean res = false;
		try {
			res = list.repOk();
		} catch (Exception e) {
			return false;
		}
		return res==false;
	}

	private boolean testRepOK_nextLoop() {
		list = new DoublyLinkedList(new int[] { 3, 2, 1 });
		list.header.next.prev = list.header.next.next.next;
		boolean res = false;
		try {
			res = list.repOk();
		} catch (Exception e) {
			return false;
		}
		return res==false;
	}

	private boolean testRepOK_nextSelfLoop() {
		list = new DoublyLinkedList(new int[] { 3, 2, 1 });
		list.header.next.next = list.header.next;
		list.header.prev = list.header.next.next.next;
		boolean res = false;
		try {
			res = list.repOk();
		} catch (Exception e) {
			return false;
		}
		return res==false;
	}

	private boolean testRepOK_noLoop() {
		list = new DoublyLinkedList(new int[] { 3, 2, 1 });
		boolean res = false;
		try {
			res = list.repOk();
		} catch (Exception e) {
			return false;
		}
		return res;
	}

	@Override
	public boolean test() {
		if (!testRepOK_prevLoop())
			return false;
		if (!testRepOK_prevSelfLoop())
			return false;
		if (!testRepOK_nextLoop())
			return false;
		if (!testRepOK_nextSelfLoop())
			return false;
		if (!testRepOK_noLoop())
			return false;
		return true;
	}

	public static void main(String[] a) {
		TestDriver driver = new TestDriver_Reflection_DoublyLinkedList();
		try {
			if (!driver.test())
				throw new RuntimeException("****TEST FAILURE");
		} catch (NullPointerException npe) {
			// npe.printStackTrace();
			// System.out.println( "*****BACKTRACKING: null pointer exception");
			SketchRequest.backtrack();
		} catch (RuntimeException re) {
			// System.out.println( re.getMessage());
			SketchRequest.backtrack();
		}
		// System.out.println("****ALL TESTS PASSED! Time: " +
		// nf.format((System.nanoTime() - startTime) * 1.0 / Math.pow(10, 9)));
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}
}
