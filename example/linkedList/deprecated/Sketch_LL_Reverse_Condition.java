package test.sketch4j.example.linkedList.deprecated;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.jpf.jvm.Verify;
import sketch4j.generator.expression.ConditionValuePruning;
import sketch4j.validator.old.pruningRules.InterStmtPruning2;
import sketch4j.validator.old.pruningRules.LazyInitPrune2;
import test.sketch4j.example.linkedList.Entry;
import test.sketch4j.example.linkedList.LinkedList;

public class Sketch_LL_Reverse_Condition {

	// expected result of sketching. Assume no loop in LL
	private static LinkedList expected_reverse(LinkedList l) {
		if (l.head == null)
			return l;
		Entry ln1 = l.head;
		Entry ln2 = l.head.next;
		Entry ln3 = null;
		Entry ln4 = null;
		while (ln2 != null) {
			ln4 = ln2.next;
			ln1.next = ln3;
			ln3 = ln1;
			ln1 = ln2;
			ln2 = ln4;
		}
		l.head = ln1;
		ln1.next = ln3;
		return l;
	}

	private static int iter_count = 0;
	private final static int ITER_BOUND = 11;

	// translated program to sketch
	// operator hole is replaced with method call
	private static LinkedList sketchme_reverse(LinkedList l) {
		if (l.head == null)
			return l;
		Entry ln1 = l.head;
		Entry ln2 = l.head.next;
		Entry ln3 = null;
		Entry ln4 = null;
		{
			_ln1_ = ln1;
			_ln2_ = ln2;
			_ln3_ = ln3;
			_ln4_ = ln4;
			_ll_ = l;
		}
		while (_ln2_ != null) {
//		while (_EXP_(0)) {
			// while (_ln2_ != null) {
			if (iter_count++ > ITER_BOUND)
				Verify.ignoreIf(true);
			_ln1_.next = _ln3_ ;
			_ln3_ = _ln1_;
			_ln1_ = _ln2_; 
			_ln2_ = _ln2_.next;
//			 _ln4_ = _ln2_.next;
//			 _ln1_.next = _ln3_
//			 _ln3_ = _ln1_;
//			 _ln1_ = _ln2_;
//			 _ln2_ = _ln4_;
//			_LHS_(0);
//			_LHS_(1);
//			_LHS_(2);
//			_LHS_(3);
//			_LHS_(4);
		}
		//... if (can_swap) { if (Verify.getBoolean()) { swap(); System.out.println("SWAP"); } } 
		_ll_.head = _ln1_;
		_ln1_.next = _ln3_;
		return _ll_;
	}
	private static List<LazyInitPrune2> exprHole = new ArrayList<LazyInitPrune2>();
//	private static List<StmtPruningStrategy<Entry>> exprHole = new ArrayList<StmtPruningStrategy<Entry>>();
	private static List<ConditionValuePruning> condHoles = new ArrayList<ConditionValuePruning>();
	private static final String[] names = { "ln1", "ln2", "ln3", "ln4", "ln1.next", "ln2.next", "ln3.next", "ln4.next",
			"null" };
	private static final int len = names.length;
	static LinkedList _ll_;
	static Entry _ln1_, _ln2_, _ln3_, _ln4_;

	private static boolean _EXP_(int c) {
		while (c >= condHoles.size()) {
			condHoles.add(new ConditionValuePruning(names));
		}
		ConditionValuePruning cond = condHoles.get(c);
		return  cond.prune(new Entry[] { _ln1_, _ln2_, _ln3_, _ln4_ });
	}

	private static Entry[] genVals(Entry[] vals) {
		int varLen = vals.length;
		Entry[] cur = new Entry[len];
		for (int i = 0; i < varLen; i++) {
			cur[i] = vals[i];
			cur[i + varLen] = (vals[i] == null ? null : vals[i].getNext());
		}
		cur[2 * varLen] = null;
		return cur;
	}

