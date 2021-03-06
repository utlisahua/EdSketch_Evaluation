/**
 * @author Lisa Apr 20, 2016 BinaryTree.java 
 */
package test.sketch4j.example.reflection.bst;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import sketch4j.generator.SketchRequest;

public class BinaryTree_expected extends BinaryTree_base {
	public BinaryTree_expected(int[] arr) {
		super(arr);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return toString;
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
				if (!visited.add(current.right)) {
					return false;
				}
				workList.add(current.right);
			}
		}
		return visited.size() == size;
	}

}
