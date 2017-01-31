/**
 * @author Lisa Jan 27, 2017 JNICompare.java 
 */
package test.sketch4j.example.jni;

public class JNICompare {

	 public native boolean intMethod(int i,int j);
	    public static void main(String[] args) {
	        System.loadLibrary("JNICompare");
	        if (!new JNICompare().intMethod(1,2)) throw new RuntimeException("******Test Failure");
	        if (new JNICompare().intMethod(2,2)) throw new  RuntimeException("******Test Failure");
	    }
	    
	    
}
