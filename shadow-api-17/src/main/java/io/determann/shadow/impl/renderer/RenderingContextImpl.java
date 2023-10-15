package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.NameRenderedEvent;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Declared;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RenderingContextImpl implements RenderingContext
{
   private final Function<Declared, NameRenderedEvent> nameRenderer;
   private final List<Consumer<NameRenderedEvent>> nameRenderedListeners;

   public RenderingContextImpl(Function<Declared, NameRenderedEvent> nameRenderer, List<Consumer<NameRenderedEvent>> nameRenderedListeners)
   {
      this.nameRenderer = nameRenderer;
      this.nameRenderedListeners = nameRenderedListeners;
   }

   @Override
   public String renderName(Declared declared)
   {
      NameRenderedEvent event = nameRenderer.apply(declared);
      nameRenderedListeners.forEach(listener -> listener.accept(event));

      return event.getName();
   }

   @Override
   public void onNameRendered(Consumer<NameRenderedEvent> onNameRendered)
   {
      nameRenderedListeners.add(onNameRendered);
   }

   public Function<Declared, NameRenderedEvent> getNameRenderer()
   {
      return nameRenderer;
   }

   public List<Consumer<NameRenderedEvent>> getNameRenderedListeners()
   {
      return nameRenderedListeners;
   }
}