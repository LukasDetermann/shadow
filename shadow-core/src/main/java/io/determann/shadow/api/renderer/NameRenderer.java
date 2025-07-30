package io.determann.shadow.api.renderer;

public interface NameRenderer
{
   String render(RenderingContext context, String packageName, String simpleName);
}
