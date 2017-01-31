package test.sketch4j.example.rbt;

import sketch4j.generator.SketchRequest;

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
public class RedBlackTree_sketch {
	Entry root = null;
	int size = 0;
	public static final boolean RED = false;
	public static final boolean BLACK = true;

	public void put(int key) {
		Entry t = root;
		if (t == null) {
			root = new Entry(key, null);
			size = 1;
			return;
		}

		Entry parent = null;
		boolean cond3 = SketchRequest.queryCondition(Entry.class, new String[] { "root", "parent", "t" },
				new Object[] { root, parent, t }, 1, 1, true, 0);
		int tmp = 0;
		do {
			parent = t;
			tmp = key - t.key;
			boolean cond1 = SketchRequest.queryCondition(int.class, new String[] { "root", "parent", "t", "key" },
					new Object[] { root, parent, t, key }, 1, 1, false, 1);
			boolean cond2 = SketchRequest.queryCondition(int.class, new String[] { "root", "parent", "t", "key" },
					new Object[] { root, parent, t, key }, 1, 1, false, 2);
//			 if (key < t.key)
			if (cond1)
				t = t.left;
//			 else if (key > t.key)
			else if (cond2)
				t = t.right;
			else
				return;
			cond3 = SketchRequest.queryCondition(Entry.class, new String[] { "root", "parent", "t" },
					new Object[] { root, parent, t }, 1, 1, true, 0);
		} while (cond3);

		Entry e = new Entry(key, parent);
		// boolean cond4 = SketchRequest.queryCondition(int.class, new String[]
		// { "root","parent","t","key"},
		// new Object[] { root,parent,t,key }, 1, 1,false, 3);
		// if (cond4)
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
//		 boolean cond1 = SketchRequest.queryCondition(boolean.class, new
//		 String[] { "root","x"},
//		 new Object[] { root,x }, 2, 1,false, 3);
		// boolean cond2 = SketchRequest.queryCondition(Entry.class, new
		// String[] { "root","x"},
		// new Object[] { root,x }, 3, 1,true, 4);
		boolean cond3 = SketchRequest.queryCondition(Entry.class, new String[] { "root", "x" },
				new Object[] { root, x }, 2, 1, true, 3);
		boolean cond4 = SketchRequest.queryCondition(Entry.class, new String[] { "root", "x" },
				new Object[] { root, x }, 2, 1, true, 4);

		while (x != null && x != root && x.parent.color == RED) {
			// while (x != null && x != root && cond1) {

			if ((x.parent == null || x.parent.parent == null || x.parent.parent.left == null)
					|| x.parent == x.parent.parent.left) {
				// if ( (x.parent==null || x.parent.parent==null
				// ||x.parent.parent.left==null) || cond2 ) {
				Entry y = rightOf(parentOf(parentOf(x)));
				if (colorOf(y) == RED) {
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = x.parent.parent;
				} else {
//					 if (x==x.parent.right) {
					if (cond3) {
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
//					 if (x==x.parent.left) {
					if (cond4) {
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
//		boolean cond1 = SketchRequest.queryCondition(Entry.class, new String[] { "root", "x" },
//				new Object[] { root, p }, 2, 1, true, 5);
//		boolean cond2 = SketchRequest.queryCondition(Entry.class, new String[] { "root", "x" },
//				new Object[] { root, p }, 2, 1, true, 6);
//		boolean cond3 = SketchRequest.queryCondition(Entry.class, new String[] { "root", "x" },
//				new Object[] { root, p }, 2, 1, true, 7);
		// boolean cond4 = SketchRequest.queryCondition(Entry.class, new
		// String[] { "root","x"},
		// new Object[] { root,p }, 2, 1,true, 8);
		 if (p!=null) {
//		if (cond1) {
			Entry r = p.right;
			p.right = r.left;
			 if (r.left !=null) {
//			if (cond2) {
				r.left.parent = p;
			}
			r.parent = p.parent;
			 if (p.parent==null) {
//			if (cond3) {
				root = r;
			} else if (p.parent.left == p) {
				// else if (cond4)
				p.parent.left = r;
			} else {
				p.parent.right = r;
			}
			r.left = p;
			p.parent = r;
		}
	}

	/** From CLR */
	private void rotateRight(Entry p) {
		// boolean cond1 = SketchRequest.queryCondition(Entry.class, new
		// String[] { "root","x"},
		// new Object[] { root,p }, 2, 1,true, 12);
		// boolean cond2 = SketchRequest.queryCondition(Entry.class, new
		// String[] { "root","x"},
		// new Object[] { root,p }, 2, 1,true, 13);
		// boolean cond3 = SketchRequest.queryCondition(Entry.class, new
		// String[] { "root","x"},
		// new Object[] { root,p }, 2, 1,true, 14);
		// boolean cond4 = SketchRequest.queryCondition(Entry.class, new
		// String[] { "root","x"},
		// new Object[] { root,p }, 2, 1,true, 15);
		if (p != null) {
			// if (cond1) {
			Entry l = p.left;
			p.left = l.right;
			if (l.right != null) {
				// if (cond2)
				l.right.parent = p;
			}
			l.parent = p.parent;
			if (p.parent == null) {
				// if (cond3)
				root = l;
			} else if (p.parent.right == p) {
				// } else if (cond4)
				p.parent.right = l;
			} else {
				p.parent.left = l;
			}
			l.right = p;
			p.parent = l;
		}
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

}
