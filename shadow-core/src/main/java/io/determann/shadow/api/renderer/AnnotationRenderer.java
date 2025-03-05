package io.determann.shadow.api.renderer;

public interface AnnotationRenderer
{
   /**
    * {@code public @interface MyAnnotation {}}
    */
   String declaration(RenderingContext renderingContext);

   /**
    * <pre>{@code
    *    public @interface MyAnnotation {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(RenderingContext renderingContext, String content);

   /**
    * {@code private final} <b>MyAnnotation</b> {@code myAnnotation;}
    */
   String type(RenderingContext renderingContext);
}
