package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.dsl.NameRenderedEvent;
import io.determann.shadow.api.dsl.NameRenderer;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.RenderingContextBuilder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.unmodifiableList;

public class RenderingContextImpl
      implements RenderingContext
{
   private final NameRenderer nameRenderer;
   private final List<Consumer<NameRenderedEvent>> nameRenderedListeners;
   private final Deque<Object> surrounding;

   RenderingContextImpl(NameRenderer nameRenderer,
                        List<Consumer<NameRenderedEvent>> nameRenderedListeners,
                        Deque<Object> surrounding)
   {
      this.nameRenderer = nameRenderer;
      this.nameRenderedListeners = nameRenderedListeners;
      this.surrounding = surrounding;
   }

   @Override
   public RenderingContextBuilder builder()
   {
      return RenderingContext.renderingContextBuilder(this);
   }

   @Override
   public Deque<Object> getSurrounding()
   {
      return new ArrayDeque<>(surrounding);
   }

   @Override
   public void onNameRendered(Consumer<NameRenderedEvent> onNameRendered)
   {
      nameRenderedListeners.add(onNameRendered);
   }

   public NameRenderer getNameRenderer()
   {
      return nameRenderer;
   }

   public List<Consumer<NameRenderedEvent>> getNameRenderedListeners()
   {
      return unmodifiableList(nameRenderedListeners);
   }
}