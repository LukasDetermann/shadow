package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.renderer.RenderingContext;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/// implementation note: a bit overkill, but minimises bug potential
interface DslSupport
{
   static <INSTANCE> INSTANCE addTypeRenderer(INSTANCE instance,
                                              String string,
                                              Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      return addTypeRenderer(instance, renderingContext -> string, toAddToSupplier);
   }

   static <INSTANCE, FROM> INSTANCE addTypeRenderer(INSTANCE instance,
                                                    FROM type,
                                                    BiFunction<RenderingContext, FROM, String> renderer,
                                                    Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      return addTypeRenderer(instance, renderingContext -> renderer.apply(renderingContext, type), toAddToSupplier);
   }

   static <INSTANCE> INSTANCE addTypeRenderer(INSTANCE instance,
                                              Renderable renderable,
                                              Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      requireNonNull(renderable);

      Consumer<Renderable> renderingConsumer = toAddToSupplier.apply(instance);

      renderingConsumer.accept(renderable);

      return instance;
   }

   static <INSTANCE> INSTANCE addArrayRenderer(INSTANCE instance,
                                               String[] strings,
                                               Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      return addArrayRenderer(instance, strings, (renderingContext, s) -> s, toAddToSupplier);
   }

   static <INSTANCE, TYPE> INSTANCE addArray2(INSTANCE instance,
                                              TYPE[] types,
                                              BiConsumer<INSTANCE, TYPE> toAddToSupplier)
   {
      requireNonNull(instance);

      for (TYPE type : types)
      {
         requireNonNull(type);
         toAddToSupplier.accept(instance, type);
      }
      return instance;
   }

   static <INSTANCE, TYPE> INSTANCE addArray2(INSTANCE instance,
                                              Collection<TYPE> types,
                                              BiConsumer<INSTANCE, TYPE> toAddToSupplier)
   {
      requireNonNull(instance);

      for (TYPE type : types)
      {
         requireNonNull(type);
         toAddToSupplier.accept(instance, type);
      }
      return instance;
   }

   static <INSTANCE, FROM> INSTANCE addArrayRenderer(INSTANCE instance,
                                                     List<FROM> types,
                                                     BiFunction<RenderingContext, FROM, String> renderer,
                                                     Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      requireNonNull(types);

      Consumer<Renderable> renderingConsumer = toAddToSupplier.apply(instance);

      for (FROM type : types)
      {
         requireNonNull(type);
         renderingConsumer.accept(renderingContext -> renderer.apply(renderingContext, type));
      }

      return instance;
   }

   static <INSTANCE, FROM> INSTANCE addArrayRenderer(INSTANCE instance,
                                                     FROM[] types,
                                                     BiFunction<RenderingContext, FROM, String> renderer,
                                                     Function<INSTANCE, Consumer<Renderable>> toAddToSupplier)
   {
      requireNonNull(types);

      Consumer<Renderable> renderingConsumer = toAddToSupplier.apply(instance);

      for (FROM type : types)
      {
         requireNonNull(type);
         renderingConsumer.accept(renderingContext -> renderer.apply(renderingContext, type));
      }

      return instance;
   }


   static <INSTANCE> INSTANCE setTypeRenderer(INSTANCE instance,
                                              String string,
                                              BiConsumer<INSTANCE, Renderable> toAddToSupplier)
   {
      return setTypeRenderer(instance, string, (renderingContext, o) -> o, toAddToSupplier);
   }

   static <INSTANCE, FROM> INSTANCE setTypeRenderer(INSTANCE instance,
                                                    FROM type,
                                                    BiFunction<RenderingContext, FROM, String> renderer,
                                                    BiConsumer<INSTANCE, Renderable> toAddToSupplier)
   {
      requireNonNull(type);

      toAddToSupplier.accept(instance, renderingContext -> renderer.apply(renderingContext, type));

      return instance;
   }

   static <INSTANCE, TYPE> INSTANCE setType(INSTANCE instance,
                                            TYPE type,
                                            BiConsumer<INSTANCE, TYPE> toAddToSupplier)
   {
      requireNonNull(type);

      toAddToSupplier.accept(instance, type);

      return instance;
   }

   static void renderElement(StringBuilder sb,
                             List<Renderable> renderers,
                             String after,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      renderElement(sb, "", renderers, after, renderingContext, delimiter);
   }

   static void renderElement(StringBuilder sb,
                             List<Renderable> renderers,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      renderElement(sb, "", renderers, "", renderingContext, delimiter);
   }

   static void renderElement(StringBuilder sb,
                             String before,
                             List<Renderable> renderers,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      renderElement(sb, before, renderers, "", renderingContext, delimiter);
   }

   static void renderElement(StringBuilder sb,
                             String before,
                             List<Renderable> renderers,
                             String after,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (!renderers.isEmpty())
      {
         sb.append(before);
         sb.append(renderers.stream().map(renderer -> renderer.render(renderingContext)).collect(joining(delimiter)));
         sb.append(after);
      }
   }
}
