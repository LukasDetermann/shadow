package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.renderer.RenderingContext;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/// implementation note: a bit overkill, but minimises bug potential
interface DslSupport
{
   static <INSTANCE> INSTANCE add(INSTANCE instance,
                                  Function<INSTANCE, Consumer<Function<RenderingContext, String>>> toAddToSupplier,
                                  String... strings)
   {
      return add(instance, toAddToSupplier, (renderingContext, s) -> s, strings);
   }

   @SafeVarargs
   static <INSTANCE, TYPE> INSTANCE add(INSTANCE instance,
                                        Function<INSTANCE, Consumer<Function<RenderingContext, String>>> toAddToSupplier,
                                        BiFunction<RenderingContext, TYPE, String> renderer,
                                        TYPE... types)
   {
      requireNonNull(types);

      Consumer<Function<RenderingContext, String>> renderingConsumer = toAddToSupplier.apply(instance);

      for (TYPE type : types)
      {
         requireNonNull(type);
         renderingConsumer.accept(renderingContext -> renderer.apply(renderingContext, type));
      }

      return instance;
   }

   static <INSTANCE> INSTANCE addStrings(INSTANCE instance,
                                         Function<INSTANCE, Consumer<String>> toAddToSupplier,
                                         String... strings)
   {
      return addStrings(instance, toAddToSupplier, s -> s, strings);
   }

   @SafeVarargs
   static <INSTANCE, TYPE> INSTANCE addStrings(INSTANCE instance,
                                               Function<INSTANCE, Consumer<String>> toAddToSupplier,
                                               Function<TYPE, String> renderer,
                                               TYPE... types)
   {
      requireNonNull(types);

      Consumer<String> renderingConsumer = toAddToSupplier.apply(instance);

      for (TYPE type : types)
      {
         requireNonNull(type);
         renderingConsumer.accept(renderer.apply(type));
      }

      return instance;
   }

   static <INSTANCE> INSTANCE set(INSTANCE instance,
                                  BiConsumer<INSTANCE, Function<RenderingContext, String>> toAddToSupplier,
                                  String s)
   {
      return set(instance, toAddToSupplier, (renderingContext, s1) -> s1, s);
   }

   static <INSTANCE, TYPE> INSTANCE set(INSTANCE instance,
                                        BiConsumer<INSTANCE, Function<RenderingContext, String>> toAddToSupplier,
                                        BiFunction<RenderingContext, TYPE, String> renderer,
                                        TYPE type)
   {
      requireNonNull(type);

      toAddToSupplier.accept(instance, renderingContext -> renderer.apply(renderingContext, type));

      return instance;
   }

   static <INSTANCE> INSTANCE setString(INSTANCE instance,
                                        BiConsumer<INSTANCE, String> toAddToSupplier,
                                        String s)
   {
      requireNonNull(s);

      toAddToSupplier.accept(instance, s);

      return instance;
   }

   static <INSTANCE, TYPE> INSTANCE setString(INSTANCE instance,
                                              BiConsumer<INSTANCE, String> toAddToSupplier,
                                              Function<TYPE, String> renderer,
                                              TYPE type)
   {
      requireNonNull(type);

      toAddToSupplier.accept(instance, renderer.apply(type));

      return instance;
   }
}
