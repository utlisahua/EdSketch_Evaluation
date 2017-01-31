package test.sketch4j.example.reflection.dll;

import java.util.Set;

import sketch4j.generator.SketchRequest;

public class DoublyLinkedList_sketch extends DoublyLinkedList {

	public DoublyLinkedList_sketch(int[] is) {
		super(is);
	}

	@SuppressWarnings("unchecked")
	public boolean repOk() throws Exception {

		if (header == null)
			return false;
		if (header.next == null)
			return size == 0 && header.next == header && header.prev == header;

		Set visited = new java.util.HashSet();
		visited.add(header);
		Node current = header;

		{
			_e_ = current;
		}
		while (true) {
			Node next = (Node) Node.class.getField("next").get(current);
			if (next == null)
				return false;
			boolean cond = SketchRequest.queryCondition(Node.class, new String[] { "current", "header" },
					new Object[] { _e_, header }, 1, 1, true, 0);
			// if (next.prev != _e_)
			if (cond)
				return false;
			// _e_ = next;
			_BLOCK_(SketchRequest.queryBlock(Node.class, new String[] { "current", "header" },
					new Object[] { _e_, header }, 1, 1, 0));
			if (!visited.add(next))
				break;
		}
		if (_e_ != header)
			return false; // // maybe not needed (also in SortedList.java)
		if (visited.size() != size)
			return false;
		return true;
	}

}