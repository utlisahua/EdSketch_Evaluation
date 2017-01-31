package test.sketch4j.example.operator;
import gov.nasa.jpf.jvm.Verify;


class Sketch4 {
        // operators that can be sketched

        static enum ArithOp { PLUS, MINUS, TIMES, DIV };

        // /* program to sketch; has one arithmetic operator hole */
        // static int sketchme(int x, int y) {
        //     int result = 1;
        //         for (int i = 0; i < 1001; i++) {
        //             result += \IV\ \AOP\ \IV\;
        //     }
        //     result += complexfunction(x, y); 
        //     return result;
        // }

        // expected result of sketching -- 
        static int expected(int x, int y) {
                int result = 1;
                for (int i = 0; i < 1001; i++) {
		            result += x - y;//we try to sketch x-y input ;: nothing result +=    
                }
                return result;
        }

        // translated program to sketch
        // operator hole is replaced with method call
        static int sketchme(int x, int y) {
                int result = 1;
                for (int i = 0; i < 1001; i++) {
		    result +=
		    		_AOP_(0, _IV_(0, new int[]{x, y}, new String[]{"x", "y"}),  _IV_(1, new int[]{x, y}, new String[]{"x", "y"}));
        
                }
                return result;
        }

    static int[] _ivs_ = new int[]{-1, -1};

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
                        int choice = Verify.getInt(0, 3);
                        if (choice == 0) _aop_[_op_] = ArithOp.PLUS;
                        else if (choice == 1) _aop_[_op_] = ArithOp.MINUS;
                        else if (choice == 2) _aop_[_op_] = ArithOp.TIMES;
                        else  _aop_[_op_] = ArithOp.DIV;      
                    System.out.println("INITIALIZATION: _aop_[" +_op_ +"] = "+_aop_[_op_]);
                }
                if (_aop_[_op_] == ArithOp.PLUS) return x+y;
                if (_aop_[_op_] == ArithOp.MINUS) return x-y; 
                if (_aop_[_op_] == ArithOp.TIMES) return x*y;   
                if (_aop_[_op_] == ArithOp.DIV) return x/y;  
                
              throw new RuntimeException("Unexpected error");
            }
           
        // test harness

        public static void main(String[] a) {
                // Test #1
                runTest(1,1);

                // Test #2
                runTest(2,1);

                // Test #3
                runTest(1,2);

                // Test #4
                runTest(10,15);
                System.out.println("****ALL TESTS PASSED!");
            
        }

        static void runTest(int x, int y) {
                int expected = expected(x, y);
                int actual = sketchme(x,y);
                boolean outcome = checkEq(expected, actual);
                String out = outcome ? "PASS" : "FAIL";
                System.out.println("[Test] expected: " + expected + ", actual: " + actual + " **" + out);
                if (!outcome) {
                        System.out.println("BACKTRACKING: test failure");
                        Verify.ignoreIf(true);
                }
        }

        static boolean checkEq(int x, int y) {
                return (x == y);
        }
}
