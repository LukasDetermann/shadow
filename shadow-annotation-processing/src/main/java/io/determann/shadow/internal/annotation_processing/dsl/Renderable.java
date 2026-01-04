package io.determann.shadow.internal.annotation_processing.dsl;


import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

public interface Renderable
{
   /// Renders this using the {@link RenderingContext}.
   /// A representation of java code can be rendered or {@link Renderable}s crated via the {@link Dsl}.
   ///
   /// If this is not created via the {@link Dsl} there are often multiple different way to render it, like a Method declaration or a Method invocation.
   /// In that case a declaration will be rendered.
   ///
   /// @see RenderingContext
   /// @see Dsl
   String render(RenderingContext renderingContext);
}
