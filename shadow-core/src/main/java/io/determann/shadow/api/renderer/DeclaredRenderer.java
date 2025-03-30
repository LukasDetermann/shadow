package io.determann.shadow.api.renderer;

public interface DeclaredRenderer
{
   /**
    * {@code public class MyClass {}}
    */
   String declaration(RenderingContext renderingContext);

   /**
    * <pre>{@code
    *    public class MyClass {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(RenderingContext renderingContext, String content);

   /**
    * {@code private final} <b>MyClass</b> {@code myClass;}
    */
   String type(RenderingContext renderingContext);
}
