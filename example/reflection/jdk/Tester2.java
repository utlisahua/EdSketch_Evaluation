package test.sketch4j.example.reflection.jdk;

import java.lang.reflect.Method;

class Tester2 {
    public static int f() { return 0; }
    public static void main(String[] args) throws Exception {
        Method m = Tester2.class.getMethod("f");
        Object o1 = m.invoke(null);
        Object o2 = m.invoke(null);
        if (o1 != o2)
            throw new Error(System.identityHashCode(o1) +
                            " " + System.identityHashCode(o2));
    }
}