package io.determann.shadow.api.renderer;

import java.util.function.Consumer;

public interface RenderingContextBuilder
{
   RenderingContextBuilder withNameRenderedListener(Consumer<NameRenderedEvent> nameRenderedListener);

   default RenderingContextBuilder withOption(RenderingContextOption<?> option)
   {
      return withOption(option, null);
   }

   <T> RenderingContextBuilder withOption(RenderingContextOption<T> option, T value);

   /**
    * all render methods produce qualified names except for the content of {@code java.lang}.
    * this is the default
    *
    * @see #withSimpleNames()
    * @see #withQualifiedNames()
    * @see RenderingContext#onNameRendered(Consumer)
    */
   RenderingContextBuilder withNamesWithoutNeedingImports();

   /**
    * all render methods produce qualified names
    * default is {@link #withNamesWithoutNeedingImports()}
    *
    * @see #withSimpleNames()
    * @see #withNamesWithoutNeedingImports()
    * @see RenderingContext#onNameRendered(Consumer)
    */
   RenderingContextBuilder withQualifiedNames();

   /**
    * all render methods produce simple names.
    * default is {@link #withNamesWithoutNeedingImports()}
    *
    * @see #withQualifiedNames()
    * @see #withNamesWithoutNeedingImports()
    * @see RenderingContext#onNameRendered(Consumer)
    */
   RenderingContextBuilder withSimpleNames();

    RenderingContext build();
}
