package test.sketch4j.example.reflection.sortedList;

import java.util.Set;

import sketch4j.generator.SketchRequest;

@SuppressWarnings("unchecked")
public class SortedList_sketch extends SortedList {
	public Node header;
	public int size = 0;

	public boolean repOK() throws Exception {
		// check cyclicity
		if (header == null)
			return false;
		Set visited = new java.util.HashSet();
		visited.add(header);
		Node current = header;
		while (true) {
			Node next = (Node) Node.class.getDeclaredField("next").get(current);
			if (next == null)
				return false;
			_BLOCK_(SketchRequest.queryBlock(Node.class, new String[] { "current", "header" },
					new Object[] { _e_, header }, 1, 1, 0));
			// current = next;
			if (!visited.add(next))
				break;
		}
		if (current != header)
			return false; // maybe not needed
		// check size
		Object obj = Set.class.getDeclaredMethod("size").invoke(visited);
		if (obj != null && (int) obj != size)
			return false;
		// check ordering
		if (header.next != header)
			return false;
		_BLOCK_(SketchRequest.queryBlock(Node.class, new String[] { "current", "header" }, new Object[] { _e_, header },
				1, 1, 1));
		// current = header.next;

		boolean cond = SketchRequest.queryCondition(Node.class, new String[] { "current", "header" },
				new Object[] { _e_, header }, 1, 1, true, 0);
		while (cond) {
			// while (current.next != header) {
			if (current.element > current.next.element)
				return false;
			// current = current.next ;
			_BLOCK_(SketchRequest.queryBlock(Node.class, new String[] { "current", "header" },
					new Object[] { _e_, header }, 1, 1, 2));
			cond = SketchRequest.queryCondition(Node.class, new String[] { "current", "header" },
					new Object[] { _e_, header }, 1, 1, true, 0);
		}
		return true;
	}
}