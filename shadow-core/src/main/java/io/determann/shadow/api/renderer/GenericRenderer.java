package io.determann.shadow.api.renderer;

public interface GenericRenderer
{
   /**
    * {@code public class MyClass<} <b>T extends Number</b> {@code >}
    */
   String declaration(RenderingContext renderingContext);

   /**
    * {@code private final} <b>T</b> {@code myPojo;}
    */
   String type(RenderingContext renderingContext);
}
