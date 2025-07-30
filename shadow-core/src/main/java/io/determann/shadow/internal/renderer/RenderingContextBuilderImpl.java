package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class RenderingContextBuilderImpl
      implements RenderingContextBuilder
{
   private final List<Consumer<NameRenderedEvent>> nameRenderedListeners = new ArrayList<>();
   private NameRenderer nameRenderer;
   private final Map<RenderingContextOption<?>, Object> options = new HashMap<>();


   public RenderingContextBuilderImpl() {}

   public RenderingContextBuilderImpl(RenderingContext renderingContext)
   {
      this.nameRenderedListeners.addAll(renderingContext.getNameRenderedListeners());
      this.nameRenderer = renderingContext.getNameRenderer();
      this.options.putAll(((RenderingContextImpl) renderingContext).getOptions());
   }

   public RenderingContextBuilder withNameRenderedListener(Consumer<NameRenderedEvent> nameRenderedListener)
   {
      this.nameRenderedListeners.add(nameRenderedListener);
      return this;
   }

   @Override
   public <T> RenderingContextBuilder withOption(RenderingContextOption<T> option, T value)
   {
      requireNonNull(option);
      this.options.put(option, value);
      return this;
   }

   public RenderingContextBuilder withNamesWithoutNeedingImports()
   {
      nameRenderer = (context, packageName, simpleName) ->
      {
         NameRenderedEvent event = switch (packageName)
         {
            case "java.lang" -> new NameRenderedEvent(packageName, simpleName, simpleName, false);
            case null, default -> new NameRenderedEvent(packageName, simpleName, packageName + '.' + simpleName, true);
         };
         context.getNameRenderedListeners().forEach(listener -> listener.accept(event));

         return event.result();
      };
      return this;
   }

   public RenderingContextBuilder withQualifiedNames()
   {
      nameRenderer = (context, packageName, simpleName) ->
      {
         NameRenderedEvent event = new NameRenderedEvent(packageName, simpleName, packageName + '.' + simpleName, true);
         context.getNameRenderedListeners().forEach(listener -> listener.accept(event));
         return event.result();
      };
      return this;
   }

   public RenderingContextBuilder withSimpleNames()
   {
      nameRenderer = (context, packageName, simpleName) ->
      {
         NameRenderedEvent event = new NameRenderedEvent(packageName, simpleName, simpleName, false);
         context.getNameRenderedListeners().forEach(listener -> listener.accept(event));
         return event.result();
      };
      return this;
   }

   public RenderingContext build()
   {
      return new RenderingContextImpl(nameRenderer, nameRenderedListeners, options);
   }
}
