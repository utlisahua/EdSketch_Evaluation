/**
 * @author Lisa Apr 20, 2016 BinaryTree.java 
 */
package test.sketch4j.example.reflection.bst;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import sketch4j.generator.SketchRequest;

public class BinaryTree_base {
	public Node root;
	public int size;
	protected Node _n1_, _n2_;
	public static final int ITER_BOUND = 11;
	protected String toString = "";

	public String toString() {
		return toString;
	}

	public BinaryTree_base(int[] arr) {
		for (int i : arr)
			insert(i);
	}

	public boolean repOK() throws Exception {
		if (root == null)
			return size == 0;
		Set<Node> visited = new HashSet<Node>();
		visited.add(root);
		LinkedList<Node> workList = new LinkedList<Node>();
		workList.add(root);

		while (!workList.isEmpty()) {
			Node current = workList.removeFirst();
			if (Node.class.getDeclaredField("left").get(current) != null) {
				if (!visited.add(current.left))
					return false;
				workList.add(current.left);
			}

			{
				_n1_ = current;
			}
			if (current.right != null) {
				if (!visited.add(current.right))
					return false;
				workList.add(current.right);
			}
		}
		return visited.size() == size;
	}

	private boolean insert(int k) {
		Node y = null;
		Node x = root;

		while ((x != null)) {
			y = x;

			if (x.key == k) {
				return false;
			}

			if ((x != null) && k < x.key) {
				x = x.left;
			}

			else if ((x != null) && (k > x.key)) {
				x = x.right;
			}
		}

		x = new Node(k);

		if (y == null) {
			root = x;
		} else {
			if (k < y.key) {
				y.left = x;
			} else {
				y.right = x;
			}
		}
		size += 1;
		return true;
	}

}
