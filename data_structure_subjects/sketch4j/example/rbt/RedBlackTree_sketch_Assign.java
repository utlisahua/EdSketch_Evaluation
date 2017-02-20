package test.sketch4j.example.rbt;

import java.util.List;

import sketch4j.generator.SketchRequest;
import sketch4j.generator.SymbolicCandidate;
import sketch4j.generator.statement.AssignmentCandidate;

/**
 * @author Lisa Apr 18, 2016 TreeMap_6.java 
 */

/**
 * Adapted from jdk / openjdk / 6-b27 / java.util.TreeMap (@see <a href=
 * "http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b27/java/util/TreeMap.java">
 * TreeMap.java</a>).
 * <p>
 * I remove the generic type for simplicity. I remove the transient property for
 * simplicity. I assume the int 0 represents null in generic type.
 * </p>
 * 
 * @author lisahua
 *
 */
public class RedBlackTree_sketch_Assign {
	Entry root = null;
	int size = 0;
	public static final boolean RED = false;
	public static final boolean BLACK = true;
	private String toString = "";
	Entry _ln1_, _ln2_;
private final int ITER_BOUND = 11;
private int count=0;

	public void put(int key) {
		Entry t = root;
		if (t == null) {
			root = new Entry(key, null);
			size = 1;
			return;
		}

		Entry parent = null;

		int tmp = 0;
		count=0;
		do {
			if (++count>ITER_BOUND){TestDriver_RBT.prunCount++; SketchRequest.backtrack();}
			parent = t;
			tmp = key - t.key;

			if (key < t.key) {
				t = t.left;
			} else if (key > t.key) {
				t = t.right;
			} else
				return;

		} while (t != null );

		Entry e = new Entry(key, parent);
		if (tmp < 0)
			parent.left = e;
		else
			parent.right = e;
		fixAfterInsertion(e);
		size++;
	}

	/** From CLR */
	private void fixAfterInsertion(Entry x) {
		x.color = RED;
		count=0;
		while (x != null && x != root && x.parent.color == RED) {
			if (++count>ITER_BOUND) {TestDriver_RBT.prunCount++;SketchRequest.backtrack();}
			if ((x.parent == null || x.parent.parent == null || x.parent.parent.left == null)
					|| x.parent == x.parent.parent.left) {
				Entry y = rightOf(parentOf(parentOf(x)));
				if (colorOf(y) == RED) {
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					
					x = x.parent.parent;
				} else {
					if (x == x.parent.right) {
						x = x.parent;
						rotateLeft(x);
					}
					setColor(parentOf(x), BLACK);
					setColor(parentOf(parentOf(x)), RED);
					rotateRight(parentOf(parentOf(x)));
				}
			} else {
				Entry y = leftOf(parentOf(parentOf(x)));
				if (colorOf(y) == RED) {
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = x.parent.parent;
				} else {
					if (x == x.parent.left) {
						x = parentOf(x);
						rotateRight(x);
					}
					setColor(parentOf(x), BLACK);
					setColor(parentOf(parentOf(x)), RED);
					rotateLeft(parentOf(parentOf(x)));
				}
			}
		}
		root.color = BLACK;
	}

	private static boolean colorOf(Entry p) {
		return (p == null ? BLACK : p.color);
	}

	private static <K, V> Entry parentOf(Entry p) {
		return (p == null ? null : p.parent);
	}

	private static <K, V> void setColor(Entry p, boolean c) {
		if (p != null)
			p.color = c;
	}

	private static <K, V> Entry leftOf(Entry p) {
		return (p == null) ? null : p.left;
	}

	private static <K, V> Entry rightOf(Entry p) {
		return (p == null) ? null : p.right;
	}

	/** From CLR */
	private void rotateLeft(Entry p) {
		if (p != null) {
			Entry r = p.right;
			{
				_ln1_ = p;
				_ln2_ = r;
			}
			 _BLOCK_(SketchRequest.queryBlock(_ln1_.getClass(), new String[] { "p", "r" }, new Entry[] { _ln1_, _ln2_ },
						1, 2, 0));
//			_ln1_.right = _ln2_.left;
			if (_ln2_.left != null) {
				_ln2_.left.parent = _ln1_;
			}
			_ln2_.parent = _ln1_.parent;
//			_BLOCK_(SketchRequest.queryBlock(_ln1_.getClass(), new String[] { "p", "r" }, new Entry[] { _ln1_, _ln2_ },
//					1, 2, 1));
			if (_ln1_.parent == null) {
				root = _ln2_;
			} else if (_ln1_.parent.left == _ln1_) {
				_ln1_.parent.left = _ln2_;
//				_BLOCK_(SketchRequest.queryBlock(_ln1_.getClass(), new String[] { "p", "r" }, new Entry[] { _ln1_, _ln2_ },
//						1, 2, 2));
			} else {
//				_BLOCK_(SketchRequest.queryBlock(_ln1_.getClass(), new String[] { "p", "r" }, new Entry[] { _ln1_, _ln2_ },
//						1, 2, 3));
				_ln1_.parent.right = _ln2_;
			}
			_ln2_.left = _ln1_;
//			_BLOCK_(SketchRequest.queryBlock(_ln1_.getClass(), new String[] { "p", "r" }, new Entry[] { _ln1_, _ln2_ },
//					1, 2, 4));
			_ln1_.parent = _ln2_;
		}
	}

