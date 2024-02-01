package io.determann.shadow.api.renderer;

public interface ClassRenderer
{
   /**
    * {@code public class MyClass {}}
    */
   String declaration();

   /**
    * <pre>{@code
    *    public class MyClass {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(String content);

   /**
    * {@code private final} <b>MyClass</b> {@code myClass;}
    */
   String type();
}
