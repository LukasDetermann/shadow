package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.impl.renderer.RenderingContextImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface RenderingContext
{
   public static RenderingContext DEFAULT = builder().withNamesWithoutNeedingImports().build();

   static Builder builder()
   {
      return new Builder();
   }

   static Builder builder(RenderingContext context)
   {
      Builder builder = new Builder();
      builder.nameRenderedListeners = context.getNameRenderedListeners();
      builder.nameRenderer = context.getNameRenderer();

      return builder;
   }

   String renderName(Declared declared);

   void onNameRendered(Consumer<NameRenderedEvent> onNameRendered);

   Function<Declared, NameRenderedEvent> getNameRenderer();

   List<Consumer<NameRenderedEvent>> getNameRenderedListeners();

   public static class Builder
   {
      private List<Consumer<NameRenderedEvent>> nameRenderedListeners = new ArrayList<>();

      private Function<Declared, NameRenderedEvent> nameRenderer;

      public Builder withNameRenderedListener(Consumer<NameRenderedEvent> nameRenderedListener)
      {
         this.nameRenderedListeners.add(nameRenderedListener);
         return this;
      }

      /**
       * all render methods produce qualified names except for the content of {@code java.lang}.
       * this is the default
       *
       * @see #withSimpleNames()
       * @see #withQualifiedNames()
       * @see #onNameRendered(Consumer)
       */
      public Builder withNamesWithoutNeedingImports()
      {
         nameRenderer = declared ->
         {
            if (!declared.getPackage().isUnnamed() && declared.getPackage().getQualifiedName().equals("java.lang"))
            {
               return new NameRenderedEvent(declared, declared.getName(), false);
            }
            return new NameRenderedEvent(declared, declared.getQualifiedName(), true);
         };
         return this;
      }

      /**
       * all render methods produce qualified names
       * default is {@link #withNamesWithoutNeedingImports()}
       *
       * @see #withSimpleNames()
       * @see #withNamesWithoutNeedingImports()
       * @see #onNameRendered(Consumer)
       */
      public Builder withQualifiedNames()
      {
         nameRenderer = declared -> new NameRenderedEvent(declared, declared.getQualifiedName(), true);
         return this;
      }

      /**
       * all render methods produce simple names.
       * default is {@link #withNamesWithoutNeedingImports()}
       *
       * @see #withQualifiedNames()
       * @see #withNamesWithoutNeedingImports()
       * @see #onNameRendered(Consumer)
       */
      public Builder withSimpleNames()
      {
         nameRenderer = declared -> new NameRenderedEvent(declared, declared.getName(), false);
         return this;
      }

      public RenderingContextImpl build()
      {
         return new RenderingContextImpl(nameRenderer, nameRenderedListeners);
      }
   }
}
