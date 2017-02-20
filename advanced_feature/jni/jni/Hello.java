class Hello {
    public native void sayHello();

    static {
        System.loadLibrary("hello");
    }

    public static void main(String[] args) {
        Hello h = new Hello();
        h.sayHello();
    }
}
