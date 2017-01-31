package test.sketch4j.example.reflection.dll;

import java.util.Set;

public class DoublyLinkedList_expected extends DoublyLinkedList {
	
    public DoublyLinkedList_expected(int[] is) {
		super(is);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
    public boolean repOk() {

        if (header == null)
            return false;
        if (header.next == null)
            return size == 0 && header.next == header
                    && header.prev == header;

        Set visited = new java.util.HashSet();
        visited.add(header);
        Node current = header;

        while (true) {
        	Node next = current.next;
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

}