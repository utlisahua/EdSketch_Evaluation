package test.sketch4j.example.rbt;
public class Entry {
//	 private static final boolean RED   = false;
	    private static final boolean BLACK = true;
        public int key;
//        String value;
        public   Entry left = null;
        public   Entry right = null;
        public   Entry parent;
        public   boolean color = BLACK;

        Entry(int key, Entry  parent) {
            this.key = key;
//            this.value = value;
            this.parent = parent;
        }
        
        public int getKey() {
            return key;
        }

//        public String getValue() {
//            return value;
//        }
//
//        public String setValue(String value) {
//        	String oldValue = this.value;
//            this.value = value;
//            return oldValue;
//        }

        public boolean equals(Object o) {
            if (!(o instanceof Entry))
                return false;
			Entry e = (Entry)o;
		    return key==e.getKey() ;
//            return key==e.getKey() && value.equals(e.getValue());
        }

//        public int hashCode() {
//            int valueHash = (value==null ? 0 : value.hashCode());
//            return  valueHash;
//        }

//        public String toString() {
//            return key + "=" + value;
//        }
    }