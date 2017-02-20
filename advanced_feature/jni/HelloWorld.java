class HelloWorld {
    public native void print();  //native method
    static   //static initializer code
    {
        System.loadLibrary("helloworld");
    } 

    public static void main(String[] args)
    {
        HelloWorld hw = new HelloWorld();
        hw.print();
    }
}
