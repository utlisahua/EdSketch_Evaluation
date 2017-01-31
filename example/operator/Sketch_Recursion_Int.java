package test.sketch4j.example.operator;
import gov.nasa.jpf.jvm.Verify;


public class Sketch_Recursion_Int {
    // operators that can be sketched
    static enum ArithOp { PLUS, MINUS, TIMES, DIV, MOD };

     /* program to sketch; has one arithmetic operator hole */
        //@pre-condition: x >= y > 0
//    static int gcd ( int x , int y ){
//    if (y == 0)                        
//        return x;
//    else return expected_gcd ( \IV\ , \IV\ \AOP \IV\ );       
//}

    // expected result of sketching
    //@pre-condition: x>0, y > 0
    static int expected_gcd ( int x , int y ){
    if ( y == 0 )                        
        return x;
    else if (x >= y)
        return expected_gcd(y, x % y);
    else return expected_gcd (y , x);       
}
    static int rec_count = 0;
    static final int REC_BOUND = 10;

    // translated program to sketch
    // operator hole is replaced with method call
    static int sketchme_gcd (int x, int y) {
        {
            rec_count++;
            if (rec_count > REC_BOUND) Verify.ignoreIf(true);
        }
       if (y == 0) 
           return x;
        else if (x >= y && y > 0)
        return sketchme_gcd(y, _AOP_(0, x, y));
    else return sketchme_gcd(y, x);
    }
   
    static int[] _ivs_ = new int[]{-1, -1,-1};

    static int _IV_(int _v_, int[] vals, String[] vars) {
        if (_ivs_[_v_] == -1) {
            _ivs_[_v_] = Verify.getInt(0, vals.length - 1);
            System.out.println("INITIALIZATION: _ivs_[" + _v_ + "] = " + vars[_ivs_[_v_]]);
        }
        return vals[_ivs_[_v_]];
    }

    // handling one operator hole
    static ArithOp[] _aop_  = new ArithOp[]{null};

    static int _AOP_(int _op_, int x, int y) {
        if (_aop_[_op_]==null) {
            // this is the first time this operator is being used so
            // explore all possibilities
            int choice = Verify.getInt(0, 4);
            if (choice == 0) _aop_[_op_] = ArithOp.PLUS;
            else if (choice == 1) _aop_[_op_] = ArithOp.MINUS;
            else if (choice == 2) _aop_[_op_] = ArithOp.TIMES;
            else if (choice == 3)  _aop_[_op_] = ArithOp.DIV;  
             else   _aop_[_op_] = ArithOp.MOD; 
            System.out.println("INITIALIZATION: _aop_[" +_op_ +"] = "+_aop_[_op_]);
        }
        if (_aop_[_op_] == ArithOp.PLUS) return x + y;
        if (_aop_[_op_] == ArithOp.MINUS) return x - y; 
        if (_aop_[_op_] == ArithOp.TIMES) return x * y;   
        if (_aop_[_op_] == ArithOp.DIV) return x / y;
        if (_aop_[_op_] == ArithOp.MOD) return x % y;
        throw new RuntimeException("Unexpected error");
    }

    // test harness

    public static void main(String[] a) {
        Verify.resetCounter(0);
        Verify.resetCounter(1);
        Verify.resetCounter(2);
        Verify.resetCounter(3);
        
        // Test #1
        
          runTest(15,10, 0);
       

        // Test #2
        runTest(5,3, 1);

        // Test #3
        runTest(8,2, 2);

        // Test #4
       runTest(7,5, 3);

        System.out.println("****ALL TESTS PASSED!");

    }

    static void runTest(int x, int y, int counter) {
        rec_count = 0;
        int expected = expected_gcd(x, y);
        int actual = sketchme_gcd(x,y);
        boolean outcome = checkEq(expected, actual);
        String out = outcome ? "PASS" : "FAIL";
        System.out.println("[Test] expected: " + expected + ", actual: " + actual + " **" + out);
        Verify.incrementCounter(counter);
        System.out.println("counter#" + counter + ": " + Verify.getCounter(counter));
        System.out.println("rec_count: " + rec_count );
        if (!outcome) {
            System.out.println("BACKTRACKING: test failure");
            Verify.ignoreIf(true);
        }
    }

    static boolean checkEq(int x, int y) {
        return (x == y);
    }
}
