package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.renderer.RenderingContext;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

interface DslSupport
{

   static void add(List<Function<RenderingContext, String>> toAddTo, String[] strings)
   {
      requireNonNull(strings);
      for (String string : strings)
      {
         requireNonNull(string);
         toAddTo.add(renderingContext -> string);
      }
   }

   static <TYPE> void add(List<Function<RenderingContext, String>> toAddTo,
                          TYPE[] types,
                          BiFunction<RenderingContext, TYPE, String> renderer)
   {
      requireNonNull(types);
      for (TYPE type : types)
      {
         requireNonNull(type);
         toAddTo.add(renderingContext -> renderer.apply(renderingContext, type));
      }
   }

   static void add(List<Function<RenderingContext, String>> toAddTo,
                   Function<RenderingContext, String> renderer)
   {
      toAddTo.add(renderer);
   }
}