	/** From CLR */
	private void rotateRight(Entry p) {
		if (p != null) {
			Entry l = p.left;
			p.left = l.right;
			if (l.right != null) {
				l.right.parent = p;
			}
			l.parent = p.parent;
			if (p.parent == null) {
				root = l;
			} else if (p.parent.right == p) {
				p.parent.right = l;
			} else {
				p.parent.left = l;
			}
			l.right = p;
			p.parent = l;
		}
	}

	private void assignment(AssignmentCandidate assign, AssignmentCandidate prev, int varLen) {
		
		int lid = assign.getJPFLHS();
		int rid = assign.getJPFRHS();
		toString += SketchRequest.toString(assign) + "   ";
		int[] vids = null;
		if (prev == null)
			vids = new int[] { varLen, lid, rid };
		else
			vids = new int[] { varLen, lid, rid, prev.getLHS(), prev.getRHS() };
		if (SketchRequest.notValid(vids)) {
			TestDriver_RBT.prunCount++;
			SketchRequest.backtrack();
		}
		// SJValueCandidate c = candidates[rid];
		// if (c.isNPE())
		// Verify.ignoreIf(true);
		// Entry rhs = (Entry) c.getValue();
		Entry rhs = getRHS(assign.getJPFRHS());
		switch (lid) {
		case 0:
			_ln1_ = rhs;
			break;
		case 1:
			_ln2_ = rhs;
			break;
		case 2:
			_ln1_.left = rhs;
			break;
		case 3:
			_ln1_.right = rhs;
			break;
		case 4:
			_ln1_.parent = rhs;
			break;
		case 5:
			_ln2_.left = rhs;
			break;
		case 6:
			_ln2_.right = rhs;
			break;
		case 7:
			_ln2_.parent = rhs;
			break;
		case 8:
			_ln1_.left.left = rhs;
			break;
		case 9:
			_ln1_.left.right = rhs;
			break;
		case 10:
			_ln1_.left.parent = rhs;
			break;
		case 11:
			_ln1_.right.left = rhs;
			break;
		case 12:
			_ln1_.right.right = rhs;
		case 13:
			_ln1_.right.parent = rhs;
			break;
		case 14:
			_ln1_.parent.left = rhs;
			break;
		case 15:
			_ln1_.parent.right = rhs;
			break;
		case 16:
			_ln1_.parent.parent = rhs;
			break;
		case 17:
			_ln2_.left.left = rhs;
			break;
		case 18:
			_ln2_.left.right = rhs;
			break;
		case 19:
			_ln2_.left.parent = rhs;
			break;
		case 20:
			_ln2_.right.left = rhs;
			break;
		case 21:
			_ln2_.right.right = rhs;
			break;
		case 22:
			_ln2_.right.parent = rhs;
			break;
		case 23:
			_ln1_.parent.left = rhs;
			break;
		case 24:
			_ln1_.parent.right = rhs;
			break;
		case 25:
			_ln1_.parent.parent = rhs;
			break;
		}
	}

	private Entry getRHS(int rid) {
		switch (rid) {
		case 0:
			return _ln1_;
		case 1:
			return _ln2_;
		case 2:
			return _ln1_.left;
		case 3:
			return _ln1_.right;
		case 4:
			return _ln1_.parent;
		case 5:
			return _ln2_.left;
		case 6:
			return _ln2_.right;
		case 7:
			return _ln2_.parent;
		case 8:
			return _ln1_.left.left;
		case 9:
			return _ln1_.left.right;
		case 10:
			return _ln1_.left.parent;
		case 11:
			return _ln1_.right.left;
		case 12:
			return _ln1_.right.right;
		case 13:
			return _ln1_.right.parent;
		case 14:
			return _ln1_.parent.left;
		case 15:
			return _ln1_.parent.right;
		case 16:
			return _ln1_.parent.parent;
		case 17:
			return _ln2_.left.left;
		case 18:
			return _ln2_.left.right;
		case 19:
			return _ln2_.left.parent;
		case 20:
			return _ln2_.right.left;
		case 21:
			return _ln2_.right.right;
		case 22:
			return _ln2_.right.parent;
		case 23:
			return _ln1_.parent.left;
		case 24:
			return _ln2_.parent.right;
		case 25:
			return _ln1_.parent.parent;
		}
		return null;

	}

