package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.constructor.ConstructorRenderable;
import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;

import java.util.Arrays;
import java.util.List;

public interface ClassBodyStep
      extends ClassRenderable
{
   ClassRenderable body(String body);

   ClassBodyStep field(String... fields);

   default ClassBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   ClassBodyStep field(List<? extends FieldRenderable> fields);

   ClassBodyStep method(String... methods);

   default ClassBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   ClassBodyStep method(List<? extends MethodRenderable> methods);

   ClassBodyStep inner(String... inner);

   default ClassBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   ClassBodyStep inner(List<? extends DeclaredRenderable> inner);

   ClassBodyStep instanceInitializer(String... instanceInitializers);

   ClassBodyStep staticInitializer(String... staticInitializer);

   ClassBodyStep constructor(String... constructors);

   default ClassBodyStep constructor(ConstructorRenderable... constructors)
   {
      return constructor(Arrays.asList(constructors));
   }

   ClassBodyStep constructor(List<? extends ConstructorRenderable> constructors);
}