	private static void _LHS_(int l) {
		while (l >= exprHole.size()) {
			exprHole.add(new LazyInitPrune2(names));
//			exprHole.add(new LazyInitPrune<Entry>(names));
//			 exprHole.add(new GreedyPrune<Entry>(names));
		}
		LazyInitPrune2 stmt = exprHole.get(l);
//		StmtPruningStrategy<Entry> stmt = exprHole.get(l);
//		int lhs = stmt.lhs();
		Entry rhs = (Entry) stmt.prune(new Entry[] { _ln1_, _ln2_, _ln3_, _ln4_ });
//		Entry rhs = (Entry) stmt.prune(genVals(new Entry[] { _ln1_, _ln2_, _ln3_, _ln4_ }));
		if (new InterStmtPruning2(stmt, (l == 0 ? null : exprHole.get(l - 1))).prune()) {
			Verify.ignoreIf(true);
		}
		switch (stmt.getLhs()) {
		case 0:
			_ln1_ = rhs;
			break;
		case 1:
			_ln2_ = rhs;
			break;
		case 2:
			_ln3_ = rhs;
			break;
		case 3:
			_ln4_ = rhs;
			break;
		case 4:
			_ln1_.next = rhs;
			break;
		case 5:
			_ln2_.next = rhs;
			break;
		case 6:
			_ln3_.next = rhs;
			break;
		case 7:
			_ln4_.next = rhs;
			break;
		}
	}

	// test harness

	public static void main(String[] a) {
		Verify.resetCounter(0);
		Verify.resetCounter(1);
		Verify.resetCounter(2);
		Verify.resetCounter(3);
		Verify.resetCounter(4);
		Verify.resetCounter(5);
		try {
			// Test #1
			runTest(createList(new int[] {}), createList(new int[] {}), 0);

			// Test #2
			runTest(createList(new int[] { 1 }), createList(new int[] { 1 }), 1);

			// Test #3
			runTest(createList(new int[] { 1, 2 }), createList(new int[] { 1, 2 }), 2);

			// Test #4
			runTest(createList(new int[] { 1, 2, 3 }), createList(new int[] { 1, 2, 3 }), 3);

			// Test #5
			runTest(createList(new int[] { 1, 2, 3, 4, 5, 6 }), createList(new int[] { 1, 2, 3, 4, 5, 6 }), 4);

			// Test #6
			runTest(createList(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }),
					createList(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }), 5);
		} catch (NullPointerException npe) {
//			 npe.printStackTrace();
			// print();
			System.out.println("BACKTRACKING: null pointer exception");
			Verify.ignoreIf(true);
		}
		System.out.println("****ALL TESTS PASSED!");
		print();
		throw new RuntimeException("****FOUND FIRST SOLUTION");
	}

	private static LinkedList createList(int[] arr) {
		LinkedList l = new LinkedList();
		if (arr.length == 0)
			return l;
		l.head = new Entry(arr[0]);
		Entry p = l.head;
		for (int i = 1; i < arr.length; i++) {
			p.next = new Entry(arr[i]);
			p = p.next;
		}
		return l;
	}

	private static void runTest(LinkedList l, LinkedList l_copy, int counter) {
		iter_count = 0;
		LinkedList expected = expected_reverse(l);
		LinkedList actual = sketchme_reverse(l_copy);
		boolean outcome = checkEq(expected, actual);
		Verify.incrementCounter(counter);
		System.out.println("counter#" + counter + ": " + Verify.getCounter(counter));
		if (!outcome) {
			print();
			System.out.println("BACKTRACKING: test failure");
			Verify.ignoreIf(true);
		}
	}

	private static void print() {
		for (LazyInitPrune2 stmt : exprHole) {
			System.out.println(" -- " + stmt.toString());
		}
		for (ConditionValuePruning c : condHoles) {
			System.out.println(" -- while (" + c.toString() + ")");
		}
	}

	private static boolean checkEq(LinkedList x, LinkedList y) {
		if (x == null && y == null)
			return true;
		Entry xh = x.head, yh = y.head;
		for (; xh != null && yh != null; xh = xh.next, yh = yh.next) {
			if (xh.val != yh.val) {
				return false;
			}
		}
		return xh == null && yh == null;
	}
}
