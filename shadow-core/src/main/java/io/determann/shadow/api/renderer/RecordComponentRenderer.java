package io.determann.shadow.api.renderer;

public interface RecordComponentRenderer
{
   String declaration(RenderingContext renderingContext);

   String invocation(RenderingContext renderingContext);
}
