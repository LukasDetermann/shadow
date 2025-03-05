package io.determann.shadow.api.renderer;

public interface MethodRenderer
{
   /**
    * {@code   public static void main(String[] args) {}}
    */
   String declaration(RenderingContext renderingContext);

   /**
    * <pre>{@code
    *   public static void main(String[] args)
    *    {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(RenderingContext renderingContext, String content);

   /**
    * {@code main()}
    */
   String invocation(RenderingContext renderingContext);

   /**
    * {@code main(parameters)}
    */
   String invocation(RenderingContext renderingContext, String parameters);
}