	public void _BLOCK_(List<SymbolicCandidate> cand) {
		toString = "";
		for (int i = 0; i < cand.size(); i++) {
			switch (cand.get(i).getType()) {
			case ASSIGNMENT:
				assignment((AssignmentCandidate) cand.get(i), (i == 0) ? null : (AssignmentCandidate) cand.get(i - 1),
						2);
				break;
			default:
				break;
			}

		}
//		System.out.println(toString);
	}

	public void remove(int key) {
		Entry p = contains(key);
		if (p == null)
			return;
		deleteEntry(p);
	}

	public Entry contains(int key) {

		Entry p = root;
		while (p != null) {
			int cmp = key - p.key;
			if (cmp < 0)
				p = p.left;
			else if (cmp > 0)
				p = p.right;
			else
				return p;
		}
		return null;
	}

	private void deleteEntry(Entry p) {
		size--;

		// If strictly internal, copy successor's element to p and then make p
		// point to successor.
		if (p.left != null && p.right != null) {
			Entry s = successor(p);
			p.key = s.key;
			p = s;
		} // p has 2 children

		// Start fixup at replacement node, if it exists.
		Entry replacement = (p.left != null ? p.left : p.right);

		if (replacement != null) {
			// Link replacement to parent
			replacement.parent = p.parent;
			if (p.parent == null)
				root = replacement;
			else if (p == p.parent.left)
				p.parent.left = replacement;
			else
				p.parent.right = replacement;

			// Null out links so they are OK to use by fixAfterDeletion.
			p.left = p.right = p.parent = null;

			// Fix replacement
			if (p.color == BLACK)
				fixAfterDeletion(replacement);
		} else if (p.parent == null) { // return if we are the only node.
			root = null;
		} else { // No children. Use self as phantom replacement and unlink.
			if (p.color == BLACK)
				fixAfterDeletion(p);

			if (p.parent != null) {
				if (p == p.parent.left)
					p.parent.left = null;
				else if (p == p.parent.right)
					p.parent.right = null;
				p.parent = null;
			}
		}
	}

	/** From CLR */
	private void fixAfterDeletion(Entry x) {
		while (x != root && colorOf(x) == BLACK) {
			if (x == x.parent.left) {
				Entry sib = rightOf(parentOf(x));

				if (colorOf(sib) == RED) {
					setColor(sib, BLACK);
					setColor(parentOf(x), RED);
					rotateLeft(parentOf(x));
					sib = rightOf(parentOf(x));
				}

				if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
					setColor(sib, RED);
					x = parentOf(x);
				} else {
					if (colorOf(rightOf(sib)) == BLACK) {
						setColor(leftOf(sib), BLACK);
						setColor(sib, RED);
						rotateRight(sib);
						sib = rightOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(rightOf(sib), BLACK);
					rotateLeft(parentOf(x));
					x = root;
				}
			} else { // symmetric
				Entry sib = leftOf(parentOf(x));

				if (colorOf(sib) == RED) {
					setColor(sib, BLACK);
					setColor(parentOf(x), RED);
					rotateRight(parentOf(x));
					sib = leftOf(parentOf(x));
				}

				if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
					setColor(sib, RED);
					x = parentOf(x);
				} else {
					if (colorOf(leftOf(sib)) == BLACK) {
						setColor(rightOf(sib), BLACK);
						setColor(sib, RED);
						rotateLeft(sib);
						sib = leftOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(leftOf(sib), BLACK);
					rotateRight(parentOf(x));
					x = root;
				}
			}
		}

		setColor(x, BLACK);
	}

	/**
	 * Returns the successor of the specified Entry, or null if no such.
	 */
	private static Entry successor(Entry t) {
		if (t == null)
			return null;
		else if (t.right != null) {
			Entry p = t.right;
			while (p.left != null)
				p = p.left;
			return p;
		} else {
			Entry p = t.parent;
			Entry ch = t;
			while (p != null && ch == p.right) {
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}

	public String toString() {
		return toString;
	}
}
