package io.determann.shadow.api.renderer;

public interface FieldRenderer
{
   /**
    * {@code private final String myField;}
    */
   String declaration(RenderingContext renderingContext);
}
