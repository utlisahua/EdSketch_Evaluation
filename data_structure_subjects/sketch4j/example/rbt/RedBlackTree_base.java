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
public class RedBlackTree_base {
	Entry root = null;
	int size = 0;
	public static final boolean RED = false;
	public static final boolean BLACK = true;

	public void put(int key) {
		Entry t = root;
		if (t == null) {
			root = new Entry(key,  null);
			size = 1;
			return;
		}
	
		Entry parent;
		int tmp =0;
		do {
			parent = t;
			tmp = key-t.key;
			if (key < t.key)
				t = t.left;
			else if (key > t.key)
				t = t.right;
			else
				return;
		} while (t != null);

		Entry e = new Entry(key, parent);
		if (tmp<0)
			parent.left = e;
		else
			parent.right = e;
		fixAfterInsertion(e);
		size++;
	}

	/** From CLR */
	private void fixAfterInsertion(Entry x) {
		x.color = RED;

		while (x != null && x != root && x.parent.color == RED) {
			if ( (x.parent==null || x.parent.parent==null ||x.parent.parent.left==null) || x.parent == x.parent.parent.left ) {
				Entry y = rightOf(parentOf(parentOf(x)));
				if (colorOf(y) == RED) {
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = parentOf(parentOf(x));
				} else {
					if (x == x.parent.right) {
						x = parentOf(x);
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
					x = parentOf(parentOf(x));
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
			p.right = r.left;
			if (r.left != null)
				r.left.parent = p;
			r.parent = p.parent;
			if (p.parent == null)
				root = r;
			else if (p.parent.left == p)
				p.parent.left = r;
			else
				p.parent.right = r;
			r.left = p;
			p.parent = r;
		}
	}

	/** From CLR */
	private void rotateRight(Entry p) {
		if (p != null) {
			Entry l = p.left;
			p.left = l.right;
			if (l.right != null)
				l.right.parent = p;
			l.parent = p.parent;
			if (p.parent == null)
				root = l;
			else if (p.parent.right == p)
				p.parent.right = l;
			else
				p.parent.left = l;
			l.right = p;
			p.parent = l;
		}
	}

	public void remove(int key) {
		Entry p = contains(key);
		if (p == null)
			return ;
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
