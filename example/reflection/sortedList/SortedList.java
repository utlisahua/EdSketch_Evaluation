package test.sketch4j.example.reflection.sortedList;

import java.util.List;
import java.util.Set;

import sketch4j.generator.SketchRequest;
import sketch4j.generator.SymbolicCandidate;
import sketch4j.generator.statement.AssignmentCandidate;
import test.sketch4j.example.reflection.dll.Node;

@SuppressWarnings("unchecked")
public class SortedList {
	public Node header;
	public int size = 0;
	Node _e_;

	private void assignment(AssignmentCandidate assign, AssignmentCandidate prev, int varLen) {
		int lid = assign.getJPFLHS();
		int rid = assign.getJPFRHS();

		int[] vids = null;
		if (prev == null)
			vids = new int[] { varLen, lid, rid };
		else
			vids = new int[] { varLen, lid, rid, prev.getLHS(), prev.getRHS() };
		if (SketchRequest.notValid(vids)) {
			SketchRequest.backtrack();
		}
		Node rhs = getRHS(assign.getJPFRHS());
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
			header.next = rhs;
			break;
		}
	}

	
	
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
			current = next;
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
		current =  header.next;
		while (current.next != header) {
			if (current.val > current.next.val)
				return false;
			current = current.next ;
		}
		return true;
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
			return header.next;
		}
		return null;

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
}