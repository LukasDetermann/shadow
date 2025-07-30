package io.determann.shadow.api.dsl.enum_;

import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;

import java.util.Arrays;
import java.util.List;

public interface EnumBodyStep
      extends EnumRenderable
{
   EnumRenderable body(String body);

   EnumBodyStep field(String... fields);

   default EnumBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   EnumBodyStep field(List<? extends FieldRenderable> fields);

   EnumBodyStep method(String... methods);

   default EnumBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   EnumBodyStep method(List<? extends MethodRenderable> methods);

   EnumBodyStep inner(String... inner);

   default EnumBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   EnumBodyStep inner(List<? extends DeclaredRenderable> inner);

   EnumBodyStep instanceInitializer(String... instanceInitializers);

   EnumBodyStep staticInitializer(String... staticInitializer);

   EnumBodyStep constructor(String... constructors);

   default EnumBodyStep constructor(ConstructorRenderable... constructors)
   {
      return constructor(Arrays.asList(constructors));
   }

   EnumBodyStep constructor(List<? extends ConstructorRenderable> constructors);
}