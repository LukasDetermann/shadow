package io.determann.shadow.api.renderer;

public interface ArrayRenderer
{
   /**
    * {@code int[]}
    */
   String type(RenderingContext renderingContext);

   /**
    * {@code new int[5][2]}
    */
   String initialisation(RenderingContext renderingContext, int... dimensions);
}
