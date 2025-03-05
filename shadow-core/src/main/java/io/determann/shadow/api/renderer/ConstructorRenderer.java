package io.determann.shadow.api.renderer;

public interface ConstructorRenderer
{
   /**
    * {@code public MyClass() {}}
    */
   String declaration(RenderingContext renderingContext);

   /**
    * <pre>{@code
    *    public MyClass() {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(RenderingContext renderingContext, String content);

   /**
    * {@code new MyClass()}
    */
   String invocation(RenderingContext renderingContext);

   /**
    * {@code new MyClass(parameters)}
    */
   String invocation(RenderingContext renderingContext, String parameters);
}
