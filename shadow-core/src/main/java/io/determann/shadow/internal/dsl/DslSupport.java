package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.renderer.RenderingContext;

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
   static <INSTANCE, TYPE> INSTANCE addTypeRenderer(INSTANCE instance,
                                                    TYPE string,
                                                    Function<INSTANCE, Consumer<Function<RenderingContext, TYPE>>> toAddToSupplier)
   {
      return addTypeRenderer(instance, string, (renderingContext, s) -> s, toAddToSupplier);
   }


   static <INSTANCE, FROM, TO> INSTANCE addTypeRenderer(INSTANCE instance,
                                                        FROM type,
                                                        BiFunction<RenderingContext, FROM, TO> renderer,
                                                        Function<INSTANCE, Consumer<Function<RenderingContext, TO>>> toAddToSupplier)
   {
      requireNonNull(type);

      Consumer<Function<RenderingContext, TO>> renderingConsumer = toAddToSupplier.apply(instance);

      requireNonNull(type);
      renderingConsumer.accept(renderingContext -> renderer.apply(renderingContext, type));

      return instance;
   }

   static <INSTANCE> INSTANCE addArrayRenderer(INSTANCE instance,
                                               String[] strings,
                                               Function<INSTANCE, Consumer<Function<RenderingContext, String>>> toAddToSupplier)
   {
      return addArrayRenderer(instance, strings, (renderingContext, s) -> s, toAddToSupplier);
   }

   static <INSTANCE, FROM, TO> INSTANCE addArrayRenderer(INSTANCE instance,
                                                         FROM[] types,
                                                         BiFunction<RenderingContext, FROM, TO> renderer,
                                                         Function<INSTANCE, Consumer<Function<RenderingContext, TO>>> toAddToSupplier)
   {
      requireNonNull(types);

      Consumer<Function<RenderingContext, TO>> renderingConsumer = toAddToSupplier.apply(instance);

      for (FROM type : types)
      {
         requireNonNull(type);
         renderingConsumer.accept(renderingContext -> renderer.apply(renderingContext, type));
      }

      return instance;
   }

   static <INSTANCE, TYPE> INSTANCE addArray(INSTANCE instance,
                                             TYPE[] types,
                                             Function<INSTANCE, Consumer<TYPE>> toAddToSupplier)
   {
      return addArray(instance, types, s -> s, toAddToSupplier);
   }

   static <INSTANCE, FROM, TO> INSTANCE addArray(INSTANCE instance,
                                                 FROM[] types,
                                                 Function<FROM, TO> renderer,
                                                 Function<INSTANCE, Consumer<TO>> toAddToSupplier)
   {
      requireNonNull(types);

      Consumer<TO> renderingConsumer = toAddToSupplier.apply(instance);

      for (FROM type : types)
      {
         requireNonNull(type);
         renderingConsumer.accept(renderer.apply(type));
      }

      return instance;
   }

   static <INSTANCE, TYPE> INSTANCE setTypeRenderer(INSTANCE instance,
                                                    TYPE type,
                                                    BiConsumer<INSTANCE, Function<RenderingContext, TYPE>> toAddToSupplier)
   {
      return setTypeRenderer(instance, type, (renderingContext, o) -> o, toAddToSupplier);
   }

   static <INSTANCE, FROM, TO> INSTANCE setTypeRenderer(INSTANCE instance,
                                                        FROM type,
                                                        BiFunction<RenderingContext, FROM, TO> renderer,
                                                        BiConsumer<INSTANCE, Function<RenderingContext, TO>> toAddToSupplier)
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

   static <INSTANCE, FROM, TO> INSTANCE setType(INSTANCE instance,
                                                FROM type,
                                                Function<FROM, TO> renderer,
                                                BiConsumer<INSTANCE, TO> toAddToSupplier)
   {
      requireNonNull(type);

      toAddToSupplier.accept(instance, renderer.apply(type));

      return instance;
   }

   static void renderElement(StringBuilder sb,
                             List<Function<RenderingContext, String>> renderers,
                             String after,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (!renderers.isEmpty())
      {
         sb.append(renderers.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
         sb.append(after);
      }
   }

   static void renderElement(StringBuilder sb,
                             List<Function<RenderingContext, String>> renderers,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (!renderers.isEmpty())
      {
         sb.append(renderers.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
      }
   }

   static void renderElement(StringBuilder sb,
                             String before,
                             List<Function<RenderingContext, String>> renderers,
                             String after,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (!renderers.isEmpty())
      {
         sb.append(before);
         sb.append(renderers.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
         sb.append(after);
      }
   }

   static void renderElement(StringBuilder sb,
                             String before,
                             List<Function<RenderingContext, String>> renderers,
                             RenderingContext renderingContext,
                             String delimiter)
   {
      if (!renderers.isEmpty())
      {
         sb.append(before);
         sb.append(renderers.stream().map(renderer -> renderer.apply(renderingContext)).collect(joining(", ")));
      }
   }
}
