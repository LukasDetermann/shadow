package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.dsl.NameRenderedEvent;
import io.determann.shadow.api.dsl.NameRenderer;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.RenderingContextBuilder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class RenderingContextBuilderImpl
      implements RenderingContextBuilder
{
   private final List<Consumer<NameRenderedEvent>> nameRenderedListeners = new ArrayList<>();
   private NameRenderer nameRenderer;
   private final Deque<Object> surrounding = new ArrayDeque<>();


   public RenderingContextBuilderImpl() {}

   public RenderingContextBuilderImpl(RenderingContext renderingContext)
   {
      this.nameRenderedListeners.addAll(renderingContext.getNameRenderedListeners());
      this.nameRenderer = renderingContext.getNameRenderer();
      this.surrounding.addAll(renderingContext.getSurrounding());
   }

   public RenderingContextBuilder withNameRenderedListener(Consumer<NameRenderedEvent> nameRenderedListener)
   {
      this.nameRenderedListeners.add(nameRenderedListener);
      return this;
   }

   @Override
   public RenderingContextBuilder addSurrounding(Object surrounding)
   {
      requireNonNull(surrounding);
      this.surrounding.addFirst(surrounding);
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
      return new RenderingContextImpl(nameRenderer, nameRenderedListeners, surrounding);
   }
}
