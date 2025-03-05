package io.determann.shadow.api.renderer;

public interface WildcardRenderer
{
   /**
    * {@code List<}<b>? extends Number</b>{@code >}
    */
   String type(RenderingContext renderingContext);
}
