package io.determann.shadow.api.renderer;

public interface ParameterRenderer
{
   /**
    * {@code public static void main( }<b>String[] args</b> {@code ) {}}
    */
   String declaration(RenderingContext renderingContext);
}
