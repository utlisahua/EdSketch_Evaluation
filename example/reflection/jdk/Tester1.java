package test.sketch4j.example.reflection.jdk;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

public class Tester1
{
  public final static Class[] EMPTY_PARAMETERS = {};

  public static void main( String argv[] ) throws Exception
  {
// This works perfectly 
  
    {
      HashMap source = new HashMap();
      Set obj = source.keySet();
      System.out.println( obj.iterator() );
    }

// Here is what does not work
  
    {
      Object source = new HashMap();
      Method source_method = source.getClass().getMethod( "keySet", EMPTY_PARAMETERS );
      Object obj = source_method.invoke( source, EMPTY_PARAMETERS );
      Method obj_method = obj.getClass().getMethod( "iterator", EMPTY_PARAMETERS );
      System.out.println( obj_method.invoke( obj, EMPTY_PARAMETERS ) );
    }
  }
}
