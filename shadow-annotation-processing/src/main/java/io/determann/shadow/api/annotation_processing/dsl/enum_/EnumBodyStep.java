package io.determann.shadow.api.annotation_processing.dsl.enum_;

import io.determann.shadow.api.annotation_processing.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.annotation_processing.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.annotation_processing.dsl.field.FieldRenderable;
import io.determann.shadow.api.annotation_processing.dsl.method.MethodRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface EnumBodyStep
      extends EnumRenderable
{
   @Contract(value = "_ -> new", pure = true)
   EnumRenderable body(String body);

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep field(String... fields);

   @Contract(value = "_ -> new", pure = true)
   default EnumBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep field(List<? extends FieldRenderable> fields);

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep method(String... methods);

   @Contract(value = "_ -> new", pure = true)
   default EnumBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep method(List<? extends MethodRenderable> methods);

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep inner(String... inner);

   @Contract(value = "_ -> new", pure = true)
   default EnumBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep inner(List<? extends DeclaredRenderable> inner);

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep instanceInitializer(String... instanceInitializers);

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep staticInitializer(String... staticInitializer);

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep constructor(String... constructors);

   @Contract(value = "_ -> new", pure = true)
   default EnumBodyStep constructor(ConstructorRenderable... constructors)
   {
      return constructor(Arrays.asList(constructors));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumBodyStep constructor(List<? extends ConstructorRenderable> constructors);
}