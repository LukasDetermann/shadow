package io.determann.shadow.api.renderer;

public interface InterfaceRenderer
{
   /**
    * {@code public interface MyInterface {}}
    */
   String declaration();

   /**
    * <pre>{@code
    *    public interface MyInterface {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(String content);

   /**
    * {@code private final} <b>MyInterface</b> {@code myClass;}
    */
   String type();
}
