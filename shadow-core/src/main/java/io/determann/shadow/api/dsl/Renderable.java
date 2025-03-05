package io.determann.shadow.api.dsl;

import io.determann.shadow.api.renderer.RenderingContext;

public interface Renderable
{
   String render(RenderingContext renderingContext);
}
