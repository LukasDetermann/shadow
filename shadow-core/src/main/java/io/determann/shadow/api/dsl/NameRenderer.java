package io.determann.shadow.api.dsl;

public interface NameRenderer
{
   String render(RenderingContext context, String packageName, String simpleName);
}
