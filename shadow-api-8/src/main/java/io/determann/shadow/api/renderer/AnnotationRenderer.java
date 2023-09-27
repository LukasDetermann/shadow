package io.determann.shadow.api.renderer;

public interface AnnotationRenderer
{
   /**
    * {@code public @interface MyAnnotation {}}
    */
   String declaration();

   /**
    * <pre>{@code
    *    public @interface MyAnnotation {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(String content);

   /**
    * {@code private final} <b>MyAnnotation</b> {@code myAnnotation;}
    */
   String type();
}
