package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RenderingContextImpl
      implements RenderingContext
{
   private final NameRenderer nameRenderer;
   private final List<Consumer<NameRenderedEvent>> nameRenderedListeners;
   private final Map<RenderingContextOption<?>, Object> options = new HashMap<>();

   RenderingContextImpl(NameRenderer nameRenderer,
                        List<Consumer<NameRenderedEvent>> nameRenderedListeners,
                        Map<RenderingContextOption<?>, Object> options)
   {
      this.nameRenderer = nameRenderer;
      this.nameRenderedListeners = nameRenderedListeners;
      this.options.putAll(options);
   }

   @Override
   public RenderingContextBuilder builder()
   {
      return RenderingContext.renderingContextBuilder(this);
   }

   @Override
   public boolean hasOption(RenderingContextOption<?> option)
   {
      return options.containsKey(option);
   }

   @Override
   public <T> T getOption(RenderingContextOption<T> option)
   {
      //noinspection unchecked
      return (T) options.get(option);
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
      return new ArrayList<>(nameRenderedListeners);
   }

   Map<RenderingContextOption<?>, Object> getOptions()
   {
      return options;
   }
}