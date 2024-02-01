package io.determann.shadow.api.renderer;

public interface ConstructorRenderer
{
   /**
    * {@code public MyClass() {}}
    */
   String declaration();

   /**
    * <pre>{@code
    *    public MyClass() {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(String content);

   /**
    * {@code new MyClass()}
    */
   String invocation();

   /**
    * {@code new MyClass(parameters)}
    */
   String invocation(String parameters);
}
