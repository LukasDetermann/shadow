package io.determann.shadow.api.renderer;

import io.determann.shadow.internal.renderer.RenderingContextBuilderImpl;

import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public interface RenderingContext
{
   RenderingContext DEFAULT = renderingContextBuilder().withNamesWithoutNeedingImports().build();

   static RenderingContextBuilder renderingContextBuilder()
   {
      return new RenderingContextBuilderImpl();
   }

   /// creates a copy to modify
   static RenderingContextBuilder renderingContextBuilder(RenderingContext context)
   {
      requireNonNull(context);
      return new RenderingContextBuilderImpl(context);
   }

   /// @see RenderingContextBuilder#withNameRenderedListener(Consumer)
   RenderingContextBuilder builder();

   Deque<Object> getSurrounding();

   void onNameRendered(Consumer<NameRenderedEvent> onNameRendered);

   List<Consumer<NameRenderedEvent>> getNameRenderedListeners();

   NameRenderer getNameRenderer();
}
