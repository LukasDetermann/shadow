package io.determann.shadow.api.annotation_processing.dsl.class_;

import io.determann.shadow.api.annotation_processing.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.annotation_processing.dsl.field.FieldRenderable;
import io.determann.shadow.api.annotation_processing.dsl.method.MethodRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ClassBodyStep
      extends ClassRenderable
{
   @Contract(value = "_ -> new", pure = true)
   ClassRenderable body(String body);

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep field(String... fields);

   @Contract(value = "_ -> new", pure = true)
   default ClassBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep field(List<? extends FieldRenderable> fields);

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep method(String... methods);

   @Contract(value = "_ -> new", pure = true)
   default ClassBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep method(List<? extends MethodRenderable> methods);

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep inner(String... inner);

   @Contract(value = "_ -> new", pure = true)
   default ClassBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep inner(List<? extends DeclaredRenderable> inner);

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep instanceInitializer(String... instanceInitializers);

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep staticInitializer(String... staticInitializer);

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep constructor(String... constructors);

   @Contract(value = "_ -> new", pure = true)
   default ClassBodyStep constructor(ConstructorRenderable... constructors)
   {
      return constructor(Arrays.asList(constructors));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassBodyStep constructor(List<? extends ConstructorRenderable> constructors);
}