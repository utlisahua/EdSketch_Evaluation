package test.sketch4j.example.reflection.dll;

import java.util.List;
import java.util.Set;

import sketch4j.generator.SketchRequest;
import sketch4j.generator.SymbolicCandidate;
import sketch4j.generator.statement.AssignmentCandidate;

public class DoublyLinkedList {

	public Node header;
	int size = 0;
	Node _e_;

	public DoublyLinkedList(int[] is) {
		Node n = header;
		header.next = header;
		header.prev = header;
		for (int i : is) {
			n.next = new Node(i);
			n.next.prev = n;
			n.next.next = header;
			n = n.next;
			header.prev = n;
		}
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

		while (true) {
			Node next = (Node) Node.class.getField("next").get(current);
			if (next == null)
				return false;
			if (next.prev != current)
				return false;
			current = next;
			if (!visited.add(next))
				break;
		}
		if (current != header)
			return false; // // maybe not needed (also in SortedList.java)
		if (visited.size() != size)
			return false;
		return true;
	}

	public void _BLOCK_(List<SymbolicCandidate> cand) {

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
	}

	private void assignment(AssignmentCandidate assign, AssignmentCandidate prev, int len) {

		int rid = assign.getJPFRHS();
		int lid = assign.getJPFLHS();
		// toString += SketchRequest.toString(assign) + " ";
		// System.out.println(lid+" "+rid);
		int[] vids = null;
		if (prev == null)
			vids = new int[] { len, lid, rid };
		else
			vids = new int[] { len, lid, rid, prev.getLHS(), prev.getRHS() };
		if (SketchRequest.notValid(vids))
			SketchRequest.backtrack();
		Node rhs = getRHS(rid);
		switch (lid) {
		case 0:
			_e_ = rhs;
			break;
		case 1:
			header = rhs;
			break;
		case 2:
			_e_.next = rhs;
			break;
		case 3:
			_e_.prev = rhs;
			break;
		case 4:
			header.next = rhs;
			break;
		case 5:
			header.prev = rhs;
			break;
		case 6:
			_e_.next.next = rhs;
			break;
		case 7:
			_e_.next.prev = rhs;
			break;
		case 8:
			_e_.prev.next = rhs;
			break;
		case 9:
			_e_.prev.prev = rhs;
			break;
		case 10:
			header.next.next = rhs;
			break;
		case 11:
			header.next.prev = rhs;
			break;
		case 12:
			header.prev.next = rhs;
			break;
		case 13:
			header.prev.prev = rhs;
			break;
		}

	}

	private Node getRHS(int rid) {
		switch (rid) {
		case 0:
			return _e_;
		case 1:
			return header;
		case 2:
			return _e_.next;
		case 3:
			return _e_.prev;
		case 4:
			return header.next;
		case 5:
			return header.prev;
		case 6:
			return _e_.next.next;
		case 7:
			return _e_.next.prev;
		case 8:
			return _e_.prev.next;
		case 9:
			return _e_.prev.prev;
		case 10:
			return header.next.next;
		case 11:
			return header.next.prev;
		case 12:
			return header.prev.next;
		case 13:
			return header.prev.prev;
		}
		return null;
		// return (Node)SketchRequest.getCandidate(rid).getValue();
	}

}