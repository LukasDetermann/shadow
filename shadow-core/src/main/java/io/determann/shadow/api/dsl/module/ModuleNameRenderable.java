package io.determann.shadow.api.dsl.module;

import io.determann.shadow.api.renderer.RenderingContext;

public interface ModuleNameRenderable
{
   String renderQualifiedName(RenderingContext renderingContext);
}
