package io.determann.shadow.api.renderer;

public interface MethodRenderer
{
   /**
    * {@code   public static void main(String[] args) {}}
    */
   String declaration();

   /**
    * <pre>{@code
    *   public static void main(String[] args)
    *    {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(String content);

   /**
    * {@code main()}
    */
   String invocation();

   /**
    * {@code main(parameters)}
    */
   String invocation(String parameters);
}
