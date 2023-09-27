package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.NameRenderedEvent;
import io.determann.shadow.api.shadow.Declared;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Context
{
   private final Function<Declared, NameRenderedEvent> nameRenderer;
   private final List<Consumer<NameRenderedEvent>> nameRenderedListeners;
   private boolean renderNestedGenerics = true;

   public Context(Function<Declared, NameRenderedEvent> nameRenderer, List<Consumer<NameRenderedEvent>> nameRenderedListeners)
   {
      this.nameRenderer = nameRenderer;
      this.nameRenderedListeners = nameRenderedListeners;
   }

   public String renderName(Declared declared)
   {
      NameRenderedEvent event = nameRenderer.apply(declared);
      nameRenderedListeners.forEach(listener -> listener.accept(event));

      return event.getName();
   }

   public boolean isRenderNestedGenerics()
   {
      return renderNestedGenerics;
   }

   public void setRenderNestedGenerics(boolean renderNestedGenerics)
   {
      this.renderNestedGenerics = renderNestedGenerics;
   }

   public static Builder builder()
   {
      return new Builder();
   }

   public static Builder builder(Context context)
   {
      Builder builder = new Builder();
      builder.nameRenderedListeners = context.nameRenderedListeners;
      builder.nameRenderer = context.nameRenderer;

      return builder;
   }

   public static class Builder
   {
      private List<Consumer<NameRenderedEvent>> nameRenderedListeners = new ArrayList<>();

      private Function<Declared, NameRenderedEvent> nameRenderer;

      public Builder withNameRenderedListener(Consumer<NameRenderedEvent> nameRenderedListener)
      {
         this.nameRenderedListeners.add(nameRenderedListener);
         return this;
      }

      public Builder withMostlyQualifiedNames()
      {
         nameRenderer = declared ->
         {
            if (!declared.getPackage().isUnnamed() && declared.getPackage().getQualifiedName().equals("java.lang"))
            {
               return new NameRenderedEvent(declared, declared.getSimpleName(), false);
            }
            return new NameRenderedEvent(declared, declared.getQualifiedName(), true);
         };
         return this;
      }


      public Builder withQualifiedNames()
      {
         nameRenderer = declared -> new NameRenderedEvent(declared, declared.getQualifiedName(), true);
         return this;
      }

      public Builder withSimpleNames()
      {
         nameRenderer = declared -> new NameRenderedEvent(declared, declared.getSimpleName(), false);
         return this;
      }

      public Context build()
      {
         return new Context(nameRenderer, nameRenderedListeners);
      }
   }
}