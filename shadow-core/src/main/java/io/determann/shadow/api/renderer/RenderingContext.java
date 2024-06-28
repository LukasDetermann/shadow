package io.determann.shadow.api.renderer;

import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.internal.renderer.RenderingContextImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

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
            Package aPackage = requestOrThrow(declared, DECLARED_GET_PACKAGE);
            if (!requestOrThrow(aPackage, PACKAGE_IS_UNNAMED) && requestOrThrow(aPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang"))
            {
               return new NameRenderedEvent(declared, requestOrThrow(declared, Operations.NAMEABLE_NAME), false);
            }
            return new NameRenderedEvent(declared, requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME), true);
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
         nameRenderer = declared -> new NameRenderedEvent(declared, requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME), true);
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
         nameRenderer = declared -> new NameRenderedEvent(declared, requestOrThrow(declared, Operations.NAMEABLE_NAME), false);
         return this;
      }

      public RenderingContextImpl build()
      {
         return new RenderingContextImpl(nameRenderer, nameRenderedListeners);
      }
   }
}